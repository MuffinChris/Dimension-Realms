package com.core.java.economy;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.core.java.essentials.Main;

public class EconCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getLabel().equalsIgnoreCase("money") || cmd.getLabel().equalsIgnoreCase("balance") || cmd.getLabel().equalsIgnoreCase("bal")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length == 0) {
					int balance = 0;
					
					for (ItemStack i : p.getInventory()) {
						if (i.getType() != null) {
							if (i.getType() == Material.GOLD_INGOT) {
								balance++;
							} else if (i.getType() == Material.GOLD_BLOCK) {
								balance+=9;
							}
						}
					}
					
					Main.msg(p, "&eYour Balance: &f" + balance);
				} else {
					if (Bukkit.getPlayer(args[1]) instanceof Player) {
						Player target = Bukkit.getPlayer(args[1]);
						int balance = 0;
						
						for (ItemStack i : target.getInventory()) {
							if (i.getType() != null) {
								if (i.getType() == Material.GOLD_INGOT) {
									balance++;
								} else if (i.getType() == Material.GOLD_BLOCK) {
									balance+=9;
								}
							}
						}
						
						Main.msg(p, "&e" + target.getName() + "'s Balance: &f" + balance);
					} else {
						Main.msg(p, "&cInvalid Player.");
					}
				}
			} else {
				if (!(args.length == 1)) {
					Main.so("&fUsage: /money <player>");
				}
				if (Bukkit.getPlayer(args[1]) instanceof Player) {
					int balance = 0;
					Player target = Bukkit.getPlayer(args[1]);
					for (ItemStack i : target.getInventory()) {
						if (i.getType() != null) {
							if (i.getType() == Material.GOLD_INGOT) {
								balance++;
							} else if (i.getType() == Material.GOLD_BLOCK) {
								balance+=9;
							}
						}
					}
					
					Main.so("&e" + target.getName() + "'s Balance: &f" + balance);
				} else {
					Main.so("&cInvalid Player.");
				}
			}
		}
		return false;
	}
	
}
