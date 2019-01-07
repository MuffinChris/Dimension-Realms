package com.core.java;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HashmapCommand implements CommandExecutor {

	private Main plugin = Main.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("core.admin")) {
				if (args.length <= 3) {
					Main.msg(p, "&fUsage: /set <key> <value> <vartype> <player>");
				} else {
					if (args.length == 4) {
						if (Bukkit.getPlayer(args[3]) instanceof Player) {
							Player target = Bukkit.getPlayer(args[3]);
							if (args[2].toLowerCase().equals("double")) {
								Main.setDoubleValue(target, args[0], Double.valueOf(args[1]));
							} else if (args[2].toLowerCase().equals("string")) {
								Main.setStringValue(target, args[0], args[1]);
							} else if (args[2].toLowerCase().equals("int")) {
								Main.setIntValue(target, args[0], Integer.valueOf(args[1]));
							} else {
								Main.msg(p, "&cInvalid Vartype, valid types are double, string, int.");
								return false;
							}
							Main.msg(p, "&aSet Key " + args[0] + " to Value " + args[1] + " of type " + args[2] + " for Player " + target.getName());
						} else {
							Main.msg(p, "&cInvalid Target.");
						}
					} else {
						Main.msg(p, "&fUsage: /set <key> <value> <vartype> <player>");
					}
				}
			} else {
				Main.msg(p, plugin.noperm);
			}
		} else {
			if (args.length <= 3) {
				Main.so("&fUsage: /set <key> <value> <vartype> <player>");
			} else {
				if (args.length == 4) {
					if (Bukkit.getPlayer(args[3]) instanceof Player) {
						Player target = Bukkit.getPlayer(args[3]);
						if (args[2].toLowerCase().equals("double")) {
							Main.setDoubleValue(target, args[0], Double.valueOf(args[1]));
						} else if (args[2].toLowerCase().equals("string")) {
							Main.setStringValue(target, args[0], args[1]);
						} else if (args[2].toLowerCase().equals("int")) {
							Main.setIntValue(target, args[0], Integer.valueOf(args[1]));
						} else {
							Main.so("&cInvalid Vartype, valid types are double, string, int.");
							return false;
						}
						Main.so("&aSet Key " + args[0] + " to Value " + args[1] + " of type " + args[2] + " for Player " + target.getName());
					} else {
						Main.so("&cInvalid Target.");
					}
				} else {
					Main.so("&fUsage: /set <key> <value> <vartype> <player>");
				}
			}
		}
		return false;
	}

}
