package com.core.java.essentials;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.core.java.Constants;
import com.core.java.Numbers;
import com.core.java.essentials.commands.*;
import com.core.java.rpgbase.HorseCommand;
import com.core.java.rpgbase.player.*;
import com.core.java.rpgbase.player.professions.PlayerProf;
import com.core.java.rpgbase.player.professions.ProfCommand;
import com.destroystokyo.paper.Title;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.core.java.rpgbase.skills.ArmorSkills;
import com.core.java.rpgbase.skills.SkilltreeCommand;
import com.core.java.rpgbase.skills.SkilltreeListener;
import com.core.java.rpgbase.EntityIncreases;
import com.core.java.rpgbase.RPGFunctions;
import com.core.java.rpgbase.bossbars.BossBarManager;
import com.core.java.rpgbase.entities.PlayerList;
import com.core.java.rpgbase.entities.PlayerListManager;
import com.core.java.economy.EconCommands;
import com.core.java.enchantments.Serration;
import com.core.java.reflection.rutils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.ViaAPI;

public class Main extends JavaPlugin {
	
	
	//TODO LIST:
	
	/*

	Make INFO CMD A GUI!!!

	Possible Name: The Rift
	or... The Rift MC
	Rift Dimensions? eh.



	Create Ability Class with cast method and stuff, and polarity.
	Abilities of different polarity increase cost by 50% up to 100% based on how many conflicting there are.
	Select item to use for casting, base item is sword, axe, shovel, pick.
	Passives are underneath ability selected for item, 2 passive slots.


	Auction with glass block housing
	Upgreadeable Mount
	Anti Fly kick when using ability by cancelling kick event
	Soulbound Enchantment.
	Item combining so you don't have to choosebetween items.
	Make explosion regen?
	 * Info command overriden by WorldEdit
	 * Update /armor command
	 * Spells do not register kills when used raw
	 * Custom Enchanting GUI or Way to make axes and stuff get ench
	 * Make Skilltrees
	 */
	
	//TODO FUTURE:
	/*
	* Town System Stuff:
	* Resource collectors, depending on biome. They build worldedit schematic and cannot be touched within scehm
	* radius so that it can be removed correctly. Have hologram over base block with options.
	* Winner of war gets % resources and tributes.
	*
	*
	*
	 * Remember to set world to hard difficulty
	 * Different ranks to uh rankup to
	 * Add referrals for invintg
	 * Stained Glass is fashionable
	 * https://www.youtube.com/watch?v=BcKzswBhjQM
	 * https://www.youtube.com/watch?v=Y1BFOzXwVu0 for builds
	 * Professions
	 * - Toolsmith (make and repair tools)
	 * - Armorer (make and repair armor)
	 * - Farmer (create custom food and grow faster food)
	 * - Enchanter (custom enchants)
	 * - Miner (tools for mining faster, better, and synthesizing ores)
	 * - Fisher (hella fish bro)
	 * Have a better more thematic crates system. Like going mining for loot gems that have to be synthesized.
	 * Also all loot gems have to be synthesized :P
	 * Souls system
	 * Making custom mobs, bosses, spawners, etc
	 * Base Menu Cmd (contains all useful cmd guis)
	 * Parties, EXP Boosts (compounding)
	 * Anvils custom repair system
	 * Make a way of wearing Elytra
	 * Sethome on bed
	 * Bank
	 * Baltop
	 * Youtuber and Twitch Ranks with Perks
	 * Find a way to make ranks less buggy, like a permission clump or something.
	 */
	
