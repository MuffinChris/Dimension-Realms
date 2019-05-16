package com.core.java.rpgbase;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntitySpawnEvent;

import com.core.java.essentials.Main;
import com.core.java.rpgbase.entities.PlayerList;

public class EntityIncreases implements Listener {

	private Main main = Main.getInstance();
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void addDamage (EntityDamageEvent e) {
		if (e.getEntity() instanceof LivingEntity) {
			LivingEntity p = (LivingEntity) e.getEntity();
			double hp = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
			if (e.getCause() == DamageCause.FALL) {
				e.setDamage(e.getDamage() * (hp / 20.0));
			}
			if (e.getCause() == DamageCause.MAGIC) {
				e.setDamage(e.getDamage() * 4.0);
			}
			if (e.getCause() == DamageCause.POISON) {
				if (p.getHealth() > (hp * 0.025)) {
					e.setDamage(hp * 0.025);
				}
			}
			if (e.getCause() == DamageCause.STARVATION) {
				e.setDamage(hp * 0.01);
			}
			if (e.getCause() == DamageCause.DROWNING) {
				e.setDamage(hp * 0.01);
			}
			if (e.getCause() == DamageCause.WITHER) {
				e.setDamage(hp / 30.0);
			}
			if (e.getCause() == DamageCause.FIRE_TICK) {
				e.setDamage(10);
			}
			if (e.getCause() == DamageCause.LAVA) {
				e.setDamage(hp * 0.15);
			}
			if (e.getCause() == DamageCause.ENTITY_EXPLOSION) {
				e.setDamage(e.getDamage() * 5);
			}
			if (e.getCause() == DamageCause.BLOCK_EXPLOSION) {
				e.setDamage(e.getDamage() * 4);
			}
			if (e.getCause() == DamageCause.LIGHTNING) {
				e.setDamage(50);
			}
			if (e.getCause() == DamageCause.CONTACT) {
				e.setDamage(10);
			}
		}
	}
	
	@EventHandler
	public void entityInfoSpawn (EntitySpawnEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			if (main.getPManager().getPList(e.getEntity()) == null) {
				main.getPManager().setPlayerList(e.getEntity(), new PlayerList());
			}
			if (e.getEntity().getType() == EntityType.PHANTOM) {
				int rand = (int) (Math.random() * 15);
				if (rand != 10) {
					e.setCancelled(true);
				}
			}
			if (e.getEntity() instanceof LivingEntity) {
				boolean slime = false;
				if (e.getEntity().getType() == EntityType.SLIME || e.getEntity().getType() == EntityType.MAGMA_CUBE) {
					if (e.getEntity().getCustomName() instanceof String && ((LivingEntity) e.getEntity()).getCustomName().contains("[")) {
						e.getEntity().setCustomNameVisible(true);
						slime = true;
					}
				}
				LivingEntity ent = (LivingEntity) e.getEntity();
				if (!(ent.getCustomName() instanceof String)) {
					int level = 0;
					int terms = 0;
					for (Entity en : ent.getNearbyEntities(64, 32, 64)) {
						if (en instanceof Player) {
							Player p = (Player) en;
							terms++;
							level+=Main.getInstance().getLevelMap().get(p.getUniqueId());
						}
					}
					if (terms > 0) {
						level/=terms;
					}
					if (level == 0) {
						level = 1;
					}
					double hp = ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
					double hpmod = (20 * (hp/20.0)) * level;
					double admod = 0.4 * level;
					if (ent.getType() == EntityType.SLIME || ent.getType() == EntityType.MAGMA_CUBE) {
						hpmod = (100 * (hp/20.0)) * level;
						ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp * 4 + hpmod);
						ent.setHealth(hp * 4 + hpmod);
					}
					if (!slime) {
						ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp * 4 + hpmod);
						ent.setHealth(hp * 4 + hpmod);
						if (ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
							double ad = ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue();
							ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(ad * 3 + admod);
						}
						ent.setCustomName(Main.color("&8[&6" + level + "&8] &f" + ent.getName()));
						ent.setCustomNameVisible(true);
					} else {
						int slimelevel = Integer.valueOf(ChatColor.stripColor(e.getEntity().getCustomName()).replaceAll("\\D+",""));
						hpmod = (100 * (hp/20.0)) * slimelevel;
						ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp * 4 + hpmod);
						ent.setHealth(hp * 4 + hpmod);
					}
					/*
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
							ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(50);
						}
						if (ent.getType() == EntityType.WOLF) {
							ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(30);
						}
						if (ent.getType() == EntityType.IRON_GOLEM) {
							ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(40);
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
					*/
				}
			}
		}
	}
	
	@EventHandler
	public void bonusRegen (EntityRegainHealthEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getEntity().isDead()) {
				e.setCancelled(true);
			} else {
				Player p = (Player) e.getEntity();
				double hp = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
				if (e.getRegainReason() == RegainReason.REGEN || e.getRegainReason() == RegainReason.SATIATED) {
					e.setAmount(hp * 0.05);
				} else {
					if (e.getRegainReason() == RegainReason.MAGIC_REGEN) {
						e.setAmount(e.getAmount() * 4 + hp * 0.04);
					} else if (e.getRegainReason() == RegainReason.MAGIC) {
						e.setAmount(e.getAmount() * 3 + hp * 0.075);
					} else if (e.getRegainReason() == RegainReason.EATING) {
						e.setAmount(e.getAmount() * 5 + hp * 0.05);
					}
				//}
			}
		}
	}
	//Healing should be main method of health increase
	}
}
