package com.core.java.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.core.java.essentials.Main;

public class DataReloadCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("core.admin")) {
				Main.getInstance().updateFunc();
				Main.msg(p, "");
				Main.msg(p, "&8» &e&lRELOADED DATA FOLDER");
				Main.msg(p, "");
			} else {
				Main.msg(p, Main.getInstance().noperm);
			}
		} else {
			Main.getInstance().updateFunc();
		}
		return false;
	}

}
