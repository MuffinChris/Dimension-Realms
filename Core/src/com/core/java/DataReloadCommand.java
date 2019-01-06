package com.core.java;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DataReloadCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("core.admin")) {
				Main.getInstance().updateFunc();
				Main.msg(p, "&e&lRELOADED DATA FOLDER");
			} else {
				Main.msg(p, Main.getInstance().noperm);
			}
		} else {
			Main.getInstance().updateFunc();
		}
		return false;
	}

}
