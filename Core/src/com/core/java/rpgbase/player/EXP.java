package com.core.java.rpgbase.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
		if (e.getEntity() instanceof Player) {
			e.setDroppedExp(Math.round(((Player) e.getEntity()).getLevel() * 3000));
		} else {
			if (e.getEntity().getCustomName().replaceAll("\\D+","") != "") {
				e.setDroppedExp(e.getDroppedExp() * 100000 * Integer.valueOf(e.getEntity().getCustomName().replaceAll("\\D+","")));
			}
		}
		if (e.getEntity().getKiller() instanceof Player && !(e.getEntity() instanceof Player)) {
			if (e.getEntity().getCustomName().replaceAll("\\D+","") != "") {
				Player p = (Player) e.getEntity().getKiller();
			    int entlevel = Integer.valueOf(e.getEntity().getCustomName().replaceAll("\\D+",""));
				if (!(plugin.getLevel(p) + 10 >= entlevel)) {
					int maxlevel = plugin.getExpMax(p);
					int level = plugin.getLevel(p);
					int randomFactor = (int) (Math.random() * 0.003 * (maxlevel - (level - entlevel)) + 1);
					int exp = (int) (0.007 * maxlevel) + randomFactor;
					float newexp = p.getExp() + (exp/maxlevel);
					if (newexp >= 1.0 && plugin.getLevel(p) < 100) {
						levelup(p);
					} else {
						p.setExp(newexp);
						plugin.getExpMap().replace(p.getUniqueId(), (int) (newexp * maxlevel));
						plugin.setIntValue(p, "Exp", (int) (newexp * maxlevel));
					}
					
				}
			}
		}
	}
	
	public void levelup (Player p) {
		while (plugin.getExpMax(p) <= plugin.getExp(p)) {
			int maxlevel = plugin.getExpMax(p);
			int newexp = (plugin.getExp(p) - maxlevel);
			int newlevel = plugin.getLevel(p) + 1;
			plugin.getLevelMap().replace(p.getUniqueId(), newlevel);
			plugin.setIntValue(p, "Level", newlevel);
			maxlevel = plugin.getExpMax(p);
			plugin.getExpMap().replace(p.getUniqueId(), newexp);
			plugin.setIntValue(p, "Exp", newexp);
			int newsp = plugin.getSPMap().get(p.getUniqueId()) + 2;
			plugin.getSPMap().replace(p.getUniqueId(), newsp);
			plugin.setIntValue(p, "SP", newsp);
			Main.msg(p, "&7&l--------------------------");
			Main.msg(p, "");
			Main.msg(p, "&7» &e&lLEVEL UP: &6" + (newlevel - 1) + " &f-> &6" + newlevel);
			Main.msg(p, "&7» &e&lSP INCREASE: &f+2");
			Main.msg(p, "");
			Main.msg(p, "&7&l--------------------------");
		}
		p.setExp(Math.min(plugin.getExp(p) / plugin.getExpMax(p), 0.99F));
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void expBar (PlayerExpChangeEvent e) {
		e.setAmount(0);
	}
	
}
