package com.core.java.rpgbase.player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.core.java.rpgbase.entities.PlayerList;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import com.core.java.enchantments.Serration;
import com.core.java.essentials.Main;

import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.NBTTagDouble;
import net.minecraft.server.v1_14_R1.NBTTagInt;
import net.minecraft.server.v1_14_R1.NBTTagList;
import net.minecraft.server.v1_14_R1.NBTTagString;

public class Weapons  implements Listener {
	
	private Main main = Main.getInstance();

	@EventHandler (priority = EventPriority.LOWEST)
	public void onClick (PlayerInteractEvent e) {
		new BukkitRunnable() {
			public void run() {
				updateInventory(e.getPlayer());
			}
		}.runTaskLater(main, 1L);
	}
	
	@EventHandler
	public void onSwap (PlayerItemHeldEvent e) {
		new BukkitRunnable() {
			public void run() {
				updateInventory(e.getPlayer());
			}
		}.runTaskLater(main, 1L);
	}
	
	@EventHandler
	public void onPickup (EntityPickupItemEvent e) {
		if (e.getEntity() instanceof Player) {
			new BukkitRunnable() {
				public void run() {
					updateInventory((Player) e.getEntity());
				}
			}.runTaskLater(main, 1L);
		}
	}
	
	@EventHandler
	public void onCraft (CraftItemEvent e) {
		for (HumanEntity ent : e.getInventory().getViewers()) {
			if (ent instanceof Player) {
				new BukkitRunnable() {
					public void run() {
						updateInventory((Player) ent);
					}
				}.runTaskLater(main, 1L);
			}
		}
	}
	
	public static boolean isWeapon(ItemStack i) {
		String s = i.getType().toString().toLowerCase();
		return (s.contains("sword") || s.contains("axe") || s.contains("bow") || s.contains("trident"));
	}
	
