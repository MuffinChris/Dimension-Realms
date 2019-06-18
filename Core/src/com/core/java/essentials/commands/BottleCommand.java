package com.core.java.essentials.commands;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.core.java.essentials.Main;

public class BottleCommand implements CommandExecutor, Listener {
	
	private Main main = Main.getInstance();

	@EventHandler
	public void onClick (InventoryClickEvent e) {
		if (e.getView().getTitle().contains("EXP BOTTLER")) {
			if (e.getWhoClicked() instanceof Player) {
				Player p = (Player) e.getWhoClicked();
				if (e.getCurrentItem() != null) {
					if (e.getCurrentItem().getType() == Material.LIME_DYE) {
						double exp = main.getExp(p);
						double maxexp = main.getExpMax(p);
						double expperlevel = maxexp * 0.1;
						DecimalFormat df = new DecimalFormat("#.##");
						if (1 * expperlevel > exp) {
							Main.msg(p, "&cYou don't have enough RPG EXP! &8(&e" + df.format(1 * expperlevel) + "&8)");
							e.setCancelled(true);
							return;
						}
						double newexp = exp - (expperlevel * 1);
						int amount = 0;
						for (ItemStack i : e.getClickedInventory()) {
							if (i != null && i instanceof ItemStack && i.getType() instanceof Material) {
								if (i.getType() == Material.EXPERIENCE_BOTTLE) {
									amount = i.getAmount();
								}
							}
						}
						amount++;
						if (amount > 64) {
							e.setCancelled(true);
							Main.msg(p, "&cPlease collect your bottles.");
							return;
						}
						main.setDoubleValue(p, "Exp", newexp);
						main.getExpMap().replace(p.getUniqueId(), newexp);
						Main.msg(p, "&aYou consumed &f" + df.format(expperlevel * 1) + " &aEXP for &f" + 1 + " &alevels.");
						p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
						sendInventory(p, true, amount);
					}
				}
			}
			if (e.getCurrentItem() != null && e.getCurrentItem().getType() != null) {
				if (e.getCurrentItem().getType() != Material.EXPERIENCE_BOTTLE) {
					e.setCancelled(true);
				}
			}
		}
	}

	public void sendInventory(Player p, boolean bottle, int amount) {
		Inventory inv = Bukkit.createInventory(null, 9, Main.color("&a&lEXP BOTTLER"));
		ItemStack pane = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
		ItemMeta paneMeta = pane.getItemMeta();
		paneMeta.setDisplayName(Main.color(" "));
		ArrayList<String> lore = new ArrayList<>();
		lore.add(Main.color(""));
		paneMeta.setLore(lore);
		pane.setItemMeta(paneMeta);
		inv.setItem(0, pane);
		inv.setItem(1, pane);
		inv.setItem(2, pane);
		inv.setItem(3, pane);
		inv.setItem(7, pane);
		inv.setItem(8, pane);
		ItemStack star = new ItemStack(Material.LIME_DYE);
		ItemMeta starMeta = star.getItemMeta();
		starMeta.setDisplayName(Main.color("&a&lConvert Experience"));
		lore = new ArrayList<>();
		lore.add(Main.color(""));
		lore.add(Main.color("&8» &fConvert some of your RPG EXP &8(&f/exp&8)"));
		lore.add(Main.color("&8» &finto Vanilla EXP Bottles!"));
		lore.add(Main.color("&8» &fConversion: &8(&a10% RPG EXP &8: &a1 Level&8)"));
		lore.add(Main.color(""));
		starMeta.setLore(lore);
		star.setItemMeta(starMeta);
		inv.setItem(6, star);

		star = new ItemStack(Material.NETHER_STAR);
		starMeta = star.getItemMeta();
		starMeta.setDisplayName(Main.color("&e&lEXP Info"));
		lore = new ArrayList<>();
		lore.add(Main.color(""));
		DecimalFormat df = new DecimalFormat("#");
		lore.add(Main.color("&8» &fCurrent Exp: &e" + df.format(main.getExp(p))));
		DecimalFormat dF = new DecimalFormat("#.##");
		lore.add(Main.color("&8» &fExp Percentage: &e" + dF.format(100F * main.getExp(p) / main.getExpMax(p)) + "%"));
		lore.add(Main.color(""));
		starMeta.setLore(lore);
		star.setItemMeta(starMeta);
		inv.setItem(5, star);

		if (bottle) {
			star = new ItemStack(Material.EXPERIENCE_BOTTLE);
			star.setAmount(amount);
			inv.setItem(4, star);
		}

		p.openInventory(inv);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length == 0) {
				sendInventory(p, false, 0);
			}
			if (args.length >= 1) {
				if (Integer.valueOf(args[0]) instanceof Integer) {
					int lvl = Integer.valueOf(args[0]);
					if (lvl > 0 && lvl <= 64) {
						double exp = main.getExp(p);
						double maxexp = main.getExpMax(p);
						double expperlevel = maxexp * 0.1;
						DecimalFormat df = new DecimalFormat("#.##");
						if (lvl * expperlevel > exp) {
							Main.msg(p, "&cYou don't have enough RPG EXP! &8(&e" + df.format(lvl * expperlevel) + "&8)");
							return false;
						}
						double newexp = exp - (expperlevel * lvl);
						main.setDoubleValue(p, "Exp", newexp);
						main.getExpMap().replace(p.getUniqueId(), newexp);
						Main.msg(p, "&aYou consumed &f" + df.format(expperlevel * lvl) + " &aEXP for &f" + lvl + " &alevels.");
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
