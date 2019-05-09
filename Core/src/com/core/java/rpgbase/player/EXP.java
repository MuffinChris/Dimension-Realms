package com.core.java.rpgbase.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import com.core.java.essentials.Main;

public class EXP implements Listener {

	private Main plugin = Main.getInstance();
	
	public Map<UUID, ArrayList<Enchantment>> enchants = new HashMap<UUID, ArrayList<Enchantment>>();
	public Map<UUID, ArrayList<Integer>> enchantsC = new HashMap<UUID, ArrayList<Integer>>();
	
	@EventHandler
	public void onJoin (PlayerJoinEvent e) {
		enchants.put(e.getPlayer().getUniqueId(), null);
		enchantsC.put(e.getPlayer().getUniqueId(), null);
	}
	
	@EventHandler
	public void enchTable (PrepareItemEnchantEvent e) {
		Player p = e.getEnchanter();
		if (enchants.get(p.getUniqueId()) != null) {
			enchants.remove(p.getUniqueId());
		}
		if (enchantsC.get(p.getUniqueId()) != null) {
			enchantsC.remove(p.getUniqueId());
		}
		ArrayList<Enchantment> enchs = new ArrayList<>();
		ArrayList<Integer> enchsCost = new ArrayList<>();
		for (int i = 0; i < e.getOffers().length; i++) {
			if (e.getOffers()[i] != null) {
				e.getOffers()[i].setCost(10000 * (i + 1));
				enchs.add(e.getOffers()[i].getEnchantment());
				enchsCost.add(e.getOffers()[i].getEnchantmentLevel());
			}
		}
		enchants.put(p.getUniqueId(), enchs);
		enchantsC.put(p.getUniqueId(), enchsCost);
	}
	
	@EventHandler
	public void enchTableAdd (EnchantItemEvent e) {
		Player p = e.getEnchanter();
		ArrayList<Enchantment> ench = enchants.get(p.getUniqueId());
		ArrayList<Integer> enchC = enchantsC.get(p.getUniqueId());
		if (e.getExpLevelCost() == 30000) {
			e.getEnchantsToAdd().put(Enchantment.getByKey(ench.get(2).getKey()), enchC.get(2));
		}
		if (e.getExpLevelCost() == 20000) {
			e.getEnchantsToAdd().put(Enchantment.getByKey(ench.get(1).getKey()), enchC.get(1));
		}
		if (e.getExpLevelCost() == 10000) {
			e.getEnchantsToAdd().put(Enchantment.getByKey(ench.get(0).getKey()), enchC.get(0));
		}
	}
	
	@EventHandler
	public void expToss (ExpBottleEvent e) {
		e.setExperience(1000000);
		e.setShowEffect(false);
	}
	
	@EventHandler
	public void expDeath (EntityDeathEvent e) {
		int level = 1;
		if (Integer.valueOf(ChatColor.stripColor(e.getEntity().getCustomName()).replaceAll("\\D+","")) instanceof Integer) {
			level = Integer.valueOf(ChatColor.stripColor(e.getEntity().getCustomName()).replaceAll("\\D+",""));
		}
		if (e.getEntity() instanceof Player) {
			e.setDroppedExp(Math.round(((Player) e.getEntity()).getLevel() * 3000));
		} else {
			e.setDroppedExp(e.getDroppedExp() * 100000 * level);
		}
		if (e.getEntity().getKiller() instanceof Player && !(e.getEntity() instanceof Player) && e.getEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
			Player p = (Player) e.getEntity().getKiller();
			int entlevel = level;
			int exp = ((int) (7 * Math.pow(entlevel, 1.5))) + 50;
			int random = (int) (Math.random() * (0.20 * exp));
			exp+=random;
			Main.msg(p, "&7[+" + exp + "&7 XP]");
			plugin.getExpMap().replace(p.getUniqueId(), exp + plugin.getExp(p));
			plugin.setIntValue(p, "Exp", exp + plugin.getExp(p));
			plugin.levelup(p);
		}
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void expBar (PlayerExpChangeEvent e) {
		e.setAmount(0);
		plugin.levelup(e.getPlayer());
	}
	
}
