package com.core.java.rpgbase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
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
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.core.java.essentials.Main;
import com.core.java.rpgbase.entities.PlayerList;
import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;

public class EntityIncreases implements Listener {

	private Main main = Main.getInstance();

	@EventHandler(priority = EventPriority.LOWEST)
	public void potions(PlayerItemConsumeEvent e) {
		if (e.getItem() instanceof ItemStack) {
			if (e.getItem().getType() == Material.POTION) {
				if (e.getItem().getItemMeta() instanceof PotionMeta) {
					PotionMeta meta = (PotionMeta) e.getItem().getItemMeta();
					if (meta.getBasePotionData().getType() == PotionType.INSTANT_DAMAGE) {
						double hp = e.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
						if (meta.getBasePotionData().isUpgraded()) {
							e.getPlayer().damage(300 + hp * 0.2);
						} else {
							e.getPlayer().damage(200 + hp * 0.1);
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void addDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof LivingEntity) {
			LivingEntity p = (LivingEntity) e.getEntity();
			double hp = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
			if (e.getCause() == DamageCause.FALL) {
				e.setDamage(e.getDamage() * (hp / 20.0));
			}
			if (e.getCause() == DamageCause.MAGIC) {
				e.setDamage(e.getDamage() / 4.0 * hp / 20.0);
			}
			if (e.getCause() == DamageCause.POISON) {
				if (p.getHealth() > (hp * 0.025)) {
					e.setDamage(hp * 0.025);
				} else {
					e.setCancelled(true);
				}
			}
			if (e.getCause() == DamageCause.SUFFOCATION) {
				e.setDamage(hp * 0.075);
			}
			if (e.getCause() == DamageCause.STARVATION) {
				e.setDamage(hp * 0.1);
			}
			if (e.getCause() == DamageCause.DROWNING) {
				e.setDamage(hp * 0.1);
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
				e.setDamage((e.getDamage() / 30.0) * hp);
			}
			if (e.getCause() == DamageCause.BLOCK_EXPLOSION) {
				e.setDamage(0.3 * (e.getDamage() / 20.0) * hp);
			}
			if (e.getCause() == DamageCause.LIGHTNING) {
				e.setDamage((e.getDamage() / 20) * hp);
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
				e.setDamage(hp / 10.0);
			}
			if (e.getCause() == DamageCause.VOID) {
				if (e.getDamage() < 1000000) {
					e.setDamage(hp * 0.1);
				}
			}
			if (e.getCause() == DamageCause.FLY_INTO_WALL) {
				e.setDamage((e.getDamage() / 4.0) * hp * 0.1);
			}
		}
	}

	@EventHandler
	public void lingeringPotion(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof AreaEffectCloud) {
			AreaEffectCloud cloud = (AreaEffectCloud) e.getDamager();
			if (cloud.getBasePotionData().getType() == PotionType.INSTANT_DAMAGE) {
				if (e.getEntity() instanceof LivingEntity) {
					e.setDamage((e.getDamage() / 3.0) * (((LivingEntity) e.getEntity()).getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() * 0.04));
				}
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

	@EventHandler(priority = EventPriority.LOWEST)
	public void entityDamage(EntityDamageByEntityEvent e) {
		int level = 1;
		Entity ent = e.getDamager();
		try {
			if (ent.getCustomName() != null && Integer.valueOf(ChatColor.stripColor(ent.getCustomName()).replaceAll("\\D+", "")) instanceof Integer) {
				level = Integer.valueOf(ChatColor.stripColor(ent.getCustomName()).replaceAll("\\D+", ""));
			}
		} catch (Exception ex) {
			Main.so("&c&lException when parsing number for Entity Bonus Damage for Golems and Slimes");
		}
		if (e.getDamager() instanceof IronGolem) {
			e.setDamage(300 + 20 * level);
		}
		if (e.getDamager() instanceof Slime || e.getDamager() instanceof MagmaCube) {
			e.setDamage(50 + 10 * level);
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

	public int minMax(Entity ent, int level) {
		if (ent.getType() == EntityType.VILLAGER) {
			if (level < 30) {
				level = 30;
			}
		}
		if (ent.getType() == EntityType.IRON_GOLEM) {
			if (level < 50) {
				level = 50;
			}
		}
		if ((ent instanceof Animals || ent instanceof Fish)) {
			if (level > 30) {
				level = 30;
			}
		}
		if (ent.getType() == EntityType.TRADER_LLAMA) {
			if (level < 50) {
				level = 50;
			}
		}
		if (ent.getType() == EntityType.WANDERING_TRADER) {
			if (level < 100) {
				level = 100;
			}
		}
		if (ent.getType() == EntityType.ENDER_DRAGON) {
			if (level < 50) {
				level = 50;
			}
		}
		if (ent.getType() == EntityType.WITHER) {
			if (level < 30) {
				level = 30;
			}
		}
		if (ent.getType() == EntityType.PILLAGER) {
			if (level < 10) {
				level = 10;
			}
		}
		if (ent.getType() == EntityType.VINDICATOR) {
			if (level < 10) {
				level = 10;
			}
		}
		if (ent.getType() == EntityType.EVOKER) {
			if (level < 10) {
				level = 10;
			}
		}
		if (ent.getType() == EntityType.WOLF) {
			if (level < 5) {
				level = 5;
			}
		}
		return level;
	}

	public void setLvldHP(LivingEntity ent, int level) {
		double hp = ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
		//double hpper = level * hp * 0.10;
		double hpmod = main.gn().getMobHp(level) * hp;
		//double hpmod = (10 * (hp/20.0)) * level * (Math.random() * 0.2 + 1);
		double admod = main.gn().getMobDmg(level, 1);

		if (ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
			double ad = ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue();
			admod = main.gn().getMobDmg(level, ad);
		}
		if (ent.getType() == EntityType.SLIME || ent.getType() == EntityType.MAGMA_CUBE) {
			ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hpmod);
			ent.setHealth(hpmod);
		}
		if (!(ent instanceof Slime) && !(ent instanceof MagmaCube)) {
			ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hpmod);
			ent.setHealth(hpmod);
			if (ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
				ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(admod);
			}
			if (ent.getName().contains("[")) {
				ent.setCustomName(Main.color("&8[&6" + level + "&8] &f" + Main.color(ent.getName().split("] ")[1])));
			} else {
				ent.setCustomName(Main.color("&8[&6" + level + "&8] &f" + ent.getName()));
			}
			ent.setCustomNameVisible(true);
		} else {
			ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hpmod);
			ent.setHealth(hpmod);
		}
	}


	@EventHandler
	public void entityInfoSpawn (EntityAddToWorldEvent e) {
		new BukkitRunnable() {
			public void run() {
			if (!(e.getEntity() instanceof Player)) {
				if (main.getPManager().getPList(e.getEntity()) == null) {
					main.getPManager().setPlayerList(e.getEntity(), new PlayerList());
				}
				if (e.getEntity() instanceof LivingEntity && e.getEntity().getType() != EntityType.ARMOR_STAND) {
					boolean slime = false;
					if (e.getEntity().getType() == EntityType.SLIME || e.getEntity().getType() == EntityType.MAGMA_CUBE) {
						if (e.getEntity().getCustomName() instanceof String && ((LivingEntity) e.getEntity()).getCustomName().contains("[")) {
							e.getEntity().setCustomNameVisible(true);
							slime = true;
						}
					}
					LivingEntity ent = (LivingEntity) e.getEntity();
					int readdlevel = 0;
					if (ent.getCustomName() instanceof String) {
						if (e.getEntity().getCustomName() != null && Integer.valueOf(ChatColor.stripColor(e.getEntity().getCustomName()).replaceAll("\\D+","")) instanceof Integer) {
							readdlevel = Integer.valueOf(ChatColor.stripColor(e.getEntity().getCustomName()).replaceAll("\\D+",""));
							readdlevel = minMax(e.getEntity(), readdlevel);
							ent.setCustomName(Main.color("&8[&6" + readdlevel + "&8] &f" + Main.color(ent.getName().split("] ")[1])));

							//setLvldHP(ent, readdlevel);

						}
						if (e.getEntity().getCustomName() != null) {
							int amt = 0;
							for (char c : e.getEntity().getCustomName().toCharArray()) {
								if (c == '[') {
									amt++;
								}
							}
							if (amt >= 2) {
								Main.so("&cRemoved Entity " + e.getEntity().getType().toString() + " at location " + e.getEntity().getLocation().getX() + ", " + e.getEntity().getLocation().getZ() + " because invalid CustomName");
								e.getEntity().remove();
							}
						}
					}
					if (!(ent.getCustomName() instanceof String)) {
						if (e.getEntity().getType() == EntityType.PHANTOM) {
							int rand = (int) (Math.random() * 7);
							if (rand != 4) {
								e.getEntity().remove();
							}
						}
						int level = 0;
						int terms = 0;
						//if (ent.getNearbyEntities(64, 32, 64) instanceof List<?>) {
						/*int max = -1;
						int min = 101;
						int maxmod = 0;
						int minmod = 0;*/
                        try {
                        	List<Entity> entslist = ent.getNearbyEntities(100, 64, 100);
                        	if (entslist.size() > 0) {
								for (Entity en : entslist) {
									if (en instanceof Player) {
										Player p = (Player) en;
										if (p.getGameMode() == GameMode.SURVIVAL) {
											int lvl = Main.getInstance().getLevelMap().get(p.getUniqueId());
											terms++;
											level += lvl;
											/*if (lvl > max) {
												max = lvl;
											}
											if (lvl < min) {
												min = lvl;
											}
											if (lvl < max) {
												maxmod+=((int) Math.ceil(lvl/10) + 5);
											}
											if (lvl > min) {
												minmod+=((int) Math.round(lvl/10) + 5);
											}*/
										}
									}
								}
							}
                        } catch (Exception ex) {
                            level = 1;
                            Main.so("&c&lERROR: &fException upon spawning of entity. (level defaulted to 1) -> for " + e.getEntity().getType().toString() + " at location " + e.getEntity().getLocation().getX() + ", " + e.getEntity().getLocation().getZ());
                            //ex.printStackTrace();
                        }
                        /*max+=maxmod;
                        min+=minmod;
                        if (terms > 1) {
                        	int dif = Math.abs(max - min);
							if (dif > 10 && dif <= 20) {
								terms++;
							}
							if (dif > 20 && dif <= 40) {
								terms+=2;
							}
							if (dif > 40 && dif <= 60) {
								terms+=3;
							}
							if (dif > 60 && dif <= 80) {
								terms+=4;
							}
							if (dif > 80 && dif <= 100) {
								terms+=5;
							}
						}*/
						if (terms > 0) {
							level/=terms;
						}
						if (level == 0) {
							level = 1;
						}
						level = minMax(e.getEntity(), level);
						setLvldHP(ent, level);
						/*double hp = ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
						//double hpper = level * hp * 0.10;
						double hpmod = main.gn().getMobHp(level) * hp;
						//double hpmod = (10 * (hp/20.0)) * level * (Math.random() * 0.2 + 1);
						double admod = main.gn().getMobDmg(level, 1);

						if (ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
							double ad = ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue();
							admod = main.gn().getMobDmg(level, ad);
						}
						if (ent.getType() == EntityType.SLIME || ent.getType() == EntityType.MAGMA_CUBE) {
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
							ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hpmod);
							ent.setHealth(hpmod);
						}*/

					}
				}
			}
			}
		}.runTaskLaterAsynchronously(main, 1L);
	}
	
	@EventHandler
	public void bonusRegen (EntityRegainHealthEvent e) {
		if (e.getEntity() instanceof LivingEntity) {
			if (e.getEntity().isDead()) {
				e.setCancelled(true);
			} else {
				LivingEntity ent = (LivingEntity) e.getEntity();
				double hp = ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
				if (e.getRegainReason() == RegainReason.REGEN || e.getRegainReason() == RegainReason.SATIATED) {
					e.setAmount(hp * 0.02);
				} else if (e.getRegainReason() == RegainReason.MAGIC_REGEN) {
					e.setAmount((e.getAmount() / 1) * hp * 0.04);
				} else if (e.getRegainReason() == RegainReason.MAGIC) {
					e.setAmount((e.getAmount() / 4) * hp * 0.1);
				} else if (e.getRegainReason() == RegainReason.EATING) {
					e.setAmount(e.getAmount() * 5 + hp * 0.04);
				} else if (e.getRegainReason() == RegainReason.ENDER_CRYSTAL) {
					e.setAmount(hp * 0.005);
				} else if (e.getRegainReason() == RegainReason.WITHER) {
					e.setAmount(hp * 0.005);
				} else if (e.getRegainReason() == RegainReason.WITHER_SPAWN) {
					e.setAmount(5);
				}
				/*Hologram damageHolo = HologramsAPI.createHologram(main, new Location(e.getEntity().getWorld(), e.getEntity().getLocation().getX(), e.getEntity().getLocation().getY() + e.getEntity().getHeight() + 0.3, e.getEntity().getLocation().getZ()));
				DecimalFormat df = new DecimalFormat("#.##");
				damageHolo.appendTextLine(Main.color("&a&l+" + df.format(e.getAmount())));
				new BukkitRunnable() {
					public void run() {
						damageHolo.delete();
					}
				}.runTaskLater(main, 20L);
				if (e.getEntity() instanceof Player) {
					Main.sendHp((Player) e.getEntity());
				}*/
			}
		}
	}
	//Healing should be main method of health increase
}
