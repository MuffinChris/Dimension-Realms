package com.core.java.rpgbase.skills;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
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
					if (Armor.getSet(e.getPlayer()).contains("leather")) {
						if (e.getPlayer().getLevel() > 500) {
							removeLevels(e.getPlayer(), 500);
							e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
							e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1));
							Vector v = new Vector(e.getPlayer().getLocation().getDirection().getX() * 1.4, 0.8, e.getPlayer().getLocation().getDirection().getZ() * 1.4);
							e.getPlayer().setVelocity(v);
							e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0F, 1.0F);
							new BukkitRunnable() {
								public void run() {
									e.getPlayer().setFallDistance(0);
									new BukkitRunnable() {
										public void run() {
											e.getPlayer().setFallDistance(0);
											new BukkitRunnable() {
												public void run() {
													e.getPlayer().setFallDistance(0);
													new BukkitRunnable() {
														public void run() {
															e.getPlayer().setFallDistance(0);
														}
													}.runTaskLater(main, 10L);
												}
											}.runTaskLater(main, 10L);
										}
									}.runTaskLater(main, 10L);
								}
							}.runTaskLater(main, 10L);
						} else {
							e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_ANVIL_HIT, 1.0F, 1.0F);
						}
					}
				}
			}
		}
	}
	
}
