package com.core.java.rpgbase;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.Slime;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.core.java.essentials.Main;
import com.core.java.rpgbase.entities.PlayerList;
import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;

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
				e.setDamage(hp * 0.05);
			}
			if (e.getCause() == DamageCause.DROWNING) {
				e.setDamage(hp * 0.05);
			}
			if (e.getCause() == DamageCause.WITHER) {
				e.setDamage(hp / 30.0);
			}
			if (e.getCause() == DamageCause.FIRE_TICK) {
				e.setDamage(hp / 40.0);
			}
			if (e.getCause() == DamageCause.FIRE) {
				e.setDamage(hp / 40.0);
			}
			if (e.getCause() == DamageCause.LAVA) {
				e.setDamage(hp * 0.15);
			}
			if (e.getCause() == DamageCause.ENTITY_EXPLOSION) {
				e.setDamage(hp/6.0 + e.getDamage() * 3.0);
			}
			if (e.getCause() == DamageCause.BLOCK_EXPLOSION) {
				e.setDamage(hp/6.0 + e.getDamage() * 3.0);
			}
			if (e.getCause() == DamageCause.LIGHTNING) {
				e.setDamage(hp / 4.0);
			}
			if (e.getCause() == DamageCause.CONTACT) {
				e.setDamage(hp / 40.0);
			}
			if (e.getCause() == DamageCause.MELTING) {
				e.setDamage(hp / 20.0);
			}
			if (e.getCause() == DamageCause.FALLING_BLOCK) {
				e.setDamage(hp / 20.0);
			}
			if (e.getCause() == DamageCause.HOT_FLOOR) {
				e.setDamage(hp / 20.0);
			}
			if (e.getCause() == DamageCause.DRYOUT) {
				e.setDamage(hp / 20.0);
			}
		}
	}
	
	public List<LivingEntity> getNearbyEnts(Entity e, double x, double y, double z) {
		List<LivingEntity> ents = new ArrayList<LivingEntity>();
		for (Entity en : e.getWorld().getNearbyEntities(e.getLocation(), x, y, z)) {
			if (en instanceof LivingEntity) {
				LivingEntity ent = (LivingEntity) en;
				if (ent.getType() != EntityType.ARMOR_STAND && ent.getType() != EntityType.BOAT) {
					ents.add(ent);
				}
			}
		}
		return ents;
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void entityDamage (EntityDamageByEntityEvent e) {
		int level = 1;
		Entity ent = e.getDamager();
		if (ent.getCustomName() != null && Integer.valueOf(ChatColor.stripColor(ent.getCustomName()).replaceAll("\\D+","")) instanceof Integer) {
			level = Integer.valueOf(ChatColor.stripColor(ent.getCustomName()).replaceAll("\\D+",""));
		}
		if (e.getDamager() instanceof IronGolem) {
			e.setDamage(30 + 3 * level);
		}
		if (e.getDamager() instanceof Slime || e.getDamager() instanceof MagmaCube) {
			e.setDamage(10 + 1.5 * level);
		}
	}
	/*
	@EventHandler
	public void ironGolemSlam (EntityDamageEvent e) {
		if (e.getEntity() instanceof Entity) {
			if (e.getEntity().getType() == EntityType.IRON_GOLEM) {
				double chance = Math.random();
				if (chance >= 0.95) {
					Entity golem = e.getEntity();
					golem.setVelocity(new Vector(0, 1.5, 0));
					new BukkitRunnable() {
						public void run() {
							golem.setVelocity(new Vector(0, -5.0, 0));
							new BukkitRunnable() {
								public void run() {
									golem.setFallDistance(0);
									int level = 1;
									if (e.getEntity().getCustomName() != null && Integer.valueOf(ChatColor.stripColor(e.getEntity().getCustomName()).replaceAll("\\D+","")) instanceof Integer) {
										level = Integer.valueOf(ChatColor.stripColor(e.getEntity().getCustomName()).replaceAll("\\D+",""));
									}
									for (LivingEntity ent : getNearbyEnts(golem, 7, 5, 7)) {
										ent.damage(level * 3.0 + 20);
									}
									golem.getWorld().spawnParticle(Particle.TOTEM, golem.getLocation(), 100, 3, 0.1, 3);
									golem.getWorld().playSound(golem.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0F, 1.0F);
								}
							}.runTaskLater(main, 15L);
						}
					}.runTaskLater(main, 10L);
				}
			}
		}
	}*/
	
	@EventHandler
	public void entityInfoSpawn (EntityAddToWorldEvent e) {
		new BukkitRunnable() {
			public void run() {
			if (!(e.getEntity() instanceof Player)) {
				if (main.getPManager().getPList(e.getEntity()) == null) {
					main.getPManager().setPlayerList(e.getEntity(), new PlayerList());
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
						if (e.getEntity().getType() == EntityType.PHANTOM) {
							int rand = (int) (Math.random() * 15);
							if (rand != 10) {
								e.getEntity().remove();
							}
						}
						int level = 0;
						int terms = 0;
						if (ent.getNearbyEntities(64, 32, 64) instanceof List<?>) {
							for (Entity en : ent.getNearbyEntities(64, 32, 64)) {
								if (en instanceof Player) {
									Player p = (Player) en;
									terms++;
									level+=Main.getInstance().getLevelMap().get(p.getUniqueId());
								}
							}
						}
						if (terms > 0) {
							level/=terms;
						}
						if (level == 0) {
							level = 1;
						}
						double hp = ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
						double hpper = level * hp * 0.10;
						double hpmod = 20 + hpper * (Math.random() * 0.2 + 1) * 20;
						//double hpmod = (10 * (hp/20.0)) * level * (Math.random() * 0.2 + 1);
						double admod = 20 + 0.5 * level;
						if (ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
							double ad = ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue();
							admod = ad * 0.1 * level * 10;
						}
						if (ent.getType() == EntityType.IRON_GOLEM) {
							hpmod*=2;
						}
						if (ent.getType() == EntityType.SLIME || ent.getType() == EntityType.MAGMA_CUBE) {
							hpmod*=1.25;
							ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hpmod);
							ent.setHealth(hpmod);
						}
						if (!slime) {
							ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hpmod);
							ent.setHealth(hpmod);
							if (ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
								ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(admod);
							}
							ent.setCustomName(Main.color("&8[&6" + level + "&8] &f" + ent.getName()));
							ent.setCustomNameVisible(true);
						} else {
							hpmod*=1.25;
							ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hpmod);
							ent.setHealth(hpmod);
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
		}.runTaskLaterAsynchronously(main, 1L);
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
