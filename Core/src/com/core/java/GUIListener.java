package com.core.java;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListener implements Listener {

	@EventHandler
	public void helpClick (InventoryClickEvent e) {
		if (e.getInventory() != null) {
			if (e.getInventory().getTitle() != null) {
				if (e.getInventory().getTitle().contains("HELP MENU")) {
					if (e.getCurrentItem() != null) {
						if (e.getCurrentItem().getType() != null) {
							if (e.getCurrentItem().getType() == Material.LEATHER_HELMET) {
								e.getWhoClicked().closeInventory();
								e.getWhoClicked().openInventory(GUICommand.armorInv);
								return;
							}
							if (e.getCurrentItem().getType() == Material.DIAMOND_SWORD) {
								e.getWhoClicked().closeInventory();
								e.getWhoClicked().openInventory(GUICommand.combatInv);
								return;
							}
						}
					}
					e.setCancelled(true);
					return;
				}
			}
		}
		if (e.getClickedInventory() != null) {
			if (e.getClickedInventory().getTitle() != null) {
				if (e.getClickedInventory().getTitle().contains("HELP MENU")) {
					e.setCancelled(true);
					return;
				}
			}
		}
	}
	
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
	
	@EventHandler
	public void combatClick (InventoryClickEvent e) {
		if (e.getInventory() != null) {
			if (e.getInventory().getTitle() != null) {
				if (e.getInventory().getTitle().contains("COMBAT INFO")) {
					e.setCancelled(true);
					return;
				}
			}
		}
		if (e.getClickedInventory() != null) {
			if (e.getClickedInventory().getTitle() != null) {
				if (e.getClickedInventory().getTitle().contains("COMBAT INFO")) {
					e.setCancelled(true);
					return;
				}
			}
		}
	}
	
}
