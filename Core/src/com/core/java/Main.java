package com.core.java;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

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

	public String version = "0.0.1";
	public String noperm = "&cNo permission!";
	
	private static Main instance;
	public static Main getInstance() {
		return instance;
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
		
		Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
		Bukkit.getPluginManager().registerEvents(new Motd(), this);
		Bukkit.getPluginManager().registerEvents(new ChatFunctions(), this);
		Bukkit.getPluginManager().registerEvents(new RPGJoinFunctions(), this);
		Bukkit.getPluginManager().registerEvents(new Armor(), this);
		Bukkit.getPluginManager().registerEvents(new EXP(), this);
		Bukkit.getPluginManager().registerEvents(new Codex(), this);
		//Bukkit.getPluginManager().registerEvents(new Stuff(), this);
		
		GUICommand.createArmorInv();
		
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					// Make this based on Mana in config. Use hashmap instead of shit loads of files (cause lag)
					if (p.getLevel() < 5000) {
						if (!p.isDead()) {
							if (p.isSleeping()) {
								p.setLevel(p.getLevel() + 20);
							} else {
								p.setLevel(p.getLevel() + 1);
							}
						} else {
							p.setLevel(0);
						}
					}
				}
			}
		}.runTaskTimerAsynchronously(this, 20L, 1L);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					sendHp(p);
				}
			}
		}.runTaskTimer(this, 5L, 5L);
	}
	
	@Override
	public void onDisable() {
		
		so("&cCORE&7: &fCore Plugin Version &c" + version + " &fDisabled!");
		
	}
	
	public static void sendHp(Player p) {
		DecimalFormat dF = new DecimalFormat("#.##");
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(color("&8---&r&8« &c" + dF.format(p.getHealth()) + " HP &8»---")));
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
