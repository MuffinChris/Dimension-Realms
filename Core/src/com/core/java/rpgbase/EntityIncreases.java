package com.core.java.rpgbase;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntityIncreases implements Listener {

	@EventHandler (priority = EventPriority.LOWEST)
	public void addDamage (EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			double hp = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
			if (e.getCause() == DamageCause.FALL) {
				e.setDamage(e.getDamage() * (hp / 20.0));
			}
			if (e.getCause() == DamageCause.MAGIC) {
				e.setDamage(e.getDamage() * 10.0);
			}
			if (e.getCause() == DamageCause.POISON) {
				if (p.getHealth() > (e.getDamage() * 2.5)) {
					e.setDamage(e.getDamage() * 2.5);
				}
			}
			if (e.getCause() == DamageCause.STARVATION) {
				e.setDamage(10);
			}
			if (e.getCause() == DamageCause.DROWNING) {
				e.setDamage(10);
			}
			if (e.getCause() == DamageCause.WITHER) {
				e.setDamage(e.getDamage() * (hp / 40.0));
			}
			if (e.getCause() == DamageCause.PROJECTILE) {
				e.setDamage(e.getDamage() * 7.0);
			}
			if (e.getCause() == DamageCause.FIRE_TICK) {
				e.setDamage(5.0);
			}
			if (e.getCause() == DamageCause.LAVA) {
				e.setDamage(25.0);
			}
		}
	}
	
	@EventHandler
	public void entityDamageSpawn (EntitySpawnEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			if (e.getEntity() instanceof LivingEntity) {
				LivingEntity ent = (LivingEntity) e.getEntity();
				double hp = ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
				ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp * 10.0);
				if (ent.getType() != null) {
					if (ent.getType() == EntityType.ZOMBIE) {
						ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(23);
					}
					if (ent.getType() == EntityType.SPIDER) {
						ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(25);
					}
					if (ent.getType() == EntityType.CAVE_SPIDER) {
						ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(23);
					}
					if (ent.getType() == EntityType.SLIME) {
						ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(20);
					}
					if (ent.getType() == EntityType.MAGMA_CUBE) {
						ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(22);
					}
					if (ent.getType() == EntityType.ENDER_DRAGON) {
						ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(40);
					}
					if (ent.getType() == EntityType.WOLF) {
						ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(30);
					}
					if (ent.getType() == EntityType.IRON_GOLEM) {
						ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(35);
					}
					if (ent.getType() == EntityType.SILVERFISH) {
						ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(15);
					}
					if (ent.getType() == EntityType.DROWNED) {
						ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(30);
					}
					if (ent.getType() == EntityType.HUSK) {
						ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(25);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void bonusRegen (EntityRegainHealthEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getRegainReason() == RegainReason.REGEN || e.getRegainReason() == RegainReason.EATING) {
				Player p = (Player) e.getEntity();
				e.setAmount(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() * 0.05D);
				//not working?
			} else {
				if (e.getRegainReason() == RegainReason.MAGIC_REGEN) {
					e.setAmount(e.getAmount() * 8.0);
				} else if (e.getRegainReason() == RegainReason.MAGIC) {
					e.setAmount(e.getAmount() * 8.0);
				}
			}
		}
	}
	
}
