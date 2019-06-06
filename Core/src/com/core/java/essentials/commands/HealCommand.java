package com.core.java.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.core.java.essentials.Main;

public class HealCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("core.mod")) {
				if (args.length == 0) {
					p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
					p.setFoodLevel(20);
					for (PotionEffect pot : p.getActivePotionEffects()) {
						p.removePotionEffect(pot.getType());
					}
					p.setFireTicks(0);
					p.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, p.getLocation(), 50, 1, 1, 1);
				} else if (args.length == 1) {
					if (Bukkit.getPlayer(args[0]) instanceof Player) {
						Player t = Bukkit.getPlayer(args[0]);
						t.setHealth(t.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
						t.setFoodLevel(20);
						for (PotionEffect pot : t.getActivePotionEffects()) {
							t.removePotionEffect(pot.getType());
						}
						t.setFireTicks(0);
						t.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, t.getLocation(), 50, 1, 1, 1);
					} else {
						Main.msg(p, "&cInvalid Target");
					}
				} else {
					Main.msg(p, "Usage: /heal <player>");
				}
			} else {
				Main.msg(p, Main.getInstance().noperm);
			}
		} else {
			if (args.length == 1) {
				if (Bukkit.getPlayer(args[0]) instanceof Player) {
					Player t = Bukkit.getPlayer(args[0]);
					t.setHealth(t.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
					t.setFoodLevel(20);
					for (PotionEffect pot : t.getActivePotionEffects()) {
						t.removePotionEffect(pot.getType());
					}
					t.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, t.getLocation(), 50, 1, 1, 1);
				} else {
					Main.so("&cInvalid Target");
				}
			} else {
				Main.so("Usage: /heal <player>");
			}
		}
		return false;
	}
	
}
