package com.core.java.essentials.commands;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.core.java.essentials.Main;

public class SpawnCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("spawn")) {
				File locFile = new File("plugins/Core/locations/spawn.yml");
	            FileConfiguration locData = YamlConfiguration.loadConfiguration(locFile);
	            p.teleport((Location) locData.get("Spawn"));
	            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
			}
			if (cmd.getName().equalsIgnoreCase("setspawn")) {
				if (p.hasPermission("core.admin")) {
					File locFile = new File("plugins/Core/locations/spawn.yml");
		            FileConfiguration locData = YamlConfiguration.loadConfiguration(locFile);
		            try {
		               locData.set("Spawn", p.getLocation());
		               locData.save(locFile);
		            } catch (IOException exception) {
		               exception.printStackTrace();
		            }
		            Main.msg(p, "&aSet Spawn Location!");
				} else {
					Main.msg(p, Main.getInstance().noperm);
				}
			}
		} else {
			Main.so("&cThis is not a Console command!");
		}
		return false;
	}

}
