package com.core.java;

import java.io.Console;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Main.msg(player, "&f&m-----&r &eHelp Menu &f&m-----");
			Main.msg(player, "&e/help &7- &fBasic Help Command");
			Main.msg(player, "&e/armor &7- &fView Armor Types");
			Main.msg(player, "&e/msg &7- &fMessage a Player");
			Main.msg(player, "&e/lag &7- &fView RAM Usage");
			//Main.msg(player, "&e/msg &7- &fMessage other Users");
		} else {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bukkit:help");
		}
		return false;
	}

}
