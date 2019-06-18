package com.core.java.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.core.java.essentials.Main;

public class ManaCommand implements CommandExecutor {

	private Main main = Main.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			Main.msg(p, "&8» &b&lMana: &f" + main.getMana(p) + " &8/&f " + main.getManaMap().get(p.getUniqueId()));
		} else {
			Main.so("Console doesn't have mana.");
		}
		return false;
	}
	
}
