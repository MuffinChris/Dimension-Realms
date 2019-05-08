package com.core.java.rpgbase.skills;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.core.java.essentials.Main;
import com.core.java.rpgbase.player.Armor;

public class ArmorSkills implements Listener {

	private Main main = Main.getInstance();
	
	public boolean containsString(ItemStack i, String s) {
		if (i != null) {
			if (i.getType().toString().contains(s)) {
				return true;
			}
		}
		return false;
	}
	
	public void removeLevels(Player p, int i) {
		p.setLevel(p.getLevel() - i);
	}
	
	@EventHandler
	public void rightClick(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getItem() != null) {
				if (containsString(e.getItem(), "SWORD")) {
					if (Armor.getSet(e.getPlayer()).contains("leather") || Armor.getSet(e.getPlayer()).contains("chain")) {
						if (main.getAbilities().get(e.getPlayer().getUniqueId()).get(0).equals("Eviscerate")) {
							if (e.getPlayer().getLevel() > 1500) {
								removeLevels(e.getPlayer(), 1500);
								e.getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
								e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20, 1));
								Vector v = new Vector(e.getPlayer().getLocation().getDirection().getX() * 1.2, 0.2, e.getPlayer().getLocation().getDirection().getZ() * 1.2);
								e.getPlayer().setVelocity(v);
								e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0F, 2.0F);
								e.getPlayer().setFallDistance(0);
								for (int increment = 0; increment <= 20; increment+=10) {
									new BukkitRunnable() {
										public void run() {
											e.getPlayer().setFallDistance(0);
										}
									}.runTaskLater(main, 10L + increment);
								}
								List<LivingEntity> entities = new ArrayList<LivingEntity>();
								entities.add(e.getPlayer());
								new BukkitRunnable() {
									int amount = 4;
									int count = 0;
									public void run() {
										amount--;
										e.getPlayer().setFallDistance(0);
										for (LivingEntity ent : getNearbyEnts(e.getPlayer(), 3, 2, 3)) {
											if (!entities.contains(ent)) {
												ent.damage(main.getAdMap().get(e.getPlayer().getUniqueId()) * 3.0, e.getPlayer());
												entities.add(ent);
												BlockData blood = Material.REDSTONE_BLOCK.createBlockData();
												ent.getWorld().spawnParticle(Particle.BLOCK_CRACK, ent.getLocation(), 100, 1, 1, 1, blood);
												e.getPlayer().getWorld().playSound(ent.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0F, 1.0F);
											}
											count++;
										}
										if (amount <= 0) {
											e.getPlayer().removePotionEffect(PotionEffectType.ABSORPTION);
											e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100 + 20 * count, count + 2));
											cancel();
										}
									}
								}.runTaskTimer(main, 1L, 4L);
								
							} else {
								e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.5F, 1.0F);
							}
						}
					}
				}
				if (containsString(e.getItem(), "AXE")) {
					if (Armor.getSet(e.getPlayer()).contains("leather"))  {
						if (main.getAbilities().get(e.getPlayer().getUniqueId()).get(1).equals("Leap")) {
							if (e.getPlayer().getLevel() > 500) {
								removeLevels(e.getPlayer(), 500);
								e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
								e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1));
								Vector v = new Vector(e.getPlayer().getLocation().getDirection().getX() * 1.4, 0.8, e.getPlayer().getLocation().getDirection().getZ() * 1.4);
								e.getPlayer().setVelocity(v);
								e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0F, 1.0F);
								e.getPlayer().setFallDistance(0);
								e.getPlayer().getWorld().spawnParticle(Particle.CLOUD, e.getPlayer().getLocation(), 25, 0, 1, 0, 0.3);
								for (int increment = 0; increment <= 40; increment+=5) {
									new BukkitRunnable() {
										public void run() {
											e.getPlayer().setFallDistance(0);
										}
									}.runTaskLater(main, 10L + increment);
								}
							} else {
								e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.5F, 1.0F);
							}
						}
					}
				}
			}
		}
	}
	
	public List<LivingEntity> getNearbyEnts(Player p, double x, double y, double z) {
		List<LivingEntity> ents = new ArrayList<LivingEntity>();
		for (Entity en : p.getWorld().getNearbyEntities(p.getLocation(), x, y, z)) {
			if (en instanceof LivingEntity) {
				LivingEntity ent = (LivingEntity) en;
				if (ent.getType() != EntityType.ARMOR_STAND && ent.getType() != EntityType.BOAT) {
					ents.add(ent);
				}
			}
		}
		return ents;
	}
	
}
