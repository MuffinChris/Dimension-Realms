package com.core.java;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RPGJoinFunctions implements Listener {

	@EventHandler
	public void onJoinRPG (PlayerJoinEvent e) {
		File pFile = new File("plugins/Core/data/" + e.getPlayer().getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            pData.set("Username", e.getPlayer().getName());
            if (!pData.isSet("Attack Speed")) {
            	pData.set("Attack Speed", 6.0);
            }
            if (!pData.isSet("Mana")) {
            	pData.set("Mana", 5000);
            }
            pData.save(pFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
		e.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(pData.getDouble("Attack Speed"));
		if (e.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() < 40.0) {
			e.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40.0);
			e.getPlayer().setHealth(40);
		}
		e.getPlayer().setHealthScale(20);
		
		if (!e.getPlayer().hasPlayedBefore()) {
			e.getPlayer().setHealth(40);
			ItemStack codex = new ItemStack(Material.ENCHANTED_BOOK);
			ItemMeta meta = codex.getItemMeta();
			meta.setDisplayName(Main.color("&8» &bGuide Codex &8«"));
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("");
			lore.add(Main.color("&7Right-Click for Help!"));
			lore.add("");
			meta.setLore(lore);
			codex.setItemMeta(meta);
			e.getPlayer().getInventory().setItem(8, codex);
			
			e.getPlayer().getInventory().setItem(0, new ItemStack(Material.WOODEN_SWORD));
			
			ItemStack bread = new ItemStack(Material.BREAD);
			bread.setAmount(8);
			e.getPlayer().getInventory().setItem(4, bread);
		}
		
	}
	
	@EventHandler
	public void respawnItems (PlayerRespawnEvent e) {
			Player p = (Player) e.getPlayer();
			ItemStack codex = new ItemStack(Material.ENCHANTED_BOOK);
			ItemMeta meta = codex.getItemMeta();
			meta.setDisplayName(Main.color("&8» &bGuide Codex &8«"));
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("");
			lore.add(Main.color("&7Right-Click for Help!"));
			lore.add("");
			meta.setLore(lore);
			codex.setItemMeta(meta);
			p.getInventory().setItem(8, codex);
			
			p.getInventory().setItem(0, new ItemStack(Material.WOODEN_SWORD));
			
			ItemStack bread = new ItemStack(Material.BREAD);
			bread.setAmount(8);
			p.getInventory().setItem(4, bread);
	}
	
}
