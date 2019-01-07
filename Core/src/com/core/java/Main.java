package com.core.java;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Main extends JavaPlugin {

	public final double basehp = 100;
	public final double leatherA = 0;
	public final double goldenA = 25;
	public final double chainmailA = 50;
	public final double ironA = 75;
	public final double diamondA = 100;
	
	public final String version = "0.0.1";
	public final String noperm = "&cNo permission!";

	public static Main getInstance() {
		return JavaPlugin.getPlugin(Main.class);
	}
	
	public Map<UUID, Integer> mana = new HashMap<UUID, Integer>();
	public Map<UUID, Integer> getManaMap() {
		return mana;
	}
	
	public Map<UUID, Integer> manaRegen = new HashMap<UUID, Integer>();
	public Map<UUID, Integer> getManaRegenMap() {
		return manaRegen;
	}
	
	public int getManaRegen (Player p) {
		return Integer.valueOf(String.valueOf(Math.round((Math.sqrt((mana.get(p.getUniqueId()) * 0.0005D))))));
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
		so("&cCORE&7: &fCommands Enabled!");
		
		Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
		Bukkit.getPluginManager().registerEvents(new Motd(), this);
		Bukkit.getPluginManager().registerEvents(new ChatFunctions(), this);
		Bukkit.getPluginManager().registerEvents(new RPGFunctions(), this);
		Bukkit.getPluginManager().registerEvents(new Armor(), this);
		Bukkit.getPluginManager().registerEvents(new EXP(), this);
		Bukkit.getPluginManager().registerEvents(new Codex(), this);
		so("&cCORE&7: &fListeners Enabled!");
		
		GUICommand.createArmorInv();
		HelpCommand.createHelpGui();
		so("&cCORE&7: &fGUIs Enabled!");
		
		manaRegen();
		hpPeriodic();
		updatePeriodic();
		so("&cCORE&7: &fPeriodics Enabled!");
	}
	
	@Override
	public void onDisable() {
		
		updateFunc();
		so("&cCORE&7: &fCore Plugin Version &c" + version + " &fDisabled!");
		
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
	
	public void hashmapUpdate(Player p) {
		UUID uuid = p.getUniqueId();
        mana.replace(uuid, Integer.valueOf(getValue(p, "Mana")));
        manaRegen.replace(uuid, getManaRegen(p));
	}
	
	public static void sendHp(Player p) {
		DecimalFormat dF = new DecimalFormat("#.##");
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(color("&8---&r&8« &c" + dF.format(p.getHealth()) + " HP &8|| &b" + getInstance().manaRegen.get(p.getUniqueId()) + " MR " + "&8»---")));
	}
	
	public static void setStringValue(Player p, String text, String value) {
		File pFile = new File("plugins/Core/data/" + p.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            pData.set(text, value);
            pData.save(pFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
	}
	public static void setDoubleValue(Player p, String text, double value) {
		File pFile = new File("plugins/Core/data/" + p.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            pData.set(text, value);
            pData.save(pFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
	}
	public static void setIntValue(Player p, String text, int value) {
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
		System.out.println(ChatColor.translateAlternateColorCodes('&', text));
	}
	
	public static String color(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}
	
}
