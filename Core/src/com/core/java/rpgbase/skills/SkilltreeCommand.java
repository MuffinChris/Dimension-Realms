package com.core.java.rpgbase.skills;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.core.java.essentials.Main;

public class SkilltreeCommand implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			p.openInventory(SkilltreeListener.spInv);
		} else {
			Main.so("&cNon-Console Command");
		}
		return false;
		
	}
}
