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
import com.core.java.rpgbase.player.Armor;

public class SkilltreeListener implements Listener {

	private Main main = Main.getInstance();
	
	public double getAdUpgrade(Player p) {
		return 0.5;
	}
	
	public int getManaUpgrade(Player p) {
		return 500;
	}
	
	public int getManaRegenUpgrade(Player p) {
		return 1;
	}
	
	public double getHPUpgrade(Player p) {
		return 5;
	}
	
	public void sendPlayerInv(Player p) {
		Inventory playerInv = Bukkit.createInventory(null, 27, Main.color("&9&lPLAYER UPGRADES"));
		ArrayList<String> lore = new ArrayList<>();
		
		ItemStack sp = new ItemStack(Material.NETHER_STAR);
		ItemMeta spMeta = sp.getItemMeta();
		spMeta.setDisplayName(Main.color("&aRemaining SP: &f" + main.getSPMap().get(p.getUniqueId())));
		sp.setItemMeta(spMeta);
		
		playerInv.setItem(13, sp);
		
		int SPAD = Integer.valueOf(Main.getValue(p, "SPAD"));
		int SPHP = Integer.valueOf(Main.getValue(p, "SPHP"));
		int SPM = Integer.valueOf(Main.getValue(p, "SPM"));
		int SPMR = Integer.valueOf(Main.getValue(p, "SPMR"));
		
		double ad = SPAD * getAdUpgrade(p);
		if (ad >= 100) {
			ItemStack sword = new ItemStack(Material.IRON_SWORD);
			ItemMeta swordMeta = sword.getItemMeta();
			swordMeta.setDisplayName(Main.color("&cAttack Damage Upgrade"));
			lore = new ArrayList<>();
			lore.add(Main.color(""));
			lore.add(Main.color("&fCurrent &8(&f" + SPAD + " SP&8) &f: &4" + ad));
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
			lore.add(Main.color("&fCurrent &8(&f" + SPAD + " SP&8) &f: &4" + ad));
			lore.add(Main.color("&fUpgrade &8(&f1 SP&8)&f: &4" + getAdUpgrade(p)));
			lore.add(Main.color(""));
			swordMeta.setLore(lore);
			swordMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			sword.setItemMeta(swordMeta);
			playerInv.setItem(11, sword);
		}
		
		double hp = SPHP * getHPUpgrade(p);
		if (hp >= 500) {
			ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE);
			ItemMeta chestMeta = chest.getItemMeta();
			chestMeta.setDisplayName(Main.color("&cHealth Bonus Upgrade"));
			lore = new ArrayList<>();
			lore.add(Main.color(""));
			lore.add(Main.color("&fCurrent &8(&f" + SPHP + " SP&8) &f: &c" + hp));
			lore.add(Main.color("&fHP Bonus Maxed Out!"));
			lore.add(Main.color(""));
			chestMeta.setLore(lore);
			chestMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			chest.setItemMeta(chestMeta);
			playerInv.setItem(12, chest);
		} else {
			ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE);
			ItemMeta chestMeta = chest.getItemMeta();
			chestMeta.setDisplayName(Main.color("&cHealth Bonus Upgrade"));
			lore = new ArrayList<>();
			lore.add(Main.color(""));
			lore.add(Main.color("&fCurrent &8(&f" + SPHP + " SP&8) &f: &c" + hp));
			lore.add(Main.color("&fUpgrade &8(&f1 SP&8)&f: &c" + getHPUpgrade(p)));
			lore.add(Main.color(""));
			chestMeta.setLore(lore);
			chestMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			chest.setItemMeta(chestMeta);
			playerInv.setItem(12, chest);
		}
		
		int maxmana = SPM * getManaUpgrade(p);
		if (maxmana >= 100000) {
			ItemStack hs = new ItemStack(Material.HEART_OF_THE_SEA);
			ItemMeta hsMeta = hs.getItemMeta();
			hsMeta.setDisplayName(Main.color("&9Max Mana Upgrade"));
			lore = new ArrayList<>();
			lore.add(Main.color(""));
			lore.add(Main.color("&fCurrent &8(&f" + SPM + " SP&8) &f: &9" + maxmana));
			lore.add(Main.color("&fMana Maxed Out!"));
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
			lore.add(Main.color("&fCurrent &8(&f" + SPM + " SP&8) &f: &9" + maxmana));
			lore.add(Main.color("&fUpgrade &8(&f1 SP&8)&f: &9" + getManaUpgrade(p)));
			lore.add(Main.color(""));
			hsMeta.setLore(lore);
			hs.setItemMeta(hsMeta);
			playerInv.setItem(14, hs);
		}
		
		int manaregen = SPMR * getManaRegenUpgrade(p);
		if (manaregen * 20 >= 2000) {
			ItemStack hs = new ItemStack(Material.LIGHT_BLUE_DYE);
			ItemMeta hsMeta = hs.getItemMeta();
			hsMeta.setDisplayName(Main.color("&bMana Regen Upgrade"));
			lore = new ArrayList<>();
			lore.add(Main.color(""));
			lore.add(Main.color("&fCurrent &8(&f" + SPMR + " SP&8) &f: &b" + manaregen * 20 + "/s"));
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
			lore.add(Main.color("&fCurrent &8(&f" + SPMR + " SP&8) &f: &b" + manaregen * 20 + "/s"));
			lore.add(Main.color("&fUpgrade &8(&f1 SP&8)&f: &b" + getManaRegenUpgrade(p) * 20));
			lore.add(Main.color(""));
			hsMeta.setLore(lore);
			hs.setItemMeta(hsMeta);
			playerInv.setItem(15, hs);
		}
		p.openInventory(playerInv);
	}
	
	public static Inventory spInv = Bukkit.createInventory(null, 27, Main.color("&9&lSKILLPOINTS MENU"));
	
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
							if (e.getWhoClicked() instanceof Player) {
								Player p = (Player) e.getWhoClicked();
								int SPAD = Integer.valueOf(Main.getValue(p, "SPAD"));
								int SPHP = Integer.valueOf(Main.getValue(p, "SPHP"));
								int SPM = Integer.valueOf(Main.getValue(p, "SPM"));
								int SPMR = Integer.valueOf(Main.getValue(p, "SPMR"));
								if (e.getCurrentItem().getType() == Material.IRON_SWORD) {
									int sp = main.getSPMap().get(p.getUniqueId());
									if (sp >= 1) {
										if (getAdUpgrade(p) * SPAD < 100) {
											SPAD++;
											main.setIntValue(p, "SPAD", SPAD);
											
											double newad = SPAD * getAdUpgrade(p) + 1;
											main.setDoubleValue(p, "AD", newad);
											
											int newsp = sp - 1;
											main.setIntValue(p, "SP", newsp);
											
										    main.getAdMap().replace(p.getUniqueId(), newad);
										    main.getSPMap().replace(p.getUniqueId(), newsp);
										    
										    p.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(newad);
											
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
								if (e.getCurrentItem().getType() == Material.DIAMOND_CHESTPLATE) {
									int sp = main.getSPMap().get(p.getUniqueId());
									if (sp >= 1) {
										if (getHPUpgrade(p) * SPHP < 500) {
											SPHP++;
											main.setIntValue(p, "SPHP", SPHP);
											
											double newhp = SPHP * getHPUpgrade(p);
											main.setDoubleValue(p, "HP", newhp);
											
											int newsp = sp - 1;
											main.setIntValue(p, "SP", newsp);
										    main.getSPMap().replace(p.getUniqueId(), newsp);
											
										    Main.msg(p, "&aUpgrade Successful: &f+" + getHPUpgrade(p) + " HP");
										    Main.msg(p, "&aRemaining SP: &f" + newsp);
											if (e.getWhoClicked() instanceof Player) {
												sendPlayerInv((Player) e.getWhoClicked());
											}
											Armor.updateSet(p);
										} else {
											Main.msg(p, "&cThis upgrade is maxed out!");
										}
									} else {
										Main.msg(p, "&cNot enough Skillpoints!");
									}
							}
							if (e.getCurrentItem().getType() == Material.HEART_OF_THE_SEA) {
									int sp = main.getSPMap().get(p.getUniqueId());
									if (sp >= 1) {
										if (getManaUpgrade(p) * SPM < 500000) {
											SPM++;
											main.setIntValue(p, "SPM", SPM);
											int newmana = SPM * getManaUpgrade(p) + 5000;
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
							if (e.getCurrentItem().getType() == Material.LIGHT_BLUE_DYE) {
									int sp = main.getSPMap().get(p.getUniqueId());
									if (sp >= 1) {
										if (getManaRegenUpgrade(p) * SPMR * 20 < 5000) {
											SPMR++;
											main.setIntValue(p, "SPMR", SPMR);
											int newmanar = SPMR * getManaRegenUpgrade(p) + 1;
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