	public static ItemStack fixItem(ItemStack i) {
			if (i != null && i.getType() != null && i.getType() != Material.AIR) {
				if (!i.getItemMeta().hasLore() || !i.getItemMeta().getLore().toString().contains("Damage")) {
					if (i.getType() != null) {
						String s = i.getType().toString().toLowerCase();
						Damageable dura = (Damageable) i.getItemMeta();
						int d = dura.getDamage();
						if (s.contains("sword")) {
							double ogdmg = 5;
							if (s.contains("diamond")) {
								ogdmg = 300;
							}
							if (s.contains("iron")) {
								ogdmg = 280;
							}
							if (s.contains("stone")) {
								ogdmg = 250;
							}
							if (s.contains("wood")) {
								ogdmg = 150;
							}
							if (s.contains("gold")) {
								ogdmg = 310;
							}
							net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(i);
							NBTTagCompound itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
							NBTTagList modifiers = new NBTTagList();
							NBTTagCompound itemAS = new NBTTagCompound();
							itemAS.set("AttributeName", new NBTTagString("generic.attackDamage"));
							itemAS.set("Name", new NBTTagString("generic.attackDamage"));
							itemAS.set("Amount", new NBTTagDouble(0));
							modifiers.add(itemAS);
								
							itemTagC.set("AttributeModifiers", modifiers);
								
							nmsStack.setTag(itemTagC);
							ItemStack nItem = CraftItemStack.asBukkitCopy(nmsStack);
							
							ItemMeta meta = nItem.getItemMeta();
							List<String> lore = new ArrayList<String>();
							lore.add(Main.color("&8« &8Common &7Tier Weapon &8»"));
							lore.add(Main.color(""));
							lore.add(Main.color("&6Level:&7 0"));
							lore.add(Main.color("&6Damage: &7" + ogdmg));
							lore.add(Main.color("&6Attack Speed: &72.4"));
							lore.add(Main.color(""));
							meta.setLore(lore);
							((Damageable) meta).setDamage(d);
							nItem.setItemMeta(meta);
							return nItem;
						} else if (s.contains("axe")) {
							double ogdmg = 8;
							if (s.contains("diamond")) {
								ogdmg = 600;
							}
							if (s.contains("iron")) {
								ogdmg = 580;
							}
							if (s.contains("stone")) {
								ogdmg = 400;
							}
							if (s.contains("wood")) {
								ogdmg = 300;
							}
							if (s.contains("gold")) {
								ogdmg = 610;
							}
							net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(i);
							NBTTagCompound itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
							NBTTagList modifiers = new NBTTagList();
							NBTTagCompound itemAS = new NBTTagCompound();
							itemAS.set("AttributeName", new NBTTagString("generic.attackDamage"));
							itemAS.set("Name", new NBTTagString("generic.attackDamage"));
							itemAS.set("Amount", new NBTTagDouble(0));
							modifiers.add(itemAS);
								
							itemTagC.set("AttributeModifiers", modifiers);
								
							nmsStack.setTag(itemTagC);
							ItemStack nItem = CraftItemStack.asBukkitCopy(nmsStack);
								
							ItemMeta meta = nItem.getItemMeta();
							List<String> lore = new ArrayList<String>();
							lore.add(Main.color("&8« &8Common &7Tier Weapon &8»"));
							lore.add(Main.color(""));
							lore.add(Main.color("&6Level:&7 0"));
							lore.add(Main.color("&6Damage: &7" + ogdmg));
							lore.add(Main.color("&6Attack Speed: &71.2"));
							lore.add(Main.color(""));
							meta.setLore(lore);
							((Damageable) meta).setDamage(d);
							nItem.setItemMeta(meta);
							return nItem;
						} else if (s.contains("crossbow")) {
							double ogdmg = 400;
							net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(i);
							NBTTagCompound itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();

							nmsStack.setTag(itemTagC);
							ItemStack nItem = CraftItemStack.asBukkitCopy(nmsStack);
							ItemStack it = nItem;
							ItemMeta meta = it.getItemMeta();
							List<String> lore = new ArrayList<String>();
							lore.add(Main.color("&8« Common &7Tier Weapon &8»"));
							lore.add(Main.color(""));
							lore.add(Main.color("&6Level:&7 0"));
							lore.add(Main.color("&6Ranged Damage: &7" + ogdmg));
							lore.add(Main.color(""));
							meta.setLore(lore);
							((Damageable) meta).setDamage(d);
							it.setItemMeta(meta);
							return it;
						} else if (s.contains("bow")) {
							double ogdmg = 300;
							net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(i);
							NBTTagCompound itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();

							nmsStack.setTag(itemTagC);
							ItemStack nItem = CraftItemStack.asBukkitCopy(nmsStack);
							ItemStack it = nItem;
							ItemMeta meta = it.getItemMeta();
							List<String> lore = new ArrayList<String>();
							lore.add(Main.color("&8« Common &7Tier Weapon &8»"));
							lore.add(Main.color(""));
							lore.add(Main.color("&6Level:&7 0"));
							lore.add(Main.color("&6Ranged Damage: &7" + ogdmg));
							lore.add(Main.color(""));
							meta.setLore(lore);
							((Damageable) meta).setDamage(d);
							it.setItemMeta(meta);
							return it;
						} else if (s.contains("trident")) {
							double ogdmg = 800;
							net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(i);
							NBTTagCompound itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
							NBTTagList modifiers = new NBTTagList();
							NBTTagCompound itemAS = new NBTTagCompound();
							itemAS.set("AttributeName", new NBTTagString("generic.attackDamage"));
							itemAS.set("Name", new NBTTagString("generic.attackDamage"));
							itemAS.set("Amount", new NBTTagDouble(0));
							modifiers.add(itemAS);
								
							itemTagC.set("AttributeModifiers", modifiers);
								
							nmsStack.setTag(itemTagC);
							ItemStack nItem = CraftItemStack.asBukkitCopy(nmsStack);
								
							ItemMeta meta = nItem.getItemMeta();
							List<String> lore = new ArrayList<String>();
							lore.add(Main.color("&e« &eUnique &7Tier Weapon &e»"));
							lore.add(Main.color(""));
							lore.add(Main.color("&6Level:&7 0"));
							lore.add(Main.color("&6Damage: &7" + ogdmg));
							lore.add(Main.color("&6Attack Speed: &70.4"));
							lore.add(Main.color(""));
							meta.setLore(lore);
							((Damageable) meta).setDamage(d);
							nItem.setItemMeta(meta);
							return nItem;
						}
					}
				}
			}
			return i;
	}
	
