package com.core.java.rpgbase.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.core.java.essentials.Main;

public class EXP implements Listener {

	private Main plugin = Main.getInstance();
	/*
	public Map<UUID, ArrayList<Enchantment>> enchants = new HashMap<UUID, ArrayList<Enchantment>>();
	public Map<UUID, ArrayList<Integer>> enchantsC = new HashMap<UUID, ArrayList<Integer>>();
	
	@EventHandler
	public void onJoin (PlayerJoinEvent e) {
		enchants.put(e.getPlayer().getUniqueId(), null);
		enchantsC.put(e.getPlayer().getUniqueId(), null);
	}
	*/
	/*@EventHandler
	public void anvil (PrepareAnvilEvent e) {
		e.
	}*/
	
	/*
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
	}*/
	
	/*
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
	}*/
	
	/*
	@EventHandler
	public void expToss (ExpBottleEvent e) {
		e.setExperience(1000000);
		e.setShowEffect(false);
	}*/

	public void giveExp (Player p, int exp) {
		Main.msg(p, "&7[+" + exp + "&7 XP]");
		plugin.getExpMap().replace(p.getUniqueId(), exp + plugin.getExp(p));
		plugin.setIntValue(p, "Exp", exp + plugin.getExp(p));
		plugin.levelup(p);
	}
	
	@EventHandler
	public void expDeath (EntityDeathEvent e) {
		int level = 1;
		if (e.getEntity().getCustomName() != null && Integer.valueOf(ChatColor.stripColor(e.getEntity().getCustomName()).replaceAll("\\D+","")) instanceof Integer) {
			level = Integer.valueOf(ChatColor.stripColor(e.getEntity().getCustomName()).replaceAll("\\D+",""));
		}
		boolean slime = false;
		if (e.getEntity().getType() == EntityType.SLIME || e.getEntity().getType() == EntityType.MAGMA_CUBE) {
			slime = true;
		}
		if (e.getEntity().getKiller() instanceof Player && !(e.getEntity() instanceof Player) && (slime || e.getEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null)) {
			Player p = (Player) e.getEntity().getKiller();
			int entlevel = level;
			int exp = ((int) (7 * Math.pow(entlevel, 1.5))) + 50;
			int random = (int) (Math.random() * (0.20 * exp));
			
			exp+=random;
			double diffi = 1;
			Entity ent = e.getEntity();
			if (ent.getType() != null) {
				if (ent.getType() == EntityType.ZOMBIE) {
					diffi = 1;
				}
				if (ent.getType() == EntityType.SPIDER) {
					diffi = 1.6;
				}
				if (ent.getType() == EntityType.CAVE_SPIDER) {
					diffi = 1.7;
				}
				if (ent.getType() == EntityType.SLIME) {
					diffi = 0.1;
				}
				if (ent.getType() == EntityType.MAGMA_CUBE) {
					diffi = 0.1;
				}
				if (ent.getType() == EntityType.ENDER_DRAGON) {
					diffi = 100;
				}
				if (ent.getType() == EntityType.WOLF) {
					diffi = 1;
				}
				if (ent.getType() == EntityType.IRON_GOLEM) {
					diffi = 2.0;
				}
				if (ent.getType() == EntityType.SILVERFISH) {
					diffi = 0.8;
				}
				if (ent.getType() == EntityType.DROWNED) {
					diffi = 1.5;
				}
				if (ent.getType() == EntityType.ENDERMAN) {
					diffi = 2.5;
				}
				if (ent.getType() == EntityType.ENDERMITE) {
					diffi = 0.65;
				}
				if (ent.getType() == EntityType.SKELETON) {
					diffi = 1.4;
				}
				if (ent.getType() == EntityType.WITHER_SKELETON) {
					diffi = 3.3;
				}
				if (ent.getType() == EntityType.ELDER_GUARDIAN) {
					diffi = 5.0;
				}
				if (ent.getType() == EntityType.GUARDIAN) {
					diffi = 2.5;
				}
				if (ent.getType() == EntityType.BLAZE) {
					diffi = 1.9;
				}
				if (ent.getType() == EntityType.CREEPER) {
					diffi = 2.2;
				}
				if (ent.getType() == EntityType.GHAST) {
					diffi = 2.5;
				}
				if (ent.getType() == EntityType.GIANT) {
					diffi = 3.0;
				}
				if (ent.getType() == EntityType.PHANTOM) {
					diffi = 2.0;
				}
				if (ent.getType() == EntityType.STRAY) {
					diffi = 1.2;
				}
			}
			int newexp = (int) (exp * diffi);
			exp = newexp;
			giveExp(p, exp);
		}
		e.setDroppedExp(0);
	}
	
	@EventHandler
	public void expBottle (PlayerInteractEvent e) {
		if (e.getItem() != null) {
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (e.getItem().getType() == Material.EXPERIENCE_BOTTLE) {
					int amount = e.getItem().getAmount();
					Player p = e.getPlayer();
					int levels = amount;
					p.setLevel(p.getLevel() + levels);
					e.getItem().setAmount(0);
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void expBar (PlayerExpChangeEvent e) {
		int maxmana = plugin.getManaMap().get(e.getPlayer().getUniqueId());
		int cmana = plugin.getCManaMap().get(e.getPlayer().getUniqueId());
		e.getPlayer().setExp(Math.max(((1.0F * cmana) / (1.0F * maxmana)), 0.99F));
		giveExp(e.getPlayer(), e.getAmount());
	}
	
}
