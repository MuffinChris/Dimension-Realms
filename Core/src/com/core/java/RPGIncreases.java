package com.core.java;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntitySpawnEvent;

public class RPGIncreases implements Listener {

	@EventHandler
	public void entityDamageSpawn (EntitySpawnEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			if (e.getEntity() instanceof LivingEntity) {
				LivingEntity ent = (LivingEntity) e.getEntity();
				double hp = ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
				ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp * 10.0);
			}
		}
	}
	
	@EventHandler
	public void bonusRegen (EntityRegainHealthEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getRegainReason() == RegainReason.REGEN) {
				e.setAmount(e.getAmount() * 4.0);
			} else {
				if (e.getRegainReason() == RegainReason.MAGIC_REGEN) {
					e.setAmount(e.getAmount() * 6.0);
				} else if (e.getRegainReason() == RegainReason.MAGIC) {
					e.setAmount(e.getAmount() * 6.0);
				}
			}
		}
	}
	
}
