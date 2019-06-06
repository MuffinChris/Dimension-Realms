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
					
					for (ItemStack item : p.getInventory().getContents()) {
						if (item != null && item.getType() != null) {
							if (item.getType() == Material.GOLD_INGOT) {
								balance+=item.getAmount();
							} else if (item.getType() == Material.GOLD_BLOCK) {
								balance+=item.getAmount() * 9;
							}
						}
					}
					
					Main.msg(p, "&8« &e&lYour Balance: &f" + balance);
				} else {
					if (Bukkit.getPlayer(args[0]) instanceof Player) {
						Player target = Bukkit.getPlayer(args[0]);
						int balance = 0;
						
						for (ItemStack item : target.getInventory().getContents()) {
							if (item != null && item.getType() != null) {
								if (item.getType() == Material.GOLD_INGOT) {
									balance+=item.getAmount();
								} else if (item.getType() == Material.GOLD_BLOCK) {
									balance+=item.getAmount() * 9;
								}
							}
						}
						
						Main.msg(p, "&8« &e&l" + target.getName() + "'s Balance: &f" + balance);
					} else {
						Main.msg(p, "&cInvalid Player.");
					}
				}
			} else {
				if (!(args.length == 1)) {
					Main.so("&fUsage: /money <player>");
				}
				if (Bukkit.getPlayer(args[0]) instanceof Player) {
					int balance = 0;
					Player target = Bukkit.getPlayer(args[0]);
					for (ItemStack i : target.getInventory().getContents()) {
						if (i.getType() != null) {
							if (i.getType() == Material.GOLD_INGOT) {
								balance++;
							} else if (i.getType() == Material.GOLD_BLOCK) {
								balance+=9;
							}
						}
					}
					
					Main.so("&8« &e&l" + target.getName() + "'s Balance: &f" + balance);
				} else {
					Main.so("&cInvalid Player.");
				}
			}
		}
		return false;
	}
	
}
