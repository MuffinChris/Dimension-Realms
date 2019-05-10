package com.core.java.rpgbase.skills;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.core.java.essentials.Main;
import com.core.java.essentials.commands.GUICommand;

public class SkilltreeListener implements Listener {

	private Main main = Main.getInstance();
	
	public Inventory playerInv = Bukkit.createInventory(null, 27, Main.color("&a&lPLAYER UPGRADES"));
	
	public double getAdUpgrade(Player p) {
		return 0.25;
	}
	
	public void sendPlayerInv(Player p) {
		ArrayList<String> lore = new ArrayList<>();
		double ad = main.getAdMap().get(p.getUniqueId());
		if (ad >= 50) {
			ItemStack sword = new ItemStack(Material.IRON_SWORD);
			ItemMeta swordMeta = sword.getItemMeta();
			swordMeta.setDisplayName(Main.color("&cAttack Damage Upgrade"));
			lore = new ArrayList<>();
			lore.add(Main.color(""));
			lore.add(Main.color("&fCurrent Increase: &4" + ad));
			lore.add(Main.color("&fAttack Damage Maxed Out!"));
			swordMeta.setLore(lore);
			sword.setItemMeta(swordMeta);
			playerInv.setItem(11, sword);
		} else {
			ItemStack sword = new ItemStack(Material.IRON_SWORD);
			ItemMeta swordMeta = sword.getItemMeta();
			swordMeta.setDisplayName(Main.color("&cAttack Damage Upgrade"));
			lore = new ArrayList<>();
			lore.add(Main.color(""));
			lore.add(Main.color("&fCurrent Increase: &4" + ad) );
			lore.add(Main.color("&fUpgrade &8(&f1 SP&8)&f: &4" + getAdUpgrade(p)));
			swordMeta.setLore(lore);
			sword.setItemMeta(swordMeta);
			playerInv.setItem(11, sword);
		}
		
		p.openInventory(playerInv);
	}
	
	public static Inventory spInv = Bukkit.createInventory(null, 27, Main.color("&a&lSKILLPOINTS MENU"));
	
	public static void createSpInv() {
		ItemStack star = new ItemStack(Material.NETHER_STAR);
		ItemMeta starMeta = star.getItemMeta();
		starMeta.setDisplayName(Main.color("&aPlayer Upgrades"));
		ArrayList<String> lore = new ArrayList<>();
		lore.add(Main.color(""));
		lore.add(Main.color("&fUpgrade your basic stats."));
		starMeta.setLore(lore);
		star.setItemMeta(starMeta);
		spInv.setItem(11, star);
		
		ItemStack as = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta asMeta = as.getItemMeta();
		asMeta.setDisplayName(Main.color("&cAssassin Skill Tree"));
		lore = new ArrayList<>();
		lore.add(Main.color(""));
		lore.add(Main.color("&fUnlock the skills of an elusive rogue."));
		asMeta.setLore(lore);
		as.setItemMeta(asMeta);
		spInv.setItem(12, as);
	}
	
	@EventHandler
	public void playerUpgradeClick (InventoryClickEvent e) {
		if (e.getInventory() != null) {
			if (e.getView().getTitle() != null) {
				if (e.getView().getTitle().contains("PLAYER UPGRADES")) {
					if (e.getCurrentItem() != null) {
						if (e.getCurrentItem().getType() != null) {
							if (e.getCurrentItem().getType() == Material.IRON_SWORD) {
								if (e.getWhoClicked() instanceof Player) {
									Player p = (Player) e.getWhoClicked();
									int sp = main.getSPMap().get(p.getUniqueId());
									if (sp >= 1) {
										double newad = main.getAdMap().get(p.getUniqueId()) + getAdUpgrade(p);
										main.setDoubleValue(p, "AD", newad);
										
										int newsp = sp - 1;
										main.setIntValue(p, "SP", newsp);
										
									    main.getAdMap().replace(p.getUniqueId(), newad);
									    main.getSPMap().replace(p.getUniqueId(), newsp);
										
									    Main.msg(p, "&aUpgrade Successful: &f+" + getAdUpgrade(p) + " AD");
									    Main.msg(p, "&aRemaining SP: &f" + newsp);
									    e.getWhoClicked().closeInventory();
										if (e.getWhoClicked() instanceof Player) {
											((Player) e.getWhoClicked()).openInventory(playerInv);
										}
									} else {
										Main.msg(p, "&cNot enough Skillpoints!");
									}
								}
							}
						}
					}
					e.setCancelled(true);
					return;
				}
			}
		}
		if (e.getClickedInventory() != null) {
			if (e.getView().getTitle() != null) {
				if (e.getView().getTitle().contains("PLAYER UPGRADES")) {
					e.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void spClick (InventoryClickEvent e) {
		if (e.getInventory() != null) {
			if (e.getView().getTitle() != null) {
				if (e.getView().getTitle().contains("SKILLPOINTS MENU")) {
					if (e.getCurrentItem() != null) {
						if (e.getCurrentItem().getType() != null) {
							if (e.getCurrentItem().getType() == Material.NETHER_STAR) {
								e.getWhoClicked().closeInventory();
								if (e.getWhoClicked() instanceof Player) {
									sendPlayerInv((Player) e.getWhoClicked());
								}
								return;
							}
							if (e.getCurrentItem().getType() == Material.DIAMOND_SWORD) {
								e.getWhoClicked().closeInventory();
								//e.getWhoClicked().openInventory(asInv);
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
			if (e.getView().getTitle() != null) {
				if (e.getView().getTitle().contains("SKILLPOINTS MENU")) {
					e.setCancelled(true);
					return;
				}
			}
		}
	}
	
}
