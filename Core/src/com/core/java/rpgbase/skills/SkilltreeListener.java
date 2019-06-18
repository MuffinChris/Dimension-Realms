package com.core.java.rpgbase.skills;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.core.java.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
		return Constants.ADPerSP;
	}
	
	public double getManaUpgrade(Player p) {
		return Constants.ManaPerSP;
	}
	
	public int getManaRegenUpgrade(Player p) {
		return Constants.ManaRegenPerSP;
	}
	
	public double getHPUpgrade(Player p) {
		return Constants.HPPerSP;
	}
	
	public void sendAssassinInv(Player p) {
		Inventory playerInv = Bukkit.createInventory(null, 54, Main.color("&aAssassin Skill Tree"));
		ArrayList<String> lore = new ArrayList<>();
		
		ItemStack sp = new ItemStack(Material.NETHER_STAR);
		ItemMeta spMeta = sp.getItemMeta();
		spMeta.setDisplayName(Main.color("&aRemaining SP: &f" + main.getSPMap().get(p.getUniqueId())));
		sp.setItemMeta(spMeta);
		
		playerInv.setItem(8, sp);
		
		if (Main.getObject(p, "Skills") instanceof List<?>) {
			List<String> skills = (List<String>) (Main.getObject(p, "Skills"));
			ItemStack mobi = new ItemStack(Material.LEATHER_BOOTS);
			ItemMeta mobiMeta = mobi.getItemMeta();
			mobiMeta.setDisplayName(Main.color("&aMobility Skills"));
			mobiMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			mobi.setItemMeta(mobiMeta);
			playerInv.setItem(0, mobi);
			if (skills.toString().contains("Leap")) {
				ItemStack leap = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
				ItemMeta leapMeta = leap.getItemMeta();
				leapMeta.setDisplayName(Main.color("&aLeap"));
				lore.clear();
				lore.add(Main.color("&aUNLOCKED"));
				lore.add(Main.color("&bMana Cost: &f500"));
				lore.add(Main.color("&fLeap into the air!"));
				leapMeta.setLore(lore);
				leap.setItemMeta(leapMeta);
				playerInv.setItem(9, leap);
			} else {
				ItemStack leap = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
				ItemMeta leapMeta = leap.getItemMeta();
				leapMeta.setDisplayName(Main.color("&cLeap"));
				lore.clear();
				lore.add(Main.color("&cLOCKED &8(&cCOST: &f5 SP&8)"));
				lore.add(Main.color("&bMana Cost: &f500"));
				lore.add(Main.color("&fLeap into the air!"));
				leapMeta.setLore(lore);
				leap.setItemMeta(leapMeta);
				playerInv.setItem(9, leap);
			}
			p.openInventory(playerInv);
		}
	}
	
	public void sendPlayerInv(Player p) {
		DecimalFormat dF = new DecimalFormat("#.##");
		Inventory playerInv = Bukkit.createInventory(null, 27, Main.color("&9&lPLAYER UPGRADES"));
		ArrayList<String> lore;
		
		ItemStack sp = new ItemStack(Material.NETHER_STAR);
		ItemMeta spMeta = sp.getItemMeta();
		spMeta.setDisplayName(Main.color("&aRemaining SP: &f" + main.getSPMap().get(p.getUniqueId())));
		sp.setItemMeta(spMeta);
		
		playerInv.setItem(13, sp);
		
		int SPAD = Integer.valueOf(Main.getValue(p, "SPAD"));
		int SPHP = Integer.valueOf(Main.getValue(p, "SPHP"));
		int SPM = Integer.valueOf(Main.getValue(p, "SPM"));
		int SPMR = Integer.valueOf(Main.getValue(p, "SPMR"));
		
		double ad = SPAD * getAdUpgrade(p) * 100;
		ad = Double.valueOf(dF.format(ad));
		//if (ad >= 100) {
			ItemStack sword = new ItemStack(Material.IRON_SWORD);
			ItemMeta swordMeta = sword.getItemMeta();
			swordMeta.setDisplayName(Main.color("&cAttack Damage Upgrade"));
			lore = new ArrayList<>();
			lore.add(Main.color(""));
			lore.add(Main.color("&fCurrent SP: &e" + main.getSPMap().get(p.getUniqueId())));
			lore.add(Main.color("&fCurrent &8(&f" + SPAD + " SP&8) &f: &4" + ad + "%"));
			lore.add(Main.color("&fUpgrade &8(&f1 SP&8)&f: &4" + getAdUpgrade(p) * 100 + "%"));
			lore.add(Main.color("&fRight-Click to apply 5 points."));
			lore.add(Main.color(""));
			swordMeta.setLore(lore);
			swordMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			sword.setItemMeta(swordMeta);
			playerInv.setItem(11, sword);
			/* else {
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
		}*/
		
		double hp = SPHP * getHPUpgrade(p) * 100;
		hp = Double.valueOf(dF.format(hp));
		/*if (hp >= 500) {
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
		} else {*/
			ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE);
			ItemMeta chestMeta = chest.getItemMeta();
			chestMeta.setDisplayName(Main.color("&cHealth Bonus Upgrade"));
			lore = new ArrayList<>();
			lore.add(Main.color(""));
		lore.add(Main.color("&fCurrent SP: &e" + main.getSPMap().get(p.getUniqueId())));
			lore.add(Main.color("&fCurrent &8(&f" + SPHP + " SP&8) &f: &c" + hp + "%"));
			lore.add(Main.color("&fUpgrade &8(&f1 SP&8)&f: &c" + getHPUpgrade(p) * 100 + "%"));
			lore.add(Main.color("&fRight-Click to apply 5 points."));
			lore.add(Main.color(""));
			chestMeta.setLore(lore);
			chestMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			chest.setItemMeta(chestMeta);
			playerInv.setItem(12, chest);
		//}
		
		double maxmana = SPM * getManaUpgrade(p) * 100;
		maxmana = Double.valueOf(dF.format(maxmana));
		/*if (maxmana >= 100000) {
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
		} else {*/
			ItemStack hs = new ItemStack(Material.HEART_OF_THE_SEA);
			ItemMeta hsMeta = hs.getItemMeta();
			hsMeta.setDisplayName(Main.color("&9Max Mana Upgrade"));
			lore = new ArrayList<>();
			lore.add(Main.color(""));
		lore.add(Main.color("&fCurrent SP: &e" + main.getSPMap().get(p.getUniqueId())));
			lore.add(Main.color("&fCurrent &8(&f" + SPM + " SP&8) &f: &9" + maxmana + "%"));
			lore.add(Main.color("&fUpgrade &8(&f1 SP&8)&f: &9" + getManaUpgrade(p) * 100 + "%"));
			lore.add(Main.color("&fRight-Click to apply 5 points."));
			lore.add(Main.color(""));
			hsMeta.setLore(lore);
			hs.setItemMeta(hsMeta);
			playerInv.setItem(14, hs);
		//}
		
		double manaregen = SPMR * getManaRegenUpgrade(p);
		/*if (manaregen * 20 >= 2000) {
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
		} else {*/
			ItemStack mr = new ItemStack(Material.LIGHT_BLUE_DYE);
			ItemMeta mrMeta = mr.getItemMeta();
			mrMeta.setDisplayName(Main.color("&bMana Regen Upgrade"));
			lore = new ArrayList<>();
			lore.add(Main.color(""));
		lore.add(Main.color("&fCurrent SP: &e" + main.getSPMap().get(p.getUniqueId())));
			lore.add(Main.color("&fCurrent &8(&f" + SPMR + " SP&8) &f: &b" + manaregen));
			lore.add(Main.color("&fUpgrade &8(&f1 SP&8)&f: &b" + getManaRegenUpgrade(p)));
			lore.add(Main.color("&fRight-Click to apply 5 points."));
			lore.add(Main.color(""));
			mrMeta.setLore(lore);
			mr.setItemMeta(mrMeta);
			playerInv.setItem(15, mr);
		//}

		ItemStack reset = new ItemStack(Material.BARRIER);
		ItemMeta resetMeta = reset.getItemMeta();
		resetMeta.setDisplayName(Main.color("&cReset Skillpoints"));
		lore = new ArrayList<>();
		lore.add(Main.color(""));
		lore.add(Main.color("&fReset your current point allocations."));
		lore.add(Main.color("&4WARNING: &fThere is no going back!"));
		lore.add(Main.color(""));
		resetMeta.setLore(lore);
		reset.setItemMeta(resetMeta);
		playerInv.setItem(22, reset);

		ItemStack glass = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
		ItemMeta glassMeta = glass.getItemMeta();
		glassMeta.setDisplayName(Main.color(" "));
		glass.setItemMeta(glassMeta);
		for (int i = 0; i < 27; i++) {
			if (playerInv.getContents()[i] == null) {
				playerInv.setItem(i, glass);
			}
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
				if (e.getView().getTitle().contains("Assassin Skill Tree")) {
					if (e.getCurrentItem() != null) {
						if (e.getCurrentItem().getType() != null) {
							if (e.getWhoClicked() instanceof Player) {
								Player p = (Player) e.getWhoClicked();
								if (Main.getObject(p, "Skills") instanceof List<?>) {
									List<String> skills = (List<String>) (Main.getObject(p, "Skills"));
									if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Leap")) {
										if (skills.contains("Leap")) {
											Main.msg(p, "&cSkill already unlocked!");
										} else {
											int sp = main.getSPMap().get(p.getUniqueId());
											if (sp >= 5) {
												int newsp = sp - 5;
												main.setIntValue(p, "SP", newsp);
											    main.getSPMap().replace(p.getUniqueId(), newsp);
											    skills.add("Leap");
											    main.setListValue(p, "Skills", skills);
											    if (e.getWhoClicked() instanceof Player) {
													sendAssassinInv((Player) e.getWhoClicked());
												}
											} else {
												Main.msg(p, "&cNot enough skillpoints!");
											}
										}
									}
								}
							}
						}
					}
					e.setCancelled(true);
					return;
				}
				if (e.getView().getTitle().contains("PLAYER UPGRADES")) {
					if (e.getCurrentItem() != null) {
						if (e.getCurrentItem().getType() != null) {
							if (e.getWhoClicked() instanceof Player) {
								Player p = (Player) e.getWhoClicked();
								int SPAD = Integer.valueOf(Main.getValue(p, "SPAD"));
								int SPHP = Integer.valueOf(Main.getValue(p, "SPHP"));
								int SPM = Integer.valueOf(Main.getValue(p, "SPM"));
								int SPMR = Integer.valueOf(Main.getValue(p, "SPMR"));
								if (e.getClick().isLeftClick()) {
									if (e.getCurrentItem().getType() == Material.BARRIER) {
										main.resetSP(p);
										Main.msg(p, "&8» &e&lYou have reset your skillpoint allocations.");
									}
									if (e.getCurrentItem().getType() == Material.IRON_SWORD) {
										int sp = main.getSPMap().get(p.getUniqueId());
										if (sp >= 1) {
											//if (getAdUpgrade(p) * SPAD < 100) {
											SPAD++;
											main.setIntValue(p, "SPAD", SPAD);

											double newad = SPAD * getAdUpgrade(p);
											main.setDoubleValue(p, "AD", newad);

											int newsp = sp - 1;
											main.setIntValue(p, "SP", newsp);

											main.getAdMap().replace(p.getUniqueId(), newad);
											main.getSPMap().replace(p.getUniqueId(), newsp);

											//p.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(newad);

											Main.msg(p, "&aUpgrade Successful: &f+" + getAdUpgrade(p) * 100 + "% AD");
											Main.msg(p, "&aRemaining SP: &f" + newsp);
											if (e.getWhoClicked() instanceof Player) {
												sendPlayerInv((Player) e.getWhoClicked());
											}
											/*} else {
												Main.msg(p, "&cThis upgrade is maxed out!");
											}*/
										} else {
											Main.msg(p, "&cNot enough Skillpoints!");
										}
									}
									if (e.getCurrentItem().getType() == Material.DIAMOND_CHESTPLATE) {
										int sp = main.getSPMap().get(p.getUniqueId());
										if (sp >= 1) {
											//if (getHPUpgrade(p) * SPHP < 500) {
											SPHP++;
											main.setIntValue(p, "SPHP", SPHP);

											double newhp = SPHP * getHPUpgrade(p);
											main.setDoubleValue(p, "HP", newhp);

											int newsp = sp - 1;
											main.setIntValue(p, "SP", newsp);
											main.getSPMap().replace(p.getUniqueId(), newsp);

											Main.msg(p, "&aUpgrade Successful: &f+" + getHPUpgrade(p) * 100 + "% HP");
											Main.msg(p, "&aRemaining SP: &f" + newsp);
											if (e.getWhoClicked() instanceof Player) {
												sendPlayerInv((Player) e.getWhoClicked());
											}
											Armor.updateSet(p);
											/*} else {
												Main.msg(p, "&cThis upgrade is maxed out!");
											}*/
										} else {
											Main.msg(p, "&cNot enough Skillpoints!");
										}
									}
									if (e.getCurrentItem().getType() == Material.HEART_OF_THE_SEA) {
										int sp = main.getSPMap().get(p.getUniqueId());
										if (sp >= 1) {
											//if (getManaUpgrade(p) * SPM < 500000) {
											SPM++;
											main.setIntValue(p, "SPM", SPM);
											File pFile = new File("plugins/Core/data/" + p.getUniqueId() + ".yml");
											FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);

											int newmana = pData.getInt("BaseMana") + (int) (SPM * (getManaUpgrade(p) * pData.getInt("BaseMana")));
											main.setIntValue(p, "Mana", newmana);

											int newsp = sp - 1;
											main.setIntValue(p, "SP", newsp);

											main.getManaMap().replace(p.getUniqueId(), newmana);
											main.getSPMap().replace(p.getUniqueId(), newsp);

											Main.msg(p, "&aUpgrade Successful: &f+" + getManaUpgrade(p) * 100 + "% Max Mana");
											Main.msg(p, "&aRemaining SP: &f" + newsp);
											if (e.getWhoClicked() instanceof Player) {
												sendPlayerInv((Player) e.getWhoClicked());
											}
											/*} else {
												Main.msg(p, "&cThis upgrade is maxed out!");
											}*/
										} else {
											Main.msg(p, "&cNot enough Skillpoints!");
										}
									}
									if (e.getCurrentItem().getType() == Material.LIGHT_BLUE_DYE) {
										int sp = main.getSPMap().get(p.getUniqueId());
										if (sp >= 1) {
											//if (getManaRegenUpgrade(p) * SPMR * 20 < 5000) {
											SPMR++;
											// BROKEN AS OF UPDATE
											main.setIntValue(p, "SPMR", SPMR);
											int newmanar = (SPMR * getManaRegenUpgrade(p) + Constants.BaseManaRegen);
											main.setIntValue(p, "ManaRegen", newmanar);

											int newsp = sp - 1;
											main.setIntValue(p, "SP", newsp);

											main.getManaRegenMap().replace(p.getUniqueId(), newmanar);
											main.getSPMap().replace(p.getUniqueId(), newsp);

											Main.msg(p, "&aUpgrade Successful: &f+" + (getManaRegenUpgrade(p)) + " Mana Regen");
											Main.msg(p, "&aRemaining SP: &f" + newsp);
											if (e.getWhoClicked() instanceof Player) {
												sendPlayerInv((Player) e.getWhoClicked());
											}
											/*} else {
												Main.msg(p, "&cThis upgrade is maxed out!");
											}*/
										} else {
											Main.msg(p, "&cNot enough Skillpoints!");
										}
									}
								}
								if (e.getClick().isRightClick()) {
									if (e.getCurrentItem().getType() == Material.IRON_SWORD) {
										int sp = main.getSPMap().get(p.getUniqueId());
										if (sp >= 5) {
											//if (getAdUpgrade(p) * SPAD < 100) {
											SPAD+=5;
											main.setIntValue(p, "SPAD", SPAD);

											double newad = SPAD * getAdUpgrade(p);
											main.setDoubleValue(p, "AD", newad);

											int newsp = sp - 5;
											main.setIntValue(p, "SP", newsp);

											main.getAdMap().replace(p.getUniqueId(), newad);
											main.getSPMap().replace(p.getUniqueId(), newsp);

											//p.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(newad);

											Main.msg(p, "&aUpgrade Successful: &f+" + getAdUpgrade(p) * 100 + "% AD");
											Main.msg(p, "&aRemaining SP: &f" + newsp);
											if (e.getWhoClicked() instanceof Player) {
												sendPlayerInv((Player) e.getWhoClicked());
											}
											/*} else {
												Main.msg(p, "&cThis upgrade is maxed out!");
											}*/
										} else {
											Main.msg(p, "&cNot enough Skillpoints!");
										}
									}
									if (e.getCurrentItem().getType() == Material.DIAMOND_CHESTPLATE) {
										int sp = main.getSPMap().get(p.getUniqueId());
										if (sp >= 5) {
											//if (getHPUpgrade(p) * SPHP < 500) {
											SPHP+=5;
											main.setIntValue(p, "SPHP", SPHP);

											double newhp = SPHP * getHPUpgrade(p);
											main.setDoubleValue(p, "HP", newhp);

											int newsp = sp - 5;
											main.setIntValue(p, "SP", newsp);
											main.getSPMap().replace(p.getUniqueId(), newsp);

											Main.msg(p, "&aUpgrade Successful: &f+" + getHPUpgrade(p) * 100 + "% HP");
											Main.msg(p, "&aRemaining SP: &f" + newsp);
											if (e.getWhoClicked() instanceof Player) {
												sendPlayerInv((Player) e.getWhoClicked());
											}
											Armor.updateSet(p);
											/*} else {
												Main.msg(p, "&cThis upgrade is maxed out!");
											}*/
										} else {
											Main.msg(p, "&cNot enough Skillpoints!");
										}
									}
									if (e.getCurrentItem().getType() == Material.HEART_OF_THE_SEA) {
										int sp = main.getSPMap().get(p.getUniqueId());
										if (sp >= 5) {
											//if (getManaUpgrade(p) * SPM < 500000) {
											SPM+=5;
											main.setIntValue(p, "SPM", SPM);
											File pFile = new File("plugins/Core/data/" + p.getUniqueId() + ".yml");
											FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);

											int newmana = pData.getInt("BaseMana") + (int) (SPM * (getManaUpgrade(p) * pData.getInt("BaseMana")));
											main.setIntValue(p, "Mana", newmana);

											int newsp = sp - 5;
											main.setIntValue(p, "SP", newsp);

											main.getManaMap().replace(p.getUniqueId(), newmana);
											main.getSPMap().replace(p.getUniqueId(), newsp);

											Main.msg(p, "&aUpgrade Successful: &f+" + getManaUpgrade(p) * 100 + "% Max Mana");
											Main.msg(p, "&aRemaining SP: &f" + newsp);
											if (e.getWhoClicked() instanceof Player) {
												sendPlayerInv((Player) e.getWhoClicked());
											}
											/*} else {
												Main.msg(p, "&cThis upgrade is maxed out!");
											}*/
										} else {
											Main.msg(p, "&cNot enough Skillpoints!");
										}
									}
									if (e.getCurrentItem().getType() == Material.LIGHT_BLUE_DYE) {
										int sp = main.getSPMap().get(p.getUniqueId());
										if (sp >= 5) {
											//if (getManaRegenUpgrade(p) * SPMR * 20 < 5000) {
											SPMR+=5;
											main.setIntValue(p, "SPMR", SPMR);
											int newmanar = (SPMR * getManaRegenUpgrade(p) + Constants.BaseManaRegen);
											main.setIntValue(p, "ManaRegen", newmanar);

											int newsp = sp - 5;
											main.setIntValue(p, "SP", newsp);

											main.getManaRegenMap().replace(p.getUniqueId(), newmanar);
											main.getSPMap().replace(p.getUniqueId(), newsp);

											Main.msg(p, "&aUpgrade Successful: &f+" + (getManaRegenUpgrade(p)) + " Mana Regen");
											Main.msg(p, "&aRemaining SP: &f" + newsp);
											if (e.getWhoClicked() instanceof Player) {
												sendPlayerInv((Player) e.getWhoClicked());
											}
											/*} else {
												Main.msg(p, "&cThis upgrade is maxed out!");
											}*/
										} else {
											Main.msg(p, "&cNot enough Skillpoints!");
										}
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
								if (e.getWhoClicked() instanceof Player) {
									sendAssassinInv((Player) e.getWhoClicked());
								}
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
		if (e.getClickedInventory() != null) {
			if (e.getView().getTitle() != null) {
				if (e.getView().getTitle().contains("ASSASSIN")) {
					e.setCancelled(true);
					return;
				}
			}
		}
	}
	
}
