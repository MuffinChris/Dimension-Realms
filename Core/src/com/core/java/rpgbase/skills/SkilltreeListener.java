package com.core.java.rpgbase.skills;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.core.java.essentials.Main;
import com.core.java.essentials.commands.GUICommand;

public class SkilltreeListener implements Listener {

	private Main main = Main.getInstance();
	
	// Mana Gain linear. Fix check for mana regen (20* for the max check)
	// Add multiple at a time with rightclick, and make the inventory not reset (resets mouse pos, instead update item)
	
	public double getAdUpgrade(Player p) {
		return 0.5;
	}
	
	public int getManaUpgrade(Player p) {
		return 500;
	}
	
	public int getManaRegenUpgrade(Player p) {
		return 1;
	}
	
	public void sendPlayerInv(Player p) {
		Inventory playerInv = Bukkit.createInventory(null, 27, Main.color("&a&lPLAYER UPGRADES"));
		ArrayList<String> lore = new ArrayList<>();
		
		ItemStack sp = new ItemStack(Material.NETHER_STAR);
		ItemMeta spMeta = sp.getItemMeta();
		spMeta.setDisplayName(Main.color("&aRemaining SP: &f" + main.getSPMap().get(p.getUniqueId())));
		sp.setItemMeta(spMeta);
		
		playerInv.setItem(13, sp);
		
		double ad = main.getAdMap().get(p.getUniqueId());
		if (ad >= 100) {
			ItemStack sword = new ItemStack(Material.IRON_SWORD);
			ItemMeta swordMeta = sword.getItemMeta();
			swordMeta.setDisplayName(Main.color("&cAttack Damage Upgrade"));
			lore = new ArrayList<>();
			lore.add(Main.color(""));
			lore.add(Main.color("&fCurrent: &4" + ad));
			lore.add(Main.color("&fAttack Damage Maxed Out!"));
			lore.add(Main.color(""));
			swordMeta.setLore(lore);
			swordMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			sword.setItemMeta(swordMeta);
			playerInv.setItem(11, sword);
		} else {
			ItemStack sword = new ItemStack(Material.IRON_SWORD);
			ItemMeta swordMeta = sword.getItemMeta();
			swordMeta.setDisplayName(Main.color("&cAttack Damage Upgrade"));
			lore = new ArrayList<>();
			lore.add(Main.color(""));
			lore.add(Main.color("&fCurrent: &4" + ad) );
			lore.add(Main.color("&fUpgrade &8(&f1 SP&8)&f: &4" + getAdUpgrade(p)));
			lore.add(Main.color(""));
			swordMeta.setLore(lore);
			swordMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			sword.setItemMeta(swordMeta);
			playerInv.setItem(11, sword);
		}
		
		int maxmana = main.getManaMap().get(p.getUniqueId());
		if (maxmana >= 100000) {
			ItemStack hs = new ItemStack(Material.HEART_OF_THE_SEA);
			ItemMeta hsMeta = hs.getItemMeta();
			hsMeta.setDisplayName(Main.color("&9Max Mana Upgrade"));
			lore = new ArrayList<>();
			lore.add(Main.color(""));
			lore.add(Main.color("&fCurrent: &9" + maxmana));
			lore.add(Main.color("&fMax Mana Maxed Out!"));
			lore.add(Main.color(""));
			hsMeta.setLore(lore);
			hs.setItemMeta(hsMeta);
			playerInv.setItem(14, hs);
		} else {
			ItemStack hs = new ItemStack(Material.HEART_OF_THE_SEA);
			ItemMeta hsMeta = hs.getItemMeta();
			hsMeta.setDisplayName(Main.color("&9Max Mana Upgrade"));
			lore = new ArrayList<>();
			lore.add(Main.color(""));
			lore.add(Main.color("&fCurrent: &9" + maxmana));
			lore.add(Main.color("&fUpgrade &8(&f1 SP&8)&f: &9" + getManaUpgrade(p)));
			lore.add(Main.color(""));
			hsMeta.setLore(lore);
			hs.setItemMeta(hsMeta);
			playerInv.setItem(14, hs);
		}
		
		int manaregen = main.getManaRegenMap().get(p.getUniqueId());
		if (manaregen * 20 >= 2000) {
			ItemStack hs = new ItemStack(Material.LIGHT_BLUE_DYE);
			ItemMeta hsMeta = hs.getItemMeta();
			hsMeta.setDisplayName(Main.color("&bMana Regen Upgrade"));
			lore = new ArrayList<>();
			lore.add(Main.color(""));
			lore.add(Main.color("&fCurrent: &b" + (manaregen * 20)) + "/s");
			lore.add(Main.color("&fMana Regen Maxed Out!"));
			lore.add(Main.color(""));
			hsMeta.setLore(lore);
			hs.setItemMeta(hsMeta);
			playerInv.setItem(15, hs);
		} else {
			ItemStack hs = new ItemStack(Material.LIGHT_BLUE_DYE);
			ItemMeta hsMeta = hs.getItemMeta();
			hsMeta.setDisplayName(Main.color("&bMana Regen Upgrade"));
			lore = new ArrayList<>();
			lore.add(Main.color(""));
			lore.add(Main.color("&fCurrent: &b" + (manaregen * 20)) + "/s");
			lore.add(Main.color("&fUpgrade &8(&f1 SP&8)&f: &b" + getManaRegenUpgrade(p) * 20));
			lore.add(Main.color(""));
			hsMeta.setLore(lore);
			hs.setItemMeta(hsMeta);
			playerInv.setItem(15, hs);
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
		lore.add(Main.color(""));
		starMeta.setLore(lore);
		star.setItemMeta(starMeta);
		spInv.setItem(11, star);
		
		ItemStack as = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta asMeta = as.getItemMeta();
		asMeta.setDisplayName(Main.color("&cAssassin Skill Tree"));
		lore = new ArrayList<>();
		lore.add(Main.color(""));
		lore.add(Main.color("&fUnlock the skills of an elusive rogue."));
		lore.add(Main.color(""));
		asMeta.setLore(lore);
		asMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
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
										double ad = main.getAdMap().get(p.getUniqueId());
										if (ad < 100) {
											double newad = main.getAdMap().get(p.getUniqueId()) + getAdUpgrade(p);
											main.setDoubleValue(p, "AD", newad);
											
											int newsp = sp - 1;
											main.setIntValue(p, "SP", newsp);
											
										    main.getAdMap().replace(p.getUniqueId(), newad);
										    main.getSPMap().replace(p.getUniqueId(), newsp);
											
										    Main.msg(p, "&aUpgrade Successful: &f+" + getAdUpgrade(p) + " AD");
										    Main.msg(p, "&aRemaining SP: &f" + newsp);
											if (e.getWhoClicked() instanceof Player) {
												sendPlayerInv((Player) e.getWhoClicked());
											}
										} else {
											Main.msg(p, "&cThis upgrade is maxed out!");
										}
									} else {
										Main.msg(p, "&cNot enough Skillpoints!");
									}
								}
							}
							if (e.getCurrentItem().getType() == Material.HEART_OF_THE_SEA) {
								if (e.getWhoClicked() instanceof Player) {
									Player p = (Player) e.getWhoClicked();
									int sp = main.getSPMap().get(p.getUniqueId());
									if (sp >= 1) {
										int mana = main.getManaMap().get(p.getUniqueId());
										if (mana < 500000) {
											int newmana = main.getManaMap().get(p.getUniqueId()) + getManaUpgrade(p);
											main.setIntValue(p, "Mana", newmana);
											
											int newsp = sp - 1;
											main.setIntValue(p, "SP", newsp);
											
										    main.getManaMap().replace(p.getUniqueId(), newmana);
										    main.getSPMap().replace(p.getUniqueId(), newsp);
											
										    Main.msg(p, "&aUpgrade Successful: &f+" + getManaUpgrade(p) + " Max Mana");
										    Main.msg(p, "&aRemaining SP: &f" + newsp);
											if (e.getWhoClicked() instanceof Player) {
												sendPlayerInv((Player) e.getWhoClicked());
											}
										} else {
											Main.msg(p, "&cThis upgrade is maxed out!");
										}
									} else {
										Main.msg(p, "&cNot enough Skillpoints!");
									}
								}
							}
							if (e.getCurrentItem().getType() == Material.LIGHT_BLUE_DYE) {
								if (e.getWhoClicked() instanceof Player) {
									Player p = (Player) e.getWhoClicked();
									int sp = main.getSPMap().get(p.getUniqueId());
									if (sp >= 1) {
										int manar = main.getManaRegenMap().get(p.getUniqueId());
										if (manar * 20 < 2000) {
											int newmanar = main.getManaRegenMap().get(p.getUniqueId()) + getManaRegenUpgrade(p);
											main.setIntValue(p, "ManaRegen", newmanar);
											
											int newsp = sp - 1;
											main.setIntValue(p, "SP", newsp);
											
										    main.getManaRegenMap().replace(p.getUniqueId(), newmanar);
										    main.getSPMap().replace(p.getUniqueId(), newsp);
											
										    Main.msg(p, "&aUpgrade Successful: &f+" + (getManaRegenUpgrade(p) * 20) + " Mana Regen");
										    Main.msg(p, "&aRemaining SP: &f" + newsp);
											if (e.getWhoClicked() instanceof Player) {
												sendPlayerInv((Player) e.getWhoClicked());
											}
										} else {
											Main.msg(p, "&cThis upgrade is maxed out!");
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
