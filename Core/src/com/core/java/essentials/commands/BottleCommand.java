package com.core.java.essentials.commands;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.core.java.essentials.Main;

public class BottleCommand implements CommandExecutor {
	
	private Main main = Main.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length == 0) {
				Main.msg(p, "Usage: /bottle <levels> || This command will consume some of your RPG Experience and give you xp bottles. &8(&a10% RPG EXP &8: &a1 Level&8)");
			}
			if (args.length >= 1) {
				if (Integer.valueOf(args[0]) instanceof Integer) {
					int lvl = Integer.valueOf(args[0]);
					if (lvl > 0 && lvl <= 64) {
						int exp = main.getExp(p);
						int maxexp = main.getExpMax(p);
						double expperlevel = maxexp * 0.1;
						if (lvl * expperlevel > exp) {
							Main.msg(p, "&cYou don't have enough RPG EXP!");
							return false;
						}
						int newexp = exp - (int) (expperlevel * lvl);
						main.setIntValue(p, "Exp", newexp);
						main.getExpMap().replace(p.getUniqueId(), newexp);
						Main.msg(p, "&aYou consumed &f" + expperlevel * lvl + " &aEXP for &f" + lvl + " &alevels.");
						p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
						ItemStack bottles = new ItemStack(Material.EXPERIENCE_BOTTLE);
						bottles.setAmount(lvl);
						p.getInventory().addItem(bottles);
					} else {
						Main.msg(p, "&cError: please submit a positive integer value &f<1-64>&c.");
					}
 				} else {
					Main.msg(p, "&cError: please submit a positive integer value &f<1-64>&c.");
				}
			}
		} else {
			Main.so("Command cannot be used from console.");
		}
		return false;
	}
}
