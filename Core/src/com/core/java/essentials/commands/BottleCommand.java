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
		if (e.getView().getTitle().contains("&a&lEXP BOTTLER")) {
			if (e.getWhoClicked() instanceof Player) {
				Player p = (Player) e.getWhoClicked();
				if (e.getCurrentItem() instanceof ItemStack) {
					if (e.getCurrentItem().getType() == Material.GREEN_DYE) {
						int exp = main.getExp(p);
						int maxexp = main.getExpMax(p);
						double expperlevel = maxexp * 0.25;
						if (1 * expperlevel > exp) {
							Main.msg(p, "&cYou don't have enough RPG EXP! &8(&a" + 1 * expperlevel + "&8)");
							e.setCancelled(true);
							return;
						}
						int newexp = exp - (int) (expperlevel * 1);
						int amount = 0;
						for (ItemStack i : e.getClickedInventory()) {
							if (i != null && i instanceof ItemStack && i.getType() instanceof Material) {
								if (i.getType() == Material.EXPERIENCE_BOTTLE) {
									amount = i.getAmount();
								}
							}
						}
						if (amount > 64) {
							e.setCancelled(true);
							Main.msg(p, "&cPlease collect your bottles.");
							return;
						}
						main.setIntValue(p, "Exp", newexp);
						main.getExpMap().replace(p.getUniqueId(), newexp);
						DecimalFormat df = new DecimalFormat("#.##");
						Main.msg(p, "&aYou consumed &f" + df.format(expperlevel * 1) + " &aEXP for &f" + 1 + " &alevels.");
						p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
						sendInventory(p, true, amount);
					}
				}
			}
			if (e.getCurrentItem().getType() != Material.EXPERIENCE_BOTTLE) {
				e.setCancelled(true);
			}
		}
	}

	public void sendInventory(Player p, boolean bottle, int amount) {
		Inventory inv = Bukkit.createInventory(null, 9, Main.color("&a&lEXP BOTTLER"));
		ItemStack pane = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
		ItemMeta paneMeta = pane.getItemMeta();
		paneMeta.setDisplayName(Main.color(""));
		ArrayList<String> lore = new ArrayList<>();
		lore.add(Main.color(""));
		paneMeta.setLore(lore);
		pane.setItemMeta(paneMeta);
		inv.setItem(0, pane);
		inv.setItem(1, pane);
		inv.setItem(3, pane);
		inv.setItem(5, pane);
		inv.setItem(7, pane);
		inv.setItem(8, pane);
		ItemStack star = new ItemStack(Material.GREEN_DYE);
		ItemMeta starMeta = star.getItemMeta();
		starMeta.setDisplayName(Main.color("&aConvert Experience"));
		lore = new ArrayList<>();
		lore.add(Main.color(""));
		lore.add(Main.color("&fConvert some of your RPG EXP &8(&f/exp&8) &finto"));
		lore.add(Main.color("&fVanilla EXP Bottles!"));
		lore.add(Main.color("&fConversion: &8(&a25% RPG EXP &8: &a1 Level&8)"));
		lore.add(Main.color(""));
		starMeta.setLore(lore);
		star.setItemMeta(starMeta);
		inv.setItem(2, star);

		star = new ItemStack(Material.NETHER_STAR);
		starMeta = star.getItemMeta();
		starMeta.setDisplayName(Main.color("&aEXP Info"));
		lore = new ArrayList<>();
		lore.add(Main.color(""));
		lore.add(Main.color("&fCurrent Exp: &a" + main.getExp(p)));
		lore.add(Main.color("&fExp Percentage: &a" + (main.getExp(p) / main.getExpMax(p))));
		lore.add(Main.color(""));
		starMeta.setLore(lore);
		star.setItemMeta(starMeta);
		inv.setItem(6, star);

		if (bottle) {
			star = new ItemStack(Material.EXPERIENCE_BOTTLE);
			star.setAmount(amount);
			inv.setItem(4, star);
		}
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
						int exp = main.getExp(p);
						int maxexp = main.getExpMax(p);
						double expperlevel = maxexp * 0.25;
						if (lvl * expperlevel > exp) {
							Main.msg(p, "&cYou don't have enough RPG EXP! &8(&a" + lvl * expperlevel + "&8)");
							return false;
						}
						int newexp = exp - (int) (expperlevel * lvl);
						main.setIntValue(p, "Exp", newexp);
						main.getExpMap().replace(p.getUniqueId(), newexp);
						DecimalFormat df = new DecimalFormat("#.##");
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