	public final String version = "0.0.1";
	public final String noperm = "&cNo permission.";
	
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
        	if (!jData.contains("joins")) {
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

	public Map<Player, Party> invite = new HashMap<Player, Party>();
	public Map<Player, Party> getInvites() {
		return invite;
	}

	public Map<Player, Boolean> pchat = new HashMap<Player, Boolean>();
	public Map<Player, Boolean> getPChat() {
		return pchat;
	}

	private PartyManager pm;
	public PartyManager getPM() {
		return pm;
	}
	
	public rutils rlib = new rutils();
	
	public rutils getRlib() {
		return rlib;
	}
	
	public ViaAPI vapi = Via.getAPI();
	
	public ViaAPI getViaAPI() {
		return vapi;
	}
	
	public BossBarManager barManager = new BossBarManager();
	public BossBarManager getBarManager() {
		return barManager;
	}
	
	public PlayerListManager pManager = new PlayerListManager();
	public PlayerListManager getPManager() {
		return pManager;
	}
	
	public playerinfoManager pinf = new playerinfoManager();
	public playerinfoManager getpinf() {
		return pinf;
	}
	
	public double getAttackSpeed(Player p) {
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
					return Double.valueOf(as);
				}
			}
		}
		return Double.valueOf(getValue(p, "AttackSpeed"));
	}
	
	public void updateAttackSpeed(Player p) {
		double as = getAttackSpeed(p);
		p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(as);
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
	
	public Map<UUID, Double> armor = new HashMap<UUID, Double>();
	public Map<UUID, Double> getArmorMap() {
		return armor;
	}
	
	public Map<UUID, Double> mr = new HashMap<UUID, Double>();
	public Map<UUID, Double> getMRMap() {
		return mr;
	}

	public Map<UUID, Boolean> godmode = new HashMap<>();
	public Map<UUID, Boolean> getGodMode() {
		return godmode;
	}
	
	public void updateExpBar(Player p) {
		int maxmana = getManaMap().get(p.getUniqueId());
		int cmana = getCManaMap().get(p.getUniqueId());
		p.setExp(Math.max(Math.min(((1.0F * cmana) / (1.0F * maxmana)), 0.99F), 0.0F));
	}
	
	public Map<UUID, Float> attackCooldown = new HashMap<UUID, Float>();
	public Map<UUID, Float> getACMap() {
		return attackCooldown;
	}
	
	public float getAC(Player p) {
		return attackCooldown.get(p.getUniqueId());
	}
	
	public void setAC(Player p, float f) {
		attackCooldown.replace(p.getUniqueId(), f);
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
	
	public Map<UUID, Double> exp = new HashMap<UUID, Double>();
	public Map<UUID, Double> getExpMap() {
		return exp;
	}
	
	public Map<UUID, Integer> sp = new HashMap<UUID, Integer>();
	public Map<UUID, Integer> getSPMap() {
		return sp;
	}

	public Map<UUID, Boolean> fall = new HashMap<UUID, Boolean>();
	public Map<UUID, Boolean> getFall() {
		return fall;
	}

	public Map<UUID, PlayerProf> profs = new HashMap<UUID, PlayerProf>();
	public Map<UUID, PlayerProf> getProf() {
		return profs;
	}
	
	/*public int getManaRegen (Player p) {
		return Integer.valueOf(String.valueOf(Math.round((Math.sqrt((mana.get(p.getUniqueId()) * 0.0005D))))));
	}*/
	
	public double getExp(Player p) {
		if (getExpMap().get(p.getUniqueId()) != null) {
			return getExpMap().get(p.getUniqueId());
		}
		return 0.0;
	}
	
	public int getLevel(Player p) {
		return getLevelMap().get(p.getUniqueId());
	}
	
	public double getExpMax(Player p) {
		return ((Math.pow(getLevel(p), 3) * 77) + 500);
	}

	private ProtocolManager protocolManager;
	public void setupPacketListeners() {
		protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.WORLD_PARTICLES) {
			@Override
			public void onPacketSending(PacketEvent event) {
					if (event.getPacketType() == PacketType.Play.Server.WORLD_PARTICLES) {
						if (event.getPacket().getNewParticles().getValues().get(0).getParticle().name().contains("DAMAGE_INDICATOR")) {
							event.setCancelled(true);
						}
					}
			}
		});


	}

	private Numbers numbers;
	public Numbers gn() {
		return numbers;
	}

	@Override
	public void onEnable() {
		
		so("&cCORE&7: &fCore Plugin Version &c" + version + " &fEnabling!");
		numbers = new Numbers();
		//Plugin vault = getServer().getPluginManager().getPlugin("Vault");
		//Vault.hookChat();
		//ChatFunctions vaultSetup = new ChatFunctions();
		//vaultSetup.loadVault();
		//Bukkit.getServicesManager().register(Chat.class, new ChatManager(Permission), this, ServicePriority.Normal);

		this.setupChat();
		so("&cCORE&7: &fVault hooked!");

		protocolManager = ProtocolLibrary.getProtocolManager();
		so("&cCORE&7: &fProtocolLib hooked!");

		setupPacketListeners();
		so("&cCORE&7: &fProtocolLib Packet Listeners Enabled!");

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
		getCommand("information").setExecutor(new InfoCommand());
		getCommand("kdr").setExecutor(new KdrCommand());
		getCommand("stats").setExecutor(new SkilltreeCommand());
		getCommand("heal").setExecutor(new HealCommand());
		getCommand("mana").setExecutor(new ManaCommand());
		getCommand("speed").setExecutor(new SpeedCommand());
		getCommand("bottle").setExecutor(new BottleCommand());
		getCommand("msg").setExecutor(new MsgCommand());
		getCommand("tell").setExecutor(new MsgCommand());
		getCommand("message").setExecutor(new MsgCommand());
		getCommand("r").setExecutor(new MsgCommand());
		getCommand("reply").setExecutor(new MsgCommand());
		getCommand("reset").setExecutor(new ResetCommand());
		getCommand("killall").setExecutor(new KillAllCommand());
		getCommand("god").setExecutor(new GodCommand());
		getCommand("invsee").setExecutor(new InvseeCommand());
		getCommand("addlore").setExecutor(new LoreCommand());
		getCommand("party").setExecutor(new PartyCommand());
		getCommand("horse").setExecutor(new HorseCommand());
		getCommand("list").setExecutor(new ListCommand());
		getCommand("rtp").setExecutor(new RTPCommand());
		getCommand("prof").setExecutor(new ProfCommand());
		so("&cCORE&7: &fCommands Enabled!");
		
		Bukkit.getPluginManager().registerEvents(new infoListeners(), this);
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
		Bukkit.getPluginManager().registerEvents(new BottleCommand(), this);
		Bukkit.getPluginManager().registerEvents(new PartyCommand(), this);
		Bukkit.getPluginManager().registerEvents(new HorseCommand(), this);
		Bukkit.getPluginManager().registerEvents(new RTPCommand(), this);
		Bukkit.getPluginManager().registerEvents(new ProfCommand(), this);
		so("&cCORE&7: &fListeners Enabled!");

		pm = new PartyManager();
		
		GUICommand.createArmorInv();
		HelpCommand.createHelpGui();
		GUICommand.createCombatInv();
		SkilltreeListener.createSpInv();
		so("&cCORE&7: &fGUIs Enabled!");
		
		manaRegen();
		hpPeriodic();
		updatePeriodic();
		absorption();
		//acPeriodic();
		midPeriodic();
		midPeriodicSync();
		so("&cCORE&7: &fPeriodics Enabled!");
		
		Serration.register();
		so("&cCORE&7: &fEnchantments Registered!");
		
		int count = 0;
		for (World w : Bukkit.getWorlds()) {
			for (Entity e : w.getEntities()) {
				if (e.getType() != EntityType.ARMOR_STAND) {
					getPManager().setPlayerList(e, new PlayerList());
					count++;
				}
			}
		}
		so("&cCORE&7: &fEntities Updated with PlayerLists! &8(&f" + count + "&8)");

		so("&cCORE&7: &fCore Plugin Version &c" + version + " &fEnabled!");
	}
	
	@Override
	public void onDisable() {

		for (Hologram h : HologramsAPI.getHolograms(this)) {
			h.delete();
		}
		updateFunc();
		so("&cCORE&7: &fCore Plugin Version &c" + version + " &fDisabled!");

	}

	private Chat chat;

	private boolean setupChat() {
		RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
		chat = rsp.getProvider();
		return chat != null;
	}

	public Chat getChat() {
		return chat;
	}

	public void midPeriodic() {
		new BukkitRunnable() {
			public void run() {
				for (Player  p : Bukkit.getOnlinePlayers()) {
					ChatFunctions.updateName(p);
				}
			}
		}.runTaskTimerAsynchronously(this, 10L, 80L);
	}

	public void midPeriodicSync() {
		new BukkitRunnable() {
			public void run() {
				for (Player  p : Bukkit.getOnlinePlayers()) {
					levelup(p);
				}
			}
		}.runTaskTimer(this, 10L, 80L);
	}

	public void partyStartup() {
		new BukkitRunnable() {
			@Override
			public void run() {
				getPM().cleanParties();
			}
		}.runTaskTimerAsynchronously(this, 1L, 200L);
	}

	public void updatePeriodic() {
		new BukkitRunnable() {
			@Override
			public void run() {
				getPManager().cleanPlayerList();
				if (Bukkit.getServer().getOnlinePlayers().size() >= 1) {
					updateFunc();
				}
			}
		}.runTaskTimer(this, 1000L, 6000L);
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
		}.runTaskTimer(this, 20L, 4L);
	}
	
	/*public void acPeriodic() {
		new BukkitRunnable() {
			public void run() {
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					CraftPlayer cp = (CraftPlayer) p;
					float f = cp.getHandle().s(0.0F);
					setAC(p, f);
				}
			}
		}.runTaskTimer(this, 1L, 1L);
	}*/
	
	public void manaRegen() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					if (mana.get(p.getUniqueId()) != null) {
						if (getMana(p) <= mana.get(p.getUniqueId())) {
							if (!p.isDead()) {
								int manaGain = getManaRegenMap().get(p.getUniqueId())/5;
								if (p.isSleeping()) {
									setMana(p, Math.min(getMana(p) + 20 + manaGain * 10, mana.get(p.getUniqueId())));
								} else {
									setMana(p, Math.min(getMana(p) + manaGain, mana.get(p.getUniqueId())));
								}
								updateExpBar(p);
								sendHp(p);
							} else {
								setMana(p, 0);
							}
						}
					}
				}
			}
		}.runTaskTimerAsynchronously(this, 20L, 4L);
	}
	
	public void absorption() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					if (p.hasPotionEffect(PotionEffectType.ABSORPTION)) {
						double heal = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() / 80.0;
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

	public void resetSP(Player p) {
		File pFile = new File("plugins/Core/data/" + p.getUniqueId() + ".yml");
		FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
		try {
			pData.set("Username", p.getName());
			pData.set("SP", getLevel(p) * Constants.SPPerLevel);
			pData.set("SPAD", 0);
			pData.set("SPHP", 0);
			pData.set("SPM", 0);
			pData.set("SPMR", 0);
			pData.set("BaseHP", Constants.BaseHP + Constants.HPPerLevel * getLevel(p));
			pData.set("Mana", Constants.BaseMana + Constants.ManaPerLevel * getLevel(p));
			pData.set("ManaRegen", Constants.BaseManaRegen);
			pData.set("Cmana", 0);
			pData.set("AD", 0.0);
			pData.set("HP", 0.0);
			getCManaMap().replace(p.getUniqueId(), 0);
			pData.save(pFile);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		hashmapUpdate(p);
	}

	public void resetPlayer(Player p) {
		File pFile = new File("plugins/Core/data/" + p.getUniqueId() + ".yml");
		FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
		try {
			pData.set("Username", p.getName());
			pData.set("Class", "None");
			pData.set("Level", 0);
			pData.set("Exp", 0.0);
			pData.set("SP", 0);
			pData.set("Mana", Constants.BaseMana);
			pData.set("ManaRegen", Constants.BaseManaRegen);
			pData.set("Cmana", 0);
			getCManaMap().replace(p.getUniqueId(), 0);
			pData.set("BaseHP", Constants.BaseHP);
			pData.set("BaseMana", Constants.BaseMana);
			pData.set("AD", 0.0);
			pData.set("HP", 0.0);
			pData.set("AttackSpeed", 4.0);
			pData.set("AbilityOne", "None");
			pData.set("AbilityTwo", "None");
			pData.set("AbilityThree", "None");
			pData.set("AbilityFour", "None");
			pData.set("Kills", 0);
			pData.set("Deaths", 0);
			pData.set("SPAD", 0);
			pData.set("SPHP", 0);
			pData.set("SPM", 0);
			pData.set("SPMR", 0);
			pData.set("Skills", new ArrayList<String>());
			pData.save(pFile);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		p.setFoodLevel(20);
		p.setSaturation(10.0F);
	}

	public void presetPlayer(Player p) {
		File pFile = new File("plugins/Core/data/" + p.getUniqueId() + ".yml");
		FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
		try {
			pData.set("Username", p.getName());
			if (!pData.isSet("Class")) {
				pData.set("Class", "None");
			}
			if (!pData.isSet("Level")) {
				pData.set("Level", 0);
			}
			if (!pData.isSet("Exp")) {
				pData.set("Exp", 0.0);
			}
			if (!pData.isSet("SP")) {
				pData.set("SP", 0);
			}
			if (!pData.isSet("Mana")) {
				pData.set("Mana", Constants.BaseMana);
			}
			if (!pData.isSet("ManaRegen")) {
				pData.set("ManaRegen", Constants.BaseManaRegen);
			}
			if (!pData.isSet("Cmana")) {
				pData.set("Cmana", 0);
			}
			if (!pData.isSet("BaseHP")) {
				pData.set("BaseHP", Constants.BaseHP);
			}
			if (!pData.isSet("BaseMana")) {
				pData.set("BaseMana", Constants.BaseMana);
			}
			if (!pData.isSet("AD")) {
				pData.set("AD", 0.0);
			}
			if (!pData.isSet("HP")) {
				pData.set("HP", 0.0);
			}
			if (!pData.isSet("AttackSpeed")) {
				pData.set("AttackSpeed", 4.0);
			}
			if (!pData.isSet("AbilityOne")) {
				pData.set("AbilityOne", "None");
			}
			if (!pData.isSet("AbilityTwo")) {
				pData.set("AbilityTwo", "None");
			}
			if (!pData.isSet("AbilityThree")) {
				pData.set("AbilityThree", "None");
			}
			if (!pData.isSet("AbilityFour")) {
				pData.set("AbilityFour", "None");
			}
			if (!pData.isSet("Kills")) {
				pData.set("Kills", 0);
			}
			if (!pData.isSet("Deaths")) {
				pData.set("Deaths", 0);
			}
			if (!pData.isSet("SPAD")) {
				pData.set("SPAD", 0);
			}
			if (!pData.isSet("SPHP")) {
				pData.set("SPHP", 0);
			}
			if (!pData.isSet("SPM")) {
				pData.set("SPM", 0);
			}
			if (!pData.isSet("SPMR")) {
				pData.set("SPMR", 0);
			}
			if (!pData.isSet("Skills")) {
				pData.set("Skills", new ArrayList<String>());
			}
			pData.save(pFile);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public void hashmapUpdate(Player p) {
		File pFile = new File("plugins/Core/data/" + p.getUniqueId() + ".yml");
		FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
		UUID uuid = p.getUniqueId();
		getManaMap().put(uuid, pData.getInt("Mana"));
		getManaRegenMap().put(uuid, pData.getInt("ManaRegen"));
		getAdMap().put(uuid, pData.getDouble("AD"));
		getLevelMap().put(uuid, pData.getInt("Level"));
		getExpMap().put(uuid, pData.getDouble("Exp"));
		getSPMap().put(uuid, pData.getInt("SP"));
		if (!getCManaMap().containsKey(uuid)) {
			getCManaMap().put(uuid, pData.getInt("Cmana"));
		}
		List<String> abs = new ArrayList<String>();
		abs.add(pData.getString("AbilityOne"));
		abs.add(pData.getString("AbilityTwo"));
		abs.add(pData.getString("AbilityThree"));
		abs.add(pData.getString("AbilityFour"));
		getAbilities().put(uuid, abs);
		if (getProf().containsKey(uuid)) {
			getProf().get(uuid).pullFiles();
		}

        updateAttackSpeed(p);
        Armor.updateSet(p);
        Weapons.updateInventory(p);
	}

	public void setupStats(Player p) {
		int SPAD = Integer.valueOf(Main.getValue(p, "SPAD"));
		int SPHP = Integer.valueOf(Main.getValue(p, "SPHP"));
		int SPM = Integer.valueOf(Main.getValue(p, "SPM"));
		int SPMR = Integer.valueOf(Main.getValue(p, "SPMR"));
		int SP = getLevel(p) * Constants.SPPerLevel;
		SP = SP - SPAD - SPHP - SPM - SPMR;
		getSPMap().replace(p.getUniqueId(), SP);
		setIntValue(p, "SP", SP);
		int level = getLevel(p);
		File pFile = new File("plugins/Core/data/" + p.getUniqueId() + ".yml");
		FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
		try {
			pData.set("BaseHP", Constants.BaseHP + (Constants.HPPerLevel * level));
			pData.set("BaseMana", Constants.BaseMana + (Constants.ManaPerLevel * level));
			pData.save(pFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SPM = pData.getInt("SPM");
		double manaUpgrade = Constants.ManaPerSP;
		int newmana = pData.getInt("BaseMana") + (int) (SPM * (manaUpgrade * pData.getInt("BaseMana")));
		setIntValue(p, "Mana", newmana);
		getManaMap().replace(p.getUniqueId(), newmana);
		Armor.updateSet(p);
		Weapons.updateInventory(p);
	}

	public void levelup(Player p, boolean silent) {
		if (silent) {
			int times = 0;
			while (getExpMax(p) <= getExp(p) && getLevel(p) < 100) {
				times++;
				double maxlevel = getExpMax(p);
				double newexp = (getExp(p) - maxlevel);
				int newlevel = getLevel(p) + 1;
				getLevelMap().replace(p.getUniqueId(), newlevel);
				setIntValue(p, "Level", newlevel);
				maxlevel = getExpMax(p);
				getExpMap().replace(p.getUniqueId(), newexp);
				setDoubleValue(p, "Exp", newexp);
				int newsp = getSPMap().get(p.getUniqueId()) + Constants.SPPerLevel;
				getSPMap().replace(p.getUniqueId(), newsp);
				setIntValue(p, "SP", newsp);
				File pFile = new File("plugins/Core/data/" + p.getUniqueId() + ".yml");
				FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
				try {
					pData.set("BaseHP", pData.getDouble("BaseHP") + Constants.HPPerLevel);
					pData.set("BaseMana", pData.getInt("BaseMana") + Constants.ManaPerLevel);
					pData.save(pFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
				int SPM = pData.getInt("SPM");
				double manaUpgrade = Constants.ManaPerSP;
				int newmana = pData.getInt("BaseMana") + (int) (SPM * (manaUpgrade * pData.getInt("BaseMana")));
				setIntValue(p, "Mana", newmana);
				getManaMap().replace(p.getUniqueId(), newmana);
			}
			if (times >= 1) {
				Armor.updateSet(p);
				Weapons.updateInventory(p);
			}
		}
	}

	public void levelup (Player p) {

		if (getExp(p) < 0) {
			setDoubleValue(p, "Exp", 0.0);
			getExpMap().replace(p.getUniqueId(), 0.0);
		}
		int times = 0;
		int level = getLevel(p);
		while (getExpMax(p) <= getExp(p) && getLevel(p) < 100) {
			times++;
			double maxlevel = getExpMax(p);
			double newexp = (getExp(p) - maxlevel);
			int newlevel = getLevel(p) + 1;
			getLevelMap().replace(p.getUniqueId(), newlevel);
			setIntValue(p, "Level", newlevel);
			maxlevel = getExpMax(p);
			getExpMap().replace(p.getUniqueId(), newexp);
			setDoubleValue(p, "Exp", newexp);
			int newsp = getSPMap().get(p.getUniqueId()) + Constants.SPPerLevel;
			getSPMap().replace(p.getUniqueId(), newsp);
			setIntValue(p, "SP", newsp);
			File pFile = new File("plugins/Core/data/" + p.getUniqueId() + ".yml");
	        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
	        try {
	            pData.set("BaseHP", pData.getDouble("BaseHP") + Constants.HPPerLevel);
	            pData.set("BaseMana", pData.getInt("BaseMana") + Constants.ManaPerLevel);
	            pData.save(pFile);
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	        int SPM = pData.getInt("SPM");
	        double manaUpgrade = Constants.ManaPerSP;
	        int newmana = pData.getInt("BaseMana") + (int) (SPM * (manaUpgrade * pData.getInt("BaseMana")));
			setIntValue(p, "Mana", newmana);
			getManaMap().replace(p.getUniqueId(), newmana);
		}
		int newlevel = times + level;
		if (times >= 1) {
			Main.msg(p, "");
			Main.msg(p, "&7» &e&lLEVEL UP: &6" + (newlevel - times) + " &f-> &6" + newlevel);
			Main.msg(p, "&7» &e&lSP INCREASE: &f+" + (Constants.SPPerLevel * times));
			Main.msg(p, "&7» &4&lAD INCREASE: &f+" + (Constants.ADPerLevel * times));
			Main.msg(p, "&7» &c&lHP INCREASE: &f+" + (Constants.HPPerLevel * times));
			Main.msg(p, "&7» &b&lMANA INCREASE: &f+" + (Constants.ManaPerLevel * times));
			Main.msg(p, "");
			p.sendTitle(new Title(Main.color("&e&lLEVEL UP!"), Main.color("&6" + (newlevel - times) + " &f-> &6" + newlevel), 5, 20, 5));
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
			Armor.updateSet(p);
			Weapons.updateInventory(p);
		}
	}
	
	public static void sendHp(Player p) {
		DecimalFormat dF = new DecimalFormat("#.##");
		double max = getInstance().getExpMax(p);
		double exp = getInstance().getExp(p);
		double exppercent = ((1.0 * exp) / (1.0 * max)) * 100;
		int mana = getInstance().getMana(p);
		//TextComponent bar = new TextComponent(color("&8---&r&8« &c" + dF.format(p.getHealth()) + " HP &8|| &b" + mana + " M &8|| &a" + dF.format(exppercent) + "% XP &8|| &e" + getInstance().level.get(p.getUniqueId()) + " LVL " + "&8»---"));
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
	public void setListValue(Player p, String text, List<String> list) {
		File pFile = new File("plugins/Core/data/" + p.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            pData.set(text, list);
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
	
	public static Object getObject(Player p, String text) {
		File pFile = new File("plugins/Core/data/" + p.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        return pData.get(text);
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
