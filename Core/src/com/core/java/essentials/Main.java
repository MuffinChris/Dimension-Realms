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
import com.core.java.rpgbase.player.EXP;
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
import com.core.java.essentials.commands.HelpCommand;
import com.core.java.essentials.commands.LagCommand;
import com.core.java.essentials.commands.SpawnCommand;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Main extends JavaPlugin {
	
	public static final double basehp = 100;
	public static final double leatherA = 0;
	public static final double goldenA = 25;
	public static final double chainmailA = 50;
	public static final double ironA = 75;
	public static final double diamondA = 100;
	
	public final String version = "0.0.1";
	public final String noperm = "&cNo permission!";

	public static Main getInstance() {
		return JavaPlugin.getPlugin(Main.class);
	}
	
	public BossBarManager barManager = new BossBarManager();
	public BossBarManager getBarManager() {
		return barManager;
	}
	
	public ArrayList<Map> maps = new ArrayList<>();
	
	public void addToMaps() {
		maps.add(mana);
		maps.add(manaRegen);
		maps.add(ad);
		maps.add(abilities);
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
	
	public int getManaRegen (Player p) {
		return Integer.valueOf(String.valueOf(Math.round((Math.sqrt((mana.get(p.getUniqueId()) * 0.0005D))))));
	}
	
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
		so("&cCORE&7: &fListeners Enabled!");
		
		GUICommand.createArmorInv();
		HelpCommand.createHelpGui();
		GUICommand.createCombatInv();
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
				updateFunc();
			}
		}.runTaskTimer(this, 12000L, 12000L);
	}
	
	public void updateFunc() {
		so("&cCORE&7: &fPeriodic Update Started.");
		int i = 0;
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			
			if (mana.containsKey(p.getUniqueId())) {
				i++;
			}
			if (manaRegen.containsKey(p.getUniqueId())) {
				i++;
			}
			if (ad.containsKey(p.getUniqueId())) {
				i++;
			}
			if (abilities.containsKey(p.getUniqueId())) {
				i++;
			}
			if (level.containsKey(p.getUniqueId())) {
				i++;
			}
			if (exp.containsKey(p.getUniqueId())) {
				i++;
			}
			if (sp.containsKey(p.getUniqueId())) {
				i++;
			}
			hashmapUpdate(p);
		}
		so("&cCORE&7: &fPeriodic Update Considered " + i + " Hashmap Instances.");
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
						if (p.getLevel() < mana.get(p.getUniqueId())) {
							if (!p.isDead()) {
								if (p.isSleeping()) {
									p.setLevel(p.getLevel() + 20 + manaRegen.get(p.getUniqueId()) * 10);
								} else {
									p.setLevel(p.getLevel() + manaRegen.get(p.getUniqueId()));
								}
							} else {
								p.setLevel(0);
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
						double heal = 1;
						int amp = p.getPotionEffect(PotionEffectType.ABSORPTION).getAmplifier();
						int duration = p.getPotionEffect(PotionEffectType.ABSORPTION).getDuration();
						p.removePotionEffect(PotionEffectType.ABSORPTION);
						if (!p.isDead()) {
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
        manaRegen.replace(uuid, getManaRegen(p));
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
	}
	
	public static void sendHp(Player p) {
		DecimalFormat dF = new DecimalFormat("#.##");
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(color("&8---&r&8« &c" + dF.format(p.getHealth()) + " HP &8|| &a" + dF.format(p.getExp() * 100) + "% XP &8|| &e" + getInstance().level.get(p.getUniqueId()) + " LVL " + "&8»---")));
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
