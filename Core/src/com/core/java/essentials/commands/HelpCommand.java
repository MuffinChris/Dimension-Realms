package com.core.java.essentials.commands;

import java.io.Console;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.core.java.essentials.Main;

public class HelpCommand implements CommandExecutor {
	
	public static Inventory helpInv = Bukkit.createInventory(null, 27, Main.color("&e&lHELP MENU"));
	public static void createHelpGui() {
		ItemStack armor = new ItemStack(Material.LEATHER_HELMET);
		ItemMeta armorMeta = armor.getItemMeta();
		armorMeta.setDisplayName(Main.color("&6&lARMOR SETS"));
		ArrayList<String> lore = new ArrayList<>();
		lore.add(Main.color(""));
		lore.add(Main.color("&7Click for information"));
		lore.add(Main.color("&7on the different armor sets!"));
		armorMeta.setLore(lore);
		armorMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		armor.setItemMeta(armorMeta);
		helpInv.setItem(12, armor);
		
		ItemStack combat = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta combatMeta = combat.getItemMeta();
		combatMeta.setDisplayName(Main.color("&4&lCOMBAT INFO"));
		lore.clear();
		lore.add(Main.color(""));
		lore.add(Main.color("&7Click for information"));
		lore.add(Main.color("&7on the combat system!"));
		combatMeta.setLore(lore);
		combatMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		combat.setItemMeta(combatMeta);
		helpInv.setItem(11, combat);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			player.openInventory(helpInv);
			/*
			Main.msg(player, "&f&m-----&r &eHelp Menu &f&m-----");
			Main.msg(player, "&e/help &7- &fBasic Help Command");
			Main.msg(player, "&e/armor &7- &fView Armor Types");
			Main.msg(player, "&e/msg &7- &fMessage a Player");
			Main.msg(player, "&e/lag &7- &fView RAM Usage");
			//Main.msg(player, "&e/msg &7- &fMessage other Users");
			 */
		} else {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bukkit:help");
		}
		return false;
	}

}
