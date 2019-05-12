package com.core.java.essentials;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.core.java.rpgbase.player.Armor;
import com.core.java.rpgbase.skills.ArmorSkills;
import com.core.java.rpgbase.skills.SkilltreeCommand;
import com.core.java.rpgbase.skills.SkilltreeListener;
import com.core.java.rpgbase.player.EXP;
import com.core.java.rpgbase.player.Weapons;
import com.core.java.rpgbase.EntityIncreases;
import com.core.java.rpgbase.RPGFunctions;
import com.core.java.rpgbase.bossbars.BossBarManager;
import com.core.java.economy.EconCommands;
import com.core.java.essentials.commands.Codex;
import com.core.java.essentials.commands.DataReloadCommand;
import com.core.java.essentials.commands.ExpCommand;
import com.core.java.essentials.commands.FlyCommand;
import com.core.java.essentials.commands.GUICommand;
import com.core.java.essentials.commands.GUIListener;
import com.core.java.essentials.commands.GamemodeCommand;
import com.core.java.essentials.commands.HashmapCommand;
import com.core.java.essentials.commands.HealCommand;
import com.core.java.essentials.commands.HelpCommand;
import com.core.java.essentials.commands.InfoCommand;
import com.core.java.essentials.commands.LagCommand;
import com.core.java.essentials.commands.ManaCommand;
import com.core.java.essentials.commands.SpawnCommand;
import com.core.java.essentials.commands.StatsCommand;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Main extends JavaPlugin {
	
	
	//TODO LIST:
	/* 
	 * Make a way of making EXP bottles (using rpg xp?)
	 * Slimes and Magma cubes levels broken, and name keeps doubling up
	 * Anvils custom repair system
	 * 
	 */
	
	//TODO FUTURE:
	/* 
	 * Base Menu Cmd (contains all useful cmd guis)
	 * Parties, EXP Boosts (compounding)
	 * Some sort of repair feature
	 * Bank
	 * Baltop
	 * Youtuber and Twitch Ranks with Perks
	 * Find a way to make ranks less buggy, like a permission clump or something.
	 */
	
	public final String version = "0.0.1";
	public final String noperm = "&cNo permission!";
	
	public int uniquejoins = 0;
	public int getJoins() {
		File jFile = new File("plugins/Core/config.yml");
        FileConfiguration jData = YamlConfiguration.loadConfiguration(jFile);
        return jData.getInt("joins");
	}
	
	public void addJoins() {
		File jFile = new File("plugins/Core/config.yml");
        FileConfiguration jData = YamlConfiguration.loadConfiguration(jFile);
        try {
        	if (jData.contains("joins")) {
        		jData.set("joins", 0);
        	} else {
        		jData.set("joins", jData.getInt("joins") + 1);
        	}
        	jData.save(jFile);
        } catch (IOException exception) {
           exception.printStackTrace();
        }
	}

	public static Main getInstance() {
		return JavaPlugin.getPlugin(Main.class);
	}
	
	public BossBarManager barManager = new BossBarManager();
	public BossBarManager getBarManager() {
		return barManager;
	}
	
	public void updateAttackSpeed(Player p) {
		if (p.getInventory().getItemInMainHand() != null) {
			ItemStack i = p.getInventory().getItemInMainHand();
			if (i.hasItemMeta() && i.getItemMeta().hasLore() && i.getItemMeta().getLore().toString().contains("Attack Speed")) {
				int index = 0;
				for (String s : i.getItemMeta().getLore()) {
					if (s.contains("Attack Speed")) {
						break;
					} else {
						index++;
					}
				}
				String as = i.getItemMeta().getLore().get(index);
				as = ChatColor.stripColor(as);
				as = as.replace("Attack Speed: ", "");
				if (Double.valueOf(as) instanceof Double) {
					p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(Double.valueOf(as));
					return;
				}
			}
		}
		p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(Double.valueOf(getValue(p, "AttackSpeed")));
	}

	public Map<UUID, Integer> cmana = new HashMap<UUID, Integer>();
	public Map<UUID, Integer> getCManaMap() {
		return cmana;
	}
	
	public int getMana(Player p) {
		return cmana.get(p.getUniqueId());
	}
	
	public void setMana(Player p, int mana) {
		cmana.replace(p.getUniqueId(), mana);
	}
	
	public void updateExpBar(Player p) {
		int maxmana = getManaMap().get(p.getUniqueId());
		int cmana = getCManaMap().get(p.getUniqueId());
		p.setExp(Math.min(((1.0F * cmana) / (1.0F * maxmana)), 0.99F));
	}
	
	public Map<UUID, Integer> mana = new HashMap<UUID, Integer>();
	public Map<UUID, Integer> getManaMap() {
		return mana;
	}
	
	public Map<UUID, Integer> manaRegen = new HashMap<UUID, Integer>();
	public Map<UUID, Integer> getManaRegenMap() {
		return manaRegen;
	}
	
	public Map<UUID, Double> ad = new HashMap<UUID, Double>();
	public Map<UUID, Double> getAdMap() {
		return ad;
	}
	
	public Map<UUID, List<String>> abilities = new HashMap<UUID, List<String>>();
	public Map<UUID, List<String>> getAbilities() {
		return abilities;
	}
	
	public Map<UUID, Integer> level = new HashMap<UUID, Integer>();
	public Map<UUID, Integer> getLevelMap() {
		return level;
	}
	
	public Map<UUID, Integer> exp = new HashMap<UUID, Integer>();
	public Map<UUID, Integer> getExpMap() {
		return exp;
	}
	
	public Map<UUID, Integer> sp = new HashMap<UUID, Integer>();
	public Map<UUID, Integer> getSPMap() {
		return sp;
	}
	
	/*public int getManaRegen (Player p) {
		return Integer.valueOf(String.valueOf(Math.round((Math.sqrt((mana.get(p.getUniqueId()) * 0.0005D))))));
	}*/
	
	public int getExp(Player p) {
		return Integer.valueOf(getValue(p, "Exp"));
	}
	
	public int getLevel(Player p) {
		return Integer.valueOf(getValue(p, "Level"));
	}
	
	public int getExpMax(Player p) {
		return (int) (Math.pow(getLevel(p), 2.2) * 77) + 500;
	}
	
	@Override
	public void onEnable() {
		
		so("&cCORE&7: &fCore Plugin Version &c" + version + " &fEnabled!");
		getCommand("help").setExecutor(new HelpCommand());
		getCommand("fly").setExecutor(new FlyCommand());
		getCommand("gms").setExecutor(new GamemodeCommand());
		getCommand("gmc").setExecutor(new GamemodeCommand());
		getCommand("gmss").setExecutor(new GamemodeCommand());
		getCommand("spawn").setExecutor(new SpawnCommand());
		getCommand("setspawn").setExecutor(new SpawnCommand());
		getCommand("armor").setExecutor(new GUICommand());
		getCommand("lag").setExecutor(new LagCommand());
		getCommand("datareload").setExecutor(new DataReloadCommand());
		getCommand("set").setExecutor(new HashmapCommand());
		getCommand("money").setExecutor(new EconCommands());
		getCommand("level").setExecutor(new ExpCommand());
		getCommand("setlevel").setExecutor(new ExpCommand());
		getCommand("addlevel").setExecutor(new ExpCommand());
		getCommand("setexp").setExecutor(new ExpCommand());
		getCommand("addexp").setExecutor(new ExpCommand());
		getCommand("info").setExecutor(new InfoCommand());
		getCommand("stats").setExecutor(new StatsCommand());
		getCommand("sp").setExecutor(new SkilltreeCommand());
		getCommand("heal").setExecutor(new HealCommand());
		getCommand("mana").setExecutor(new ManaCommand());
		so("&cCORE&7: &fCommands Enabled!");
		
		Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
		Bukkit.getPluginManager().registerEvents(new Motd(), this);
		Bukkit.getPluginManager().registerEvents(new ChatFunctions(), this);
		Bukkit.getPluginManager().registerEvents(new RPGFunctions(), this);
		Bukkit.getPluginManager().registerEvents(new Armor(), this);
		Bukkit.getPluginManager().registerEvents(new EXP(), this);
		Bukkit.getPluginManager().registerEvents(new Codex(), this);
		Bukkit.getPluginManager().registerEvents(new EntityIncreases(), this);
		Bukkit.getPluginManager().registerEvents(new ArmorSkills(), this);
		Bukkit.getPluginManager().registerEvents(new Weapons(), this);
		Bukkit.getPluginManager().registerEvents(new KDRListener(), this);
		Bukkit.getPluginManager().registerEvents(new SkilltreeListener(), this);
		so("&cCORE&7: &fListeners Enabled!");
		
		GUICommand.createArmorInv();
		HelpCommand.createHelpGui();
		GUICommand.createCombatInv();
		SkilltreeListener.createSpInv();
		so("&cCORE&7: &fGUIs Enabled!");
		
		manaRegen();
		hpPeriodic();
		updatePeriodic();
		armorPeriodic();
		absorption();
		so("&cCORE&7: &fPeriodics Enabled!");
	}
	
	@Override
	public void onDisable() {
		
		updateFunc();
		so("&cCORE&7: &fCore Plugin Version &c" + version + " &fDisabled!");
		
	}
	
	public void createRecipes() {
		ItemStack armorgem = new ItemStack(Material.EMERALD);
		ItemMeta armorgemmeta = armorgem.getItemMeta();
		armorgemmeta.setDisplayName(ChatColor.GREEN + "Armor Gem");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Used to craft Armor.");
		armorgemmeta.setLore(lore);
		armorgem.addEnchantment(Enchantment.MENDING, 1);
		armorgem.setItemMeta(armorgemmeta);
		
		
	}
	
	public void updatePeriodic() {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (Bukkit.getServer().getOnlinePlayers().size() >= 1) {
					updateFunc();
				}
			}
		}.runTaskTimer(this, 12000L, 12000L);
	}
	
	public void updateFunc() {
		so("&cCORE&7: &fPeriodic Update Started.");
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			hashmapUpdate(p);
		}
		so("&cCORE&7: &fPeriodic Update Finished.");
	}
	
	public void hpPeriodic() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					sendHp(p);
				}
			}
		}.runTaskTimer(this, 20L, 5L);
	}
	
	public void armorPeriodic() {
		new BukkitRunnable() {
			public void run() {
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					if (!p.isDead()) {
						String set = Armor.getSet(p);
						Armor.updateSet(p, set);
					}
				}
			}
		}.runTaskTimer(this, 200L, 1200L);
	}
	
	public void manaRegen() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					if (mana.get(p.getUniqueId()) != null) {
						if (getMana(p) <= mana.get(p.getUniqueId())) {
							if (!p.isDead()) {
								if (p.isSleeping()) {
									setMana(p, Integer.valueOf(Math.min(getMana(p) + 20 + getManaRegenMap().get(p.getUniqueId()) * 10, mana.get(p.getUniqueId()))));
								} else {
									setMana(p, Integer.valueOf(Math.min(getMana(p) + getManaRegenMap().get(p.getUniqueId()), mana.get(p.getUniqueId()))));
								}
								updateExpBar(p);
							} else {
								setMana(p, 0);
							}
						}
					}
				}
			}
		}.runTaskTimerAsynchronously(this, 20L, 1L);
	}
	
	public void absorption() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					if (p.hasPotionEffect(PotionEffectType.ABSORPTION)) {
						double heal = 4;
						int amp = p.getPotionEffect(PotionEffectType.ABSORPTION).getAmplifier();
						int duration = p.getPotionEffect(PotionEffectType.ABSORPTION).getDuration();
						p.removePotionEffect(PotionEffectType.ABSORPTION);
						if (!p.isDead()) {
							p.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, p.getLocation(), 50, 1, 1, 1);
							p.setHealth(Math.min(heal + p.getHealth(), p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()));
						}
						if (amp >= 1) {
							if (amp - 1 != 0) {
								p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, duration, amp - 1));
							}
						}
					}
				}
			}
		}.runTaskTimer(this, 20L, 20L);
	}
	
	public void hashmapUpdate(Player p) {
		UUID uuid = p.getUniqueId();
        mana.replace(uuid, Integer.valueOf(getValue(p, "Mana")));
        manaRegen.replace(uuid, Integer.valueOf(getValue(p, "ManaRegen")));
        ad.replace(uuid, Double.valueOf(getValue(p, "AD")));
        level.replace(uuid, Integer.valueOf(getValue(p, "Level")));
        exp.replace(uuid, Integer.valueOf(getValue(p, "Exp")));
        sp.replace(uuid, Integer.valueOf(getValue(p, "SP")));
        List<String> abs = new ArrayList<String>();
        abs.add(getValue(p, "AbilityOne"));
        abs.add(getValue(p, "AbilityTwo"));
        abs.add(getValue(p, "AbilityThree"));
        abs.add(getValue(p, "AbilityFour"));
        abilities.replace(uuid, abs);
        p.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(ad.get(uuid));
        updateAttackSpeed(p);
	}
	
	public void levelup (Player p) {
		while (getExpMax(p) <= getExp(p) && getLevel(p) < 100) {
			int maxlevel = getExpMax(p);
			int newexp = (getExp(p) - maxlevel);
			int newlevel = getLevel(p) + 1;
			getLevelMap().replace(p.getUniqueId(), newlevel);
			setIntValue(p, "Level", newlevel);
			maxlevel = getExpMax(p);
			getExpMap().replace(p.getUniqueId(), newexp);
			setIntValue(p, "Exp", newexp);
			int newsp = getSPMap().get(p.getUniqueId()) + 2;
			getSPMap().replace(p.getUniqueId(), newsp);
			setIntValue(p, "SP", newsp);
			Main.msg(p, "&7&m--------------------------");
			Main.msg(p, "");
			Main.msg(p, "&7» &e&lLEVEL UP: &6" + (newlevel - 1) + " &f-> &6" + newlevel);
			Main.msg(p, "&7» &e&lSP INCREASE: &f+2");
			Main.msg(p, "");
			Main.msg(p, "&7&m--------------------------");
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
		}
	}
	
	public static void sendHp(Player p) {
		DecimalFormat dF = new DecimalFormat("#.##");
		int max = getInstance().getExpMax(p);
		int exp = getInstance().getExp(p);
		double exppercent = ((1.0 * exp) / (1.0 * max)) * 100;
		int mana = getInstance().getMana(p);
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(color("&8---&r&8« &c" + dF.format(p.getHealth()) + " HP &8|| &b" + mana + " M &8|| &a" + dF.format(exppercent) + "% XP &8|| &e" + getInstance().level.get(p.getUniqueId()) + " LVL " + "&8»---")));
	}
	
	public void setStringValue(Player p, String text, String value) {
		File pFile = new File("plugins/Core/data/" + p.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            pData.set(text, value);
            pData.save(pFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
	}
	public void setDoubleValue(Player p, String text, double value) {
		File pFile = new File("plugins/Core/data/" + p.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            pData.set(text, value);
            pData.save(pFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
	}
	public void setIntValue(Player p, String text, int value) {
		File pFile = new File("plugins/Core/data/" + p.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            pData.set(text, value);
            pData.save(pFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
	}
	public static String getValue(Player p, String text) {
		File pFile = new File("plugins/Core/data/" + p.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        return String.valueOf(pData.get(text));
	}
	
	public static void msg(Player p, String text) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', text));
	}
	
	public static void so(String text) {
		Bukkit.getServer().getConsoleSender().sendMessage((ChatColor.translateAlternateColorCodes('&', text)));
	}
	
	public static String color(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}
	
}
