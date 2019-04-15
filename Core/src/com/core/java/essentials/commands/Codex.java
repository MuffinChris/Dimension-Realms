package com.core.java.essentials.commands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Codex implements Listener {
	
	@EventHandler
	public void codex(PlayerInteractEvent e) {
		Player p = (Player) e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (p.getInventory().getItemInMainHand() != null) {
				ItemStack i = p.getInventory().getItemInMainHand();
				if (i.getType() != null) {
					if (i.getType() == Material.ENCHANTED_BOOK) {
						if (i.getItemMeta().getDisplayName().contains("Guide Codex")) {
							p.performCommand("help");
							//Add GUI help later
						}
					}
				}
			}
		}
	}

}