	public static void updateInventory (Player p) {
		for (int i = 0; i < p.getInventory().getContents().length; i++) {
			if (p.getInventory().getItem(i) instanceof ItemStack) {
				ItemStack it = p.getInventory().getItem(i);
				if (it != null && it.getType() != null && isWeapon(it) && isNotUpdated(it)) {
					p.getInventory().setItem(i, fixItem(it));
				}
			}
		}
		Main.getInstance().updateAttackSpeed(p);
		p.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(5 * Main.getInstance().getLevel(p) + 1);
	}
	
	public static boolean isNotUpdated(ItemStack i) {
		return !(i != null && i.hasItemMeta() && i.getItemMeta().hasLore() && i.getItemMeta().getLore().toString().toLowerCase().contains("level"));
	}
	
	/*@EventHandler
	public void asUpdater (PlayerJoinEvent e) {
		main.getACMap().put(e.getPlayer().getUniqueId(), 1.0F);
	}
	
	@EventHandler
	public void asUpdaterCleanup (PlayerQuitEvent e) {
		if (main.getACMap().containsKey(e.getPlayer().getUniqueId())) {
			main.getACMap().remove(e.getPlayer().getUniqueId());
		}
	}*/

	/*@EventHandler (priority = EventPriority.HIGH)
	public void fireworkDmg (FireworkExplodeEvent e) {
		if (e.getEntity().isCustomNameVisible() && e.getEntity().getCustomName() != null) {
			double dmg = Double.valueOf(e.getEntity().getCustomName());
			for (Entity en : e.getEntity().getNearbyEntities(3, 3, 3)) {
				if (en instanceof LivingEntity) {
					LivingEntity ent = (LivingEntity) e.getEntity();
					double hp = ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
					((LivingEntity)en).damage(500 + dmg);
				}
			}
		}
	}*/

