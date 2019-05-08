package com.core.java.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.core.java.essentials.Main;

public class ExpCommand implements CommandExecutor {
	
	private Main main = Main.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			Main.msg(p, "");
			Main.msg(p, "&8» &e&lLEVEL: &f" + main.getLevel(p) + " &8/&f 100");
			Main.msg(p, "&8» &e&lEXP: &f" + main.getExp(p) + " &8/&f " + main.getExpMax(p));
			Main.msg(p, "");
		} else {
			if (args.length == 1) {
				if (Bukkit.getPlayer(args[0]) instanceof Player) {
					Player target = (Player) Bukkit.getPlayer(args[0]);
					Main.so("&8» &e&lLEVEL: &f" + main.getLevel(target) + " &8/&f 100");
					Main.so("&8» &e&lEXP: &f" + main.getExp(target) + " &8/&f " + main.getExpMax(target));
				}
			} else {
				Main.so("Usage: /level <player>");
			}
		}
		return false;
	}
		
}
