package com.core.java.essentials.commands;

import java.io.File;
import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.core.java.essentials.Main;

public class KdrCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length == 0) {
				File pFile = new File("plugins/Core/data/" + p.getUniqueId() + ".yml");
			    FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
			    int kills = pData.getInt("Kills");
			    int deaths = pData.getInt("Deaths");
			    double kdr = (1.0 * kills) / (1.0 * deaths);
				if (deaths == 0) {
					kdr = 0.0;
				}
			    DecimalFormat df = new DecimalFormat("#.##");
			    Main.msg(p, "&8» &c&lKDR: &f" + df.format(kdr) + " &8(&c" + kills + "&8/&c" + deaths + "&8)");
			} else if (args.length == 1) {
				if (Bukkit.getPlayer(args[0]) instanceof Player) {
					Player t = Bukkit.getPlayer(args[0]);
					File pFile = new File("plugins/Core/data/" + t.getUniqueId() + ".yml");
				    FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
				    int kills = pData.getInt("Kills");
				    int deaths = pData.getInt("Deaths");
				    double kdr = (1.0 * kills) / (1.0 * deaths);
					if (deaths == 0) {
						kdr = 0.0;
					}
				    DecimalFormat df = new DecimalFormat("#.##");
				    Main.msg(p, "&8» &c&l" + t.getName() + "'s KDR: &f" + df.format(kdr) + " &8(&c" + kills + "&8/&c" + deaths + "&8)");
				} else {
					Main.msg(p, "&cInvalid Player");
				}
			} else {
				Main.msg(p, "Usage: /stats <player>");
			}
		} else {
			if (args.length == 1) {
				if (Bukkit.getPlayer(args[0]) instanceof Player) {
					Player t = Bukkit.getPlayer(args[0]);
					File pFile = new File("plugins/Core/data/" + t.getUniqueId() + ".yml");
				    FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
				    int kills = pData.getInt("Kills");
				    int deaths = pData.getInt("Deaths");
				    double kdr = (1.0 * kills) / (1.0 * deaths);
					if (deaths == 0) {
						kdr = 0.0;
					}
				    DecimalFormat df = new DecimalFormat("#.##");
				    Main.so("&8» &c&l" + t.getName() + "'s KDR: &f" + df.format(kdr) + " &8(&c" + kills + "&8/&c" + deaths + "&8)");
				} else {
					Main.so("&cInvalid Player");
				}
			} else {
				Main.so("Usage: /stats <player>");
			}
		}
		return false;
	}
	
}
