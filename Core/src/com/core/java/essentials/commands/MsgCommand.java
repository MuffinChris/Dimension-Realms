package com.core.java.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.core.java.essentials.Main;

public class MsgCommand implements CommandExecutor {

	private Main main = Main.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (cmd.getLabel().equalsIgnoreCase("r") || cmd.getLabel().equalsIgnoreCase("reply")) {
				if (main.getpinf().getPinf(p).getReply() instanceof Player) {
					if (args.length == 0) {
						Main.msg(p, "Usage: /r <msg>");
					}
					if (args.length >= 1) {
						Player t = main.getpinf().getPinf(p).getReply();
						String output = "";
						for (String s : args) {
							output+=s;
							output+=" ";
						}
						Main.msg(p, "&7[&eYou &8\u00BB &e" + t.getName() + "&7] &f" + output);
						Main.msg(t, "&7[&e" + p.getName() + " &8\u00BB &eYou&7] &f" + output);
						main.getpinf().getPinf(p).setReply(t);
						main.getpinf().getPinf(t).setReply(p);
					}
				} else {
					Main.msg(p, "&cYou have no one to reply to.");
				}
			} else {
				if (args.length <= 1) {
					Main.msg(p, "Usage: /msg <player> <msg>");
				}
				if (args.length >= 2) {
					if (Bukkit.getPlayer(args[0]) instanceof Player) {
						Player t = Bukkit.getPlayer(args[0]);
						String output = "";
						int index = 0;
						for (String s : args) {
							if (index != 0) {
								output+=s;
								output+=" ";
							}
							index++;
						}
						Main.msg(p, "&7[&eYou &8\u00BB &e" + t.getName() + "&7] &f" + output);
						Main.msg(t, "&7[&e" + p.getName() + " &8\u00BB &eYou&7] &f" + output);
						main.getpinf().getPinf(p).setReply(t);
						main.getpinf().getPinf(t).setReply(p);
					} else {
						Main.msg(p, "&cInvalid Player.");
					}
				}
			}
		} else {
			if (args.length <= 1) {
				Main.so("Usage: /msg <player> <msg>");
			}
			if (args.length >= 2) {
				if (Bukkit.getPlayer(args[0]) instanceof Player) {
					Player t = Bukkit.getPlayer(args[0]);
					String output = "";
					int index = 0;
					for (String s : args) {
						if (index != 0) {
							output+=s;
							output+=" ";
						}
						index++;
					}
					Main.so("&7[&eYou &8\u00BB &e" + t.getName() + "&7] &f" + output);
					Main.msg(t, "&7[&eCONSOLE" + " &8\u00BB &eYou&7] &f" + output);
				} else {
					Main.so("&cInvalid Player.");
				}
			}
		}
		return false;
	}
	
}
