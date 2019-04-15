package com.core.java.rpgbase.skills;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.core.java.rpgbase.player.Armor;

public class ArmorSkills implements Listener {

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
						} else {
							e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_ANVIL_BREAK, 1.0F, 1.0F);
						}
					}
				}
			}
		}
	}
	
}
