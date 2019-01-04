package com.core.java;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListener implements Listener {

	@EventHandler
	public void armorClick (InventoryClickEvent e) {
		if (e.getInventory() != null) {
			if (e.getInventory().getTitle() != null) {
				if (e.getInventory().getTitle().contains("ARMOR SETS")) {
					e.setCancelled(true);
					return;
				}
			}
		}
		if (e.getClickedInventory() != null) {
			if (e.getClickedInventory().getTitle() != null) {
				if (e.getClickedInventory().getTitle().contains("ARMOR SETS")) {
					e.setCancelled(true);
					return;
				}
			}
		}
	}
	
}
