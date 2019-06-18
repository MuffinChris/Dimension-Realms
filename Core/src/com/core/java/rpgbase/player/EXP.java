package com.core.java.rpgbase.player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.core.java.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.core.java.essentials.Main;
import com.core.java.rpgbase.entities.PlayerList;
import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;

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

	public boolean checkExp (double exp) {
		if (Double.isFinite(exp)) {
			return true;
		}
		return false;
	}

	public void giveExp (Player p, double exp) {
		if (checkExp(exp)) {
			DecimalFormat df = new DecimalFormat("#");
			if (exp >= 0) {
				Main.msg(p, "&7[+" + df.format(exp) + "&7 XP]");
				plugin.getExpMap().replace(p.getUniqueId(), exp + plugin.getExp(p));
				plugin.setDoubleValue(p, "Exp", exp + plugin.getExp(p));
				plugin.levelup(p);
			} else {
				Main.msg(p, "&7[-" + df.format(Math.abs(exp)) + "&7 XP]");
				double newexp = Math.max(plugin.getExp(p) + exp, 0);
				plugin.getExpMap().replace(p.getUniqueId(), newexp);
				plugin.setDoubleValue(p, "Exp", newexp);
				plugin.levelup(p);
			}
		}
	}

	public void giveExp (Player p, double exp, boolean party) {
		if (checkExp(exp)) {
			DecimalFormat df = new DecimalFormat("#");
			Main.msg(p, "&7[+" + df.format(exp) + " &7(&a+" + df.format(exp * (Constants.PartyXPMult-1)) + "&7)" + "&7 XP]");
			exp = (exp * Constants.PartyXPMult);
			plugin.getExpMap().replace(p.getUniqueId(), exp + plugin.getExp(p));
			plugin.setDoubleValue(p, "Exp", exp + plugin.getExp(p));
			plugin.levelup(p);
		}
	}

	public void giveExp (Player p, double exp, double percent, boolean party) {
		if (checkExp(exp)) {
			if (percent >= 0 && percent <= 1) {
				exp = Math.abs(exp);
				DecimalFormat df = new DecimalFormat("#");
				DecimalFormat dF = new DecimalFormat("#.##");
				Main.msg(p, "&7[+" + df.format(exp) + " &7(&a+" + df.format(exp * (Constants.PartyXPMult - 1)) + "&7)" + " &7(" + dF.format(percent * 100) + "%) XP]");
				exp = (exp * Constants.PartyXPMult);
				plugin.getExpMap().replace(p.getUniqueId(), exp + plugin.getExp(p));
				plugin.setDoubleValue(p, "Exp", exp + plugin.getExp(p));
				plugin.levelup(p);
			}
		}
	}
	
	public void giveExp (Player p, double exp, double percent) {
		if (checkExp(exp)) {
			if (percent >= 0 && percent <= 1) {
				exp = Math.abs(exp);
				DecimalFormat dF = new DecimalFormat("#.##");
				DecimalFormat df = new DecimalFormat("#");
				Main.msg(p, "&7[+" + df.format(exp) + " &7(" + dF.format(percent * 100) + "%) XP]");
				plugin.getExpMap().replace(p.getUniqueId(), exp + plugin.getExp(p));
				plugin.setDoubleValue(p, "Exp", exp + plugin.getExp(p));
				plugin.levelup(p);
			}
		}
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
		if (!(e.getEntity() instanceof Player)) {
			int entlevel = level;
			double exp = ((1 * Math.pow(entlevel, 2.4))) + 50;
			double random = (Math.random() * (0.50 * exp));

			if ((e.getEntity() instanceof Animals || e.getEntity() instanceof Fish || e.getEntity() instanceof Squid) && !slime) {
				exp = (exp/25.0);
				random = (Math.random() * (2 * exp));
			}

			exp+=random;
			double diffi = 1;
			Entity ent = e.getEntity();
			if (ent.getType() != null) {
				if (ent.getType() == EntityType.ZOMBIE) {
					diffi = 1;
				}
				if (ent.getType() == EntityType.SPIDER) {
					diffi = 1.2;
				}
				if (ent.getType() == EntityType.CAVE_SPIDER) {
					diffi = 1.4;
				}
				if (ent.getType() == EntityType.SLIME) {
					diffi = 1.0;
				}
				if (ent.getType() == EntityType.MAGMA_CUBE) {
					diffi = 1.0;
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
				if (ent.getType() == EntityType.PIG_ZOMBIE) {
					diffi = 1.5;
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
				if (ent.getType() == EntityType.WITHER) {
					diffi = 250;
				}
				if (ent.getType() == EntityType.ENDER_DRAGON) {
					diffi = 1000;
				}
				if (ent.getType() == EntityType.PILLAGER) {
					diffi = 2;
				}
				if (ent.getType() == EntityType.VINDICATOR) {
					diffi = 5;
				}
				//
				if (ent.getType() == EntityType.GUARDIAN) {
					diffi = 2.5;
				}
				if (ent.getType() == EntityType.ILLUSIONER) {
					diffi = 5;
				}
				if (ent.getType() == EntityType.RAVAGER) {
					diffi = 10;
				}
			}
			double newexp = (exp * diffi);
			exp = newexp;
			PlayerList plist = plugin.getPManager().getPList(e.getEntity());
			if (!checkExp(exp)) {
				exp = 1000000;
			}
			double fulldmg = 0;
			if (plist != null && plist.getPlayers() instanceof List<?>) {
				fulldmg = plugin.getPManager().getPList(e.getEntity()).getFullDmg();
				if (fulldmg <= 0) {
					return;
				}
				for (Player pl : plist.getPlayers()) {
					double dmg = plist.getDamage(pl);
					if ((dmg / fulldmg) >= 1.0) {
						boolean go = true;
						if (plugin.getPM().hasParty(pl)) {
							Party party = plugin.getPM().getParty(pl);
							if (party.getPlayers().size() > 1 && party.getShare()) {
								go = false;
								int size = party.getPlayers().size();
								for (Player pp : party.getPlayers()) {
									if (pl.getLocation().distance(pp.getLocation()) <= 100) {
										if (!pp.equals(pl)) {
											giveExp(pp, ((exp * (dmg / fulldmg)) / size), true);
										}
									}
								}
								giveExp(pl, ((exp * (dmg / fulldmg)) / size), true);
							}
						}
						if (go) {
							giveExp(pl, (exp * (dmg / fulldmg)));
						}
					} else {
						boolean go = true;
						if (plugin.getPM().hasParty(pl)) {
							Party party = plugin.getPM().getParty(pl);
							if (party.getPlayers().size() > 1 && party.getShare()) {
								go = false;
								int size = party.getPlayers().size();
								for (Player pp : party.getPlayers()) {
									if (pl.getLocation().distance(pp.getLocation()) <= 100) {
										if (!pp.equals(pl)) {
											giveExp(pp, ((exp * (dmg / fulldmg)) / size), (dmg / fulldmg), true);
										}
									}
								}
								giveExp(pl, ((exp * (dmg / fulldmg)) / size), (dmg / fulldmg), true);
							}
						}
						if (go) {
							giveExp(pl, (exp * (dmg / fulldmg)), (dmg / fulldmg));
						}
					}
				}
			}
		}
		if (e.getEntity() instanceof Player) {
			Player pl = (Player) e.getEntity();
			if (pl.getKiller() instanceof Player) {
				Player p = (Player) e.getEntity().getKiller();
				double exp = ((27 * Math.pow(plugin.getLevel(pl), 3))) + 50;
				double random = (Math.random() * (0.15 * exp));
				exp+=random;
				boolean go = true;
				if (plugin.getPM().hasParty(p)) {
					Party party = plugin.getPM().getParty(p);
					if (party.getPlayers().size() > 1 && party.getShare()) {
						go = false;
						int size = party.getPlayers().size();
						for (Player pp : party.getPlayers()) {
							if (p.getLocation().distance(pp.getLocation()) <= 100) {
								if (!p.equals(pp)) {
									giveExp(pp, ((exp) / size), true);
								}
							}
						}
						giveExp(p, ((exp) / size), true);
					}
				}
				if (go) {
					giveExp(p, ((exp)));
				}
			}
			double exp = ((12 * Math.pow(plugin.getLevel(pl), 3))) + 50;
			double random = (Math.random() * (0.75 * exp));
			exp+=random;
			giveExp(pl, -exp);
		}
		e.setDroppedExp(0);
		new BukkitRunnable() {
			public void run() {
				if (!(e.getEntity() instanceof Player)) {
					if (plugin.getPManager().getPList().containsKey(e.getEntity())) {
						plugin.getPManager().getPList().remove(e.getEntity());
					}
				} else {
					plugin.getPManager().getPList(e.getEntity()).getList().clear();
				}
			}
		}.runTaskLater(plugin, 10L);
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
					//expallowed.add(p);
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void expOnDeath (PlayerDeathEvent e) {
		Player p = (Player) e.getEntity();
		plugin.getCManaMap().replace(p.getUniqueId(), 0);
	}

	@EventHandler (priority = EventPriority.LOWEST)
	public void expBar (PlayerExpChangeEvent e) {
		int maxmana = plugin.getManaMap().get(e.getPlayer().getUniqueId());
		int cmana = plugin.getCManaMap().get(e.getPlayer().getUniqueId());
		e.getPlayer().setExp(Math.max(Math.min(((1.0F * cmana) / (1.0F * maxmana)), 0.99F), 0.0F));
	}
	
	//public List<Player> expallowed = new ArrayList<Player>();
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void expPickup (PlayerPickupExperienceEvent e) {
		e.setCancelled(true);
		int maxmana = plugin.getManaMap().get(e.getPlayer().getUniqueId());
		int cmana = plugin.getCManaMap().get(e.getPlayer().getUniqueId());
		e.getPlayer().setExp(Math.max(Math.min(((1.0F * cmana) / (1.0F * maxmana)), 0.99F), 0.0F));
		int level = plugin.getLevel(e.getPlayer());
		double amount = e.getExperienceOrb().getExperience();
		giveExp(e.getPlayer(), (30 + Math.pow((level * 1.0) / 2.0, 1.8) * Math.pow(amount, 1.8)));
		e.getExperienceOrb().remove();
	}
	
}