	@EventHandler (priority = EventPriority.LOW)
	public void bowDmg (EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Projectile) {
			Projectile a = (Projectile) e.getDamager();
			if (a.getShooter() instanceof Player) {
				if (a.getCustomName() instanceof String) {
					double dmg = Double.valueOf(a.getCustomName());
					if (dmg == -50 || dmg == -100) {
						e.setDamage(0);
					} else {
						e.setDamage(dmg);
					}
				}
			} else if (a.getShooter() instanceof Entity) {
				Entity ent = (Entity) a.getShooter();
				int level = 1;
				if (ent.isCustomNameVisible() && ent.getCustomName() != null && Integer.valueOf(ChatColor.stripColor(ent.getCustomName()).replaceAll("\\D+","")) instanceof Integer) {
					level = Integer.valueOf(ChatColor.stripColor(ent.getCustomName()).replaceAll("\\D+",""));
				}
				double mod = 1.0;
				if (e.getDamager() instanceof ShulkerBullet) {
					mod = 0.5;
				}
				if (e.getDamager() instanceof Fireball) {
					mod = 2.0;
					e.getEntity().setFireTicks(100);
				}
				if (e.getDamager() instanceof SmallFireball) {
					mod = 0.1;
					e.getEntity().setFireTicks(100);
				}
				if (e.getDamager() instanceof DragonFireball) {
					mod = 0.3;
				}
				if (e.getDamager() instanceof WitherSkull) {
					if (e.getDamage() <= 15) {
						e.setDamage(main.gn().getMobProjectileDmg(level, e.getDamage()));
					}
				} else {
					e.setDamage(main.gn().getMobProjectileDmg(level, e.getDamage()) * mod);
					//e.setDamage((150 + 0.00025 * e.getDamage() * Math.pow(level+35, 3)) * mod);
				}
			}
		}
	}
	
	@EventHandler
	public void projDmgSetup (ProjectileLaunchEvent e) {
		if (e.getEntity() instanceof Trident) {
			Trident trident = (Trident) e.getEntity();
			if (trident.getShooter() instanceof Player) {
				Player p = (Player) trident.getShooter();
				double dmg = getWeaponAttackDamage(p);
				ItemStack i = p.getInventory().getItemInMainHand();
				if (i.containsEnchantment(Enchantment.IMPALING)) {
					if (e.getEntity().getType() == EntityType.PLAYER || e.getEntity().getType() == EntityType.GUARDIAN || e.getEntity().getType() == EntityType.ELDER_GUARDIAN || e.getEntity().getType() == EntityType.DOLPHIN || e.getEntity().getType() == EntityType.COD || e.getEntity().getType() == EntityType.PUFFERFISH || e.getEntity().getType() == EntityType.TROPICAL_FISH || e.getEntity().getType() == EntityType.SALMON || e.getEntity().getType() == EntityType.SQUID || e.getEntity().getType() == EntityType.TURTLE) {
						dmg+=(100 * i.getEnchantmentLevel(Enchantment.IMPALING));
					}
				}
				e.getEntity().setCustomName(String.valueOf(dmg));
			}
		}
	}
	
	@EventHandler
	public void bowDmgSetup (EntityShootBowEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			double dmg = getRangedAttackDamage(p);
			if (e.getBow().containsEnchantment(Enchantment.ARROW_DAMAGE)) {
				int level = e.getBow().getEnchantmentLevel(Enchantment.ARROW_DAMAGE);
				dmg+=(level * 50);
			}
			dmg = dmg * (2 * e.getForce());
			if (e.getArrowItem().getType() == Material.TIPPED_ARROW) {
				if (e.getArrowItem().hasItemMeta()) {
					PotionMeta meta = (PotionMeta) e.getArrowItem().getItemMeta();
					if (meta.getBasePotionData().getType() == PotionType.INSTANT_HEAL) {
						if (meta.getBasePotionData().isUpgraded()) {
							dmg = -100;
						} else {
							dmg = -50;
						}
					}
					if (meta.getBasePotionData().getType() == PotionType.INSTANT_DAMAGE) {
						int amp = 1;
						if (meta.getBasePotionData().isUpgraded()) {
							amp = 2;
						}
						dmg = dmg + (0.15 * dmg) * amp;
					}
				}
			}
			e.getProjectile().setCustomName(String.valueOf(dmg));
		}
	}
	
	public static boolean canUseWeapon(Player p) {
		if (p.getInventory().getItemInMainHand() != null) {
			ItemStack i = p.getInventory().getItemInMainHand();
			if (i.hasItemMeta() && i.getItemMeta().hasLore() && i.getItemMeta().getLore().toString().contains("Level")) {
				int index = 0;
				for (String s : i.getItemMeta().getLore()) {
					if (s.contains("Level")) {
						break;
					} else {
						index++;
					}
				}
				String ad = i.getItemMeta().getLore().get(index);
				ad = ChatColor.stripColor(ad);
				ad = ad.replace("Level: ", "");
				int level = Main.getInstance().getLevel(p);
				if (Integer.valueOf(ad) <= level) {
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static double getWeaponAttackDamage(Player p) {
		if (p.getInventory().getItemInMainHand() != null) {
			ItemStack i = p.getInventory().getItemInMainHand();
			if (i.hasItemMeta() && i.getItemMeta().hasLore() && i.getItemMeta().getLore().toString().contains("Damage")) {
				int index = 0;
				for (String s : i.getItemMeta().getLore()) {
					if (s.contains("Ranged")) {
						return 0.0;
					}
					if (s.contains("Damage")) {
						break;
					} else {
						index++;
					}
				}
				if (index < i.getItemMeta().getLore().size()) {
					String ad = i.getItemMeta().getLore().get(index);
					ad = ChatColor.stripColor(ad);
					ad = ad.replace("Damage: ", "");
					if (Double.valueOf(ad) instanceof Double) {
						return (Double.valueOf(ad) + p.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue()) * (1 + Main.getInstance().getAdMap().get(p.getUniqueId()));
					}
				}
			}
		}
		return 0;
	}

	public static double getRangedAttackDamage(Player p) {
		if (p.getInventory().getItemInMainHand() != null) {
			ItemStack i = p.getInventory().getItemInMainHand();
			if (i.hasItemMeta() && i.getItemMeta().hasLore() && i.getItemMeta().getLore().toString().contains("Ranged Damage")) {
				int index = 0;
				for (String s : i.getItemMeta().getLore()) {
					if (s.contains("Ranged Damage")) {
						break;
					} else {
						index++;
					}
				}
				String ad = i.getItemMeta().getLore().get(index);
				ad = ChatColor.stripColor(ad);
				ad = ad.replace("Ranged Damage: ", "");
				if (Double.valueOf(ad) instanceof Double) {
					return Double.valueOf(ad) * (1 + (Main.getInstance().getAdMap().get(p.getUniqueId())));
				}
			}
		}
		return 0;
	}

	public static double getCrit(Player p) {
		if (p.getInventory().getItemInMainHand() != null) {
			ItemStack i = p.getInventory().getItemInMainHand();
			if (i.hasItemMeta() && i.getItemMeta().hasLore() && i.getItemMeta().getLore().toString().contains("Crit Chance")) {
				int index = 0;
				for (String s : i.getItemMeta().getLore()) {
					if (s.contains("Crit Chance")) {
						break;
					} else {
						index++;
					}
				}
				String ad = i.getItemMeta().getLore().get(index);
				ad = ChatColor.stripColor(ad);
				ad = ad.replace("%", "");
				ad = ad.replace("Crit Chance: ", "");
				if (Double.valueOf(ad) instanceof Double) {
					return Double.valueOf(ad) * 0.01;
				}
			}
		}
		return 0;
	}

	public static double getCritDamage(Player p) {
		if (p.getInventory().getItemInMainHand() != null) {
			ItemStack i = p.getInventory().getItemInMainHand();
			if (i.hasItemMeta() && i.getItemMeta().hasLore() && i.getItemMeta().getLore().toString().contains("Crit Damage")) {
				int index = 0;
				for (String s : i.getItemMeta().getLore()) {
					if (s.contains("Crit Damage")) {
						break;
					} else {
						index++;
					}
				}
				String ad = i.getItemMeta().getLore().get(index);
				ad = ChatColor.stripColor(ad);
				ad = ad.replace("%", "");
				ad = ad.replace("Crit Damage: ", "");
				if (Double.valueOf(ad) instanceof Double) {
					return Double.valueOf(ad) * 0.01;
				}
			}
		}
		return 0;
	}

	@EventHandler (priority = EventPriority.HIGHEST)
	public void weaponAttributes (EntityDamageByEntityEvent e) {
		if (e.getDamage() <= 0.01) {
			return;
		}
		if (e.getCause() == DamageCause.ENTITY_ATTACK || e.getCause() == DamageCause.ENTITY_SWEEP_ATTACK || e.getCause() == DamageCause.PROJECTILE) {
			if (e.getDamager() instanceof Player) {
				Player p = (Player) e.getDamager();
				if (getCrit(p) > 0) {
					double rand = Math.random();
					if (rand <= getCrit(p)) {
						if (getCritDamage(p) > 0) {
							e.setDamage(e.getDamage() * (1 + getCritDamage(p)));
							e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0F, 1.0F);
							e.getEntity().getWorld().spawnParticle(Particle.CRIT, e.getEntity().getLocation(), 10);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void bonusDmg (EntityDamageByEntityEvent e) {
		if (e.getDamage() <= 0.01) {
			return;
		}
		if (e.getCause() == DamageCause.ENTITY_ATTACK || e.getCause() == DamageCause.ENTITY_SWEEP_ATTACK) {
			if (e.getDamager() instanceof Player) {
				double sweep = 1.0;
				if (e.getCause() == DamageCause.ENTITY_SWEEP_ATTACK) {
					sweep = 0.2;
				}
				Player p = (Player) e.getDamager();
				double beforedmg = e.getDamage();
				double fulldmg = p.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue();
				double ratio = beforedmg/fulldmg;
				if (!canUseWeapon(p)) {
					e.setCancelled(true);
					Main.msg(p, "&cYou do not meet this item's level requirements.");
					return;
				}
				e.setDamage(getWeaponAttackDamage((Player) e.getDamager()) + 4);
				if (Double.valueOf(getWeaponAttackDamage(p)) instanceof Double) {
					ItemStack i = p.getInventory().getItemInMainHand();
					if (i.containsEnchantment(Enchantment.DAMAGE_ALL)) {
						int level = i.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
						e.setDamage(e.getDamage() + 50 * i.getEnchantmentLevel(Enchantment.DAMAGE_ALL));

					}
					if (i.containsEnchantment(Enchantment.SWEEPING_EDGE)) {
						if (e.getCause() == DamageCause.ENTITY_SWEEP_ATTACK) {
							int level = i.getEnchantmentLevel(Enchantment.SWEEPING_EDGE);
							e.setDamage(e.getDamage() + 50 * level);
						}
					}
					if (i.containsEnchantment(Serration.enchantment)) {
						int level = i.getEnchantmentLevel(Serration.enchantment);
						e.setDamage(e.getDamage() + 100 * level);
					}
					if (i.containsEnchantment(Enchantment.DAMAGE_UNDEAD)) {
						if (e.getEntity().getType() == EntityType.ZOMBIE || e.getEntity().getType() == EntityType.SKELETON || e.getEntity().getType() == EntityType.PHANTOM || e.getEntity().getType() == EntityType.SKELETON_HORSE || e.getEntity().getType() == EntityType.STRAY || e.getEntity().getType() == EntityType.HUSK || e.getEntity().getType() == EntityType.DROWNED || e.getEntity().getType() == EntityType.PIG_ZOMBIE || e.getEntity().getType() == EntityType.WITHER_SKELETON || e.getEntity().getType() == EntityType.WITHER) {
							e.setDamage(e.getDamage() + 100 * i.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD));
						}
					}
					if (i.containsEnchantment(Enchantment.DAMAGE_ARTHROPODS)) {
						if (e.getEntity().getType() == EntityType.SPIDER || e.getEntity().getType() == EntityType.CAVE_SPIDER || e.getEntity().getType() == EntityType.SILVERFISH || e.getEntity().getType() == EntityType.ENDERMITE) {
							e.setDamage(e.getDamage() + 100 * i.getEnchantmentLevel(Enchantment.DAMAGE_ALL));
						}
					}
					if (i.containsEnchantment(Enchantment.IMPALING)) {
						if (e.getEntity().getType() == EntityType.PLAYER || e.getEntity().getType() == EntityType.GUARDIAN || e.getEntity().getType() == EntityType.ELDER_GUARDIAN || e.getEntity().getType() == EntityType.DOLPHIN || e.getEntity().getType() == EntityType.COD || e.getEntity().getType() == EntityType.PUFFERFISH || e.getEntity().getType() == EntityType.TROPICAL_FISH || e.getEntity().getType() == EntityType.SALMON || e.getEntity().getType() == EntityType.SQUID || e.getEntity().getType() == EntityType.TURTLE) {
							e.setDamage(e.getDamage() + 100 * i.getEnchantmentLevel(Enchantment.IMPALING));
						}
					}
				}
				/*try {
			        float asp = main.getAC(p);
			        double as = main.getAttackSpeed(p);
			        if (as <= 3.0) {
				        if (asp >= 0.9) {
				        	
				        } else if (asp >= 0.7) { 
				        	e.setDamage(e.getDamage() * Math.pow(asp, 1.2));
				        } else if ( asp >= 0.4) {
				        	e.setDamage(e.getDamage() * Math.pow(asp, 1.5));
				        } else {
				        	e.setDamage(e.getDamage() * Math.pow(asp, 2));
				        }
			        } else if (as <= 5.0) {
				        if (asp >= 0.8) {
				        	
				        } else if (asp >= 0.65) { 
				        	e.setDamage(e.getDamage() * Math.pow(asp, 1.3));
				        } else if ( asp >= 0.3) {
				        	e.setDamage(e.getDamage() * Math.pow(asp, 1.6));
				        } else {
				        	e.setDamage(e.getDamage() * Math.pow(asp, 2.4));
				        }
			        } else if (as <= 8) {
				        if (asp >= 0.7) {
				        	
				        } else if (asp >= 0.5) { 
				        	e.setDamage(e.getDamage() * Math.pow(asp, 1.5));
				        } else if ( asp >= 0.2) {
				        	e.setDamage(e.getDamage() * Math.pow(asp, 2));
				        } else {
				        	e.setDamage(e.getDamage() * Math.pow(asp, 3));
				        }
			        } else if (as <= 12) {
				        if (asp >= 0.7) {
				        	
				        } else if (asp >= 0.4) { 
				        	e.setDamage(e.getDamage() * Math.pow(asp, 1.5));
				        } else if ( asp >= 0.15) {
				        	e.setDamage(e.getDamage() * Math.pow(asp, 2));
				        } else {
				        	e.setDamage(e.getDamage() * Math.pow(asp, 3));
				        }
			        } else {
				        if (asp >= 0.2) {
				        	
				        } else if (asp >= 0.1) { 
				        	e.setDamage(e.getDamage() * Math.pow(asp, 1.5));
				        } else {
				        	e.setDamage(e.getDamage() * Math.pow(asp, 3));
				        }
			        }
				} catch (Exception ex) {
					ex.printStackTrace();
				}*/
				if (ratio <= 0.8 && ratio > 0.5) {
					ratio = Math.pow(ratio, 2);
				} else if (ratio <= 0.5 && ratio > 0.2) {
					ratio = Math.pow(ratio, 3);
				} else if (ratio <= 0.2) {
					ratio = Math.pow(ratio, 5);
				}
				e.setDamage(e.getDamage() * sweep * ratio);
				//e.setCancelled(true);
				if (e.getEntity() instanceof LivingEntity) {
					if (main.getPManager().getPList(e.getEntity()) != null) {
						main.getPManager().getPList(e.getEntity()).addDamage(p, e.getDamage());
					} else {
						PlayerList pl = new PlayerList();
						pl.getList().put((Player) e.getDamager(), e.getDamage());
						main.getPManager().setPlayerList(e.getEntity(), pl);
					}
				}
			}
		} else if (e.getCause() == DamageCause.PROJECTILE) {
			Player p;
			Projectile a = (Projectile) e.getDamager();
			if (a.getShooter() instanceof Player) {
				p = (Player) a.getShooter();
			} else {
				return;
			}
			if (e.getEntity() instanceof LivingEntity) {
				if (main.getPManager().getPList(e.getEntity()) != null) {
					main.getPManager().getPList(e.getEntity()).addDamage(p, e.getDamage());
				} else {
					PlayerList pl = new PlayerList();
					pl.getList().put(p, e.getDamage());
					main.getPManager().setPlayerList(e.getEntity(), pl);
				}
			}
		}
	}

	@EventHandler (priority = EventPriority.HIGHEST)
	public void dmgHolo (EntityDamageEvent e) {
		/*if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (main.getGodMode().get(p.getUniqueId())) {
				e.setCancelled(true);
				return;
			}
		}*/
		if (e.getDamage() > 0.25) {
			if (e.getEntityType() == EntityType.ARMOR_STAND || e.getEntityType() == EntityType.PRIMED_TNT) {
				return;
			}
			if (e.getEntity() instanceof LivingEntity) {
				Hologram damageHolo = HologramsAPI.createHologram(main, new Location(e.getEntity().getWorld(), e.getEntity().getLocation().getX(), e.getEntity().getLocation().getY() + e.getEntity().getHeight() + 0.3, e.getEntity().getLocation().getZ()));
				DecimalFormat df = new DecimalFormat("#.##");
				damageHolo.appendTextLine(Main.color("&c&l-" + df.format(e.getDamage())));
				new BukkitRunnable() {
					public void run() {
						damageHolo.delete();
					}
				}.runTaskLater(main, 20L);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void hitDelay (EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			if (e.getEntity() instanceof LivingEntity) {
				LivingEntity ent = (LivingEntity) e.getEntity();
				new BukkitRunnable() {
					public void run() {
						ent.setNoDamageTicks(0);
					}
				}.runTaskLater(main, 1L);
			}
		}
	}
}
