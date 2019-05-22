package com.core.java.rpgbase.player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Trident;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
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
				updateMainHand(e.getPlayer());
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
								ogdmg = 60;
							}
							if (s.contains("iron")) {
								ogdmg = 50;
							}
							if (s.contains("stone")) {
								ogdmg = 40;
							}
							if (s.contains("wood")) {
								ogdmg = 32;
							}
							if (s.contains("gold")) {
								ogdmg = 70;
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
							lore.add(Main.color("&6Level:&7 1"));
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
								ogdmg = 110;
							}
							if (s.contains("iron")) {
								ogdmg = 90;
							}
							if (s.contains("stone")) {
								ogdmg = 45;
							}
							if (s.contains("wood")) {
								ogdmg = 30;
							}
							if (s.contains("gold")) {
								ogdmg = 120;
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
							lore.add(Main.color("&6Level:&7 1"));
							lore.add(Main.color("&6Damage: &7" + ogdmg));
							lore.add(Main.color("&6Attack Speed: &71.2"));
							lore.add(Main.color(""));
							meta.setLore(lore);
							((Damageable) meta).setDamage(d);
							nItem.setItemMeta(meta);
							return nItem;
						} else if (s.contains("crossbow")) {
							double ogdmg = 60;
							ItemStack it = new ItemStack(Material.CROSSBOW);
							ItemMeta meta = it.getItemMeta();
							List<String> lore = new ArrayList<String>();
							lore.add(Main.color("&8« Common &7Tier Weapon &8»"));
							lore.add(Main.color(""));
							lore.add(Main.color("&6Level:&7 1"));
							lore.add(Main.color("&6Ranged Damage: &7" + ogdmg));
							lore.add(Main.color(""));
							meta.setLore(lore);
							((Damageable) meta).setDamage(d);
							it.setItemMeta(meta);
							return it;
						} else if (s.contains("bow")) {
							double ogdmg = 50;
							ItemStack it = new ItemStack(Material.BOW);
							ItemMeta meta = it.getItemMeta();
							List<String> lore = new ArrayList<String>();
							lore.add(Main.color("&8« Common &7Tier Weapon &8»"));
							lore.add(Main.color(""));
							lore.add(Main.color("&6Level:&7 1"));
							lore.add(Main.color("&6Ranged Damage: &7" + ogdmg));
							lore.add(Main.color(""));
							meta.setLore(lore);
							((Damageable) meta).setDamage(d);
							it.setItemMeta(meta);
							return it;
						} else if (s.contains("trident")) {
							double ogdmg = 150;
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
							lore.add(Main.color("&6Level:&7 1"));
							lore.add(Main.color("&6Damage: &7" + ogdmg));
							lore.add(Main.color("&6Attack Speed: &70.8"));
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
	}
	
	public static boolean isNotUpdated(ItemStack i) {
		return !(i != null && i.hasItemMeta() && i.getItemMeta().hasLore() && i.getItemMeta().getLore().toString().toLowerCase().contains("level"));
	}
	
	public static void updateMainHand (Player p) {
		if (p.getInventory().getItemInMainHand() != null) {
			ItemStack i = p.getInventory().getItemInMainHand();
			if (i.getType() != Material.AIR) {
				if (!i.getItemMeta().hasLore() || !i.getItemMeta().getLore().toString().contains("Damage")) {
					if (i.getType() != null) {
						String s = i.getType().toString().toLowerCase();
						Damageable dura = (Damageable) i.getItemMeta();
						int d = dura.getDamage();
						if (s.contains("sword")) {
							double ogdmg = 5;
							if (s.contains("diamond")) {
								ogdmg = 60;
							}
							if (s.contains("iron")) {
								ogdmg = 50;
							}
							if (s.contains("stone")) {
								ogdmg = 40;
							}
							if (s.contains("wood")) {
								ogdmg = 32;
							}
							if (s.contains("gold")) {
								ogdmg = 70;
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
							lore.add(Main.color("&6Level:&7 1"));
							lore.add(Main.color("&6Damage: &7" + ogdmg));
							lore.add(Main.color("&6Attack Speed: &72.4"));
							lore.add(Main.color(""));
							meta.setLore(lore);
							((Damageable) meta).setDamage(d);
							nItem.setItemMeta(meta);
							
							p.getInventory().getItemInMainHand().setAmount(0);
							p.getInventory().setItemInMainHand(nItem);
						} else if (s.contains("axe")) {
							double ogdmg = 8;
							if (s.contains("diamond")) {
								ogdmg = 110;
							}
							if (s.contains("iron")) {
								ogdmg = 90;
							}
							if (s.contains("stone")) {
								ogdmg = 45;
							}
							if (s.contains("wood")) {
								ogdmg = 30;
							}
							if (s.contains("gold")) {
								ogdmg = 120;
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
							lore.add(Main.color("&6Level:&7 1"));
							lore.add(Main.color("&6Damage: &7" + ogdmg));
							lore.add(Main.color("&6Attack Speed: &71.2"));
							lore.add(Main.color(""));
							meta.setLore(lore);
							((Damageable) meta).setDamage(d);
							nItem.setItemMeta(meta);
							p.getInventory().getItemInMainHand().setAmount(0);
							p.getInventory().setItemInMainHand(nItem);
						} else if (s.contains("crossbow")) {
							double ogdmg = 70;
							net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(i);
							NBTTagCompound itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
							nmsStack.setTag(itemTagC);
							ItemStack it = CraftItemStack.asBukkitCopy(nmsStack);
							ItemMeta meta = it.getItemMeta();
							List<String> lore = new ArrayList<String>();
							lore.add(Main.color("&8« Common &7Tier Weapon &8»"));
							lore.add(Main.color(""));
							lore.add(Main.color("&6Level:&7 1"));
							lore.add(Main.color("&6Ranged Damage: &7" + ogdmg));
							lore.add(Main.color(""));
							meta.setLore(lore);
							((Damageable) meta).setDamage(d);
							it.setItemMeta(meta);
							p.getInventory().getItemInMainHand().setAmount(0);
							p.getInventory().setItemInMainHand(it);
						} else if (s.contains("bow")) {
							double ogdmg = 50;
							net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(i);
							NBTTagCompound itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
							nmsStack.setTag(itemTagC);
							ItemStack it = CraftItemStack.asBukkitCopy(nmsStack);
							ItemMeta meta = it.getItemMeta();
							List<String> lore = new ArrayList<String>();
							lore.add(Main.color("&8« Common &7Tier Weapon &8»"));
							lore.add(Main.color(""));
							lore.add(Main.color("&6Level:&7 1"));
							lore.add(Main.color("&6Ranged Damage: &7" + ogdmg));
							lore.add(Main.color(""));
							meta.setLore(lore);
							((Damageable) meta).setDamage(d);
							it.setItemMeta(meta);
							p.getInventory().getItemInMainHand().setAmount(0);
							p.getInventory().setItemInMainHand(it);
						} else if (s.contains("trident")) {
							double ogdmg = 150;
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
							lore.add(Main.color("&6Level:&7 1"));
							lore.add(Main.color("&6Damage: &7" + ogdmg));
							lore.add(Main.color("&6Attack Speed: &70.8"));
							lore.add(Main.color(""));
							meta.setLore(lore);
							((Damageable) meta).setDamage(d);
							nItem.setItemMeta(meta);
							p.getInventory().getItemInMainHand().setAmount(0);
							p.getInventory().setItemInMainHand(nItem);
						}
					}
				}
			}
		}
		Main.getInstance().updateAttackSpeed(p);
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
						return Double.valueOf(ad) * (1 + Main.getInstance().getAdMap().get(p.getUniqueId()));
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
					return Double.valueOf(ad);
				}
			}
		}
		return 0;
	}
	
	@EventHandler
	public void asUpdater (PlayerJoinEvent e) {
		main.getACMap().put(e.getPlayer().getUniqueId(), 1.0F);
	}
	
	@EventHandler
	public void asUpdaterCleanup (PlayerQuitEvent e) {
		if (main.getACMap().containsKey(e.getPlayer().getUniqueId())) {
			main.getACMap().remove(e.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler (priority = EventPriority.LOW)
	public void bowDmg (EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Projectile) {
			Projectile a = (Projectile) e.getDamager();
			if (a.getShooter() instanceof Player) {
				if (a.getCustomName() instanceof String) {
					double dmg = Double.valueOf(a.getCustomName());
					e.setDamage(dmg);
				}
			} else if (a.getShooter() instanceof Entity) {
				Entity ent = (Entity) a.getShooter();
				int level = 1;
				if (ent.getCustomName() != null && Integer.valueOf(ChatColor.stripColor(ent.getCustomName()).replaceAll("\\D+","")) instanceof Integer) {
					level = Integer.valueOf(ChatColor.stripColor(ent.getCustomName()).replaceAll("\\D+",""));
				}
				double mod = 1.0;
				if (e.getDamager() instanceof ShulkerBullet) {
					mod = 1.0;
				}
				if (e.getDamager() instanceof Fireball) {
					mod = 0.7;
				}
				if (e.getDamager() instanceof SmallFireball) {
					mod = 0.8;
					e.getEntity().setFireTicks(100);
				}
				if (e.getDamager() instanceof DragonFireball) {
					mod = 0.7;
				}
				if (e.getDamager() instanceof WitherSkull) {
					mod = 0.5;
				}
				e.setDamage((20 + 0.2 * e.getDamage() * level) * mod);
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
						dmg+=(10 * i.getEnchantmentLevel(Enchantment.IMPALING));
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
				dmg+=(level * 10);
			}
			dmg = dmg * (2 * e.getForce());
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
	
	@EventHandler (priority = EventPriority.LOW)
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
				if (!canUseWeapon(p)) {
					e.setCancelled(true);
					Main.msg(p, "&cYou do not meet this item's level requirements.");
					return;
				}
				e.setDamage(e.getDamage() + getWeaponAttackDamage((Player) e.getDamager()));
				if (Double.valueOf(getWeaponAttackDamage(p)) instanceof Double) {
					ItemStack i = p.getInventory().getItemInMainHand();
					if (i.containsEnchantment(Enchantment.DAMAGE_ALL)) {
						int level = i.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
						if (level == 1) {
							e.setDamage(e.getDamage() - 1);
							e.setDamage(e.getDamage() + 10 * i.getEnchantmentLevel(Enchantment.DAMAGE_ALL));
						} else {
							e.setDamage(e.getDamage() - 1 - 0.5 * (level - 1));
							e.setDamage(e.getDamage() + 10 * i.getEnchantmentLevel(Enchantment.DAMAGE_ALL));
						}
					}
					if (i.containsEnchantment(Enchantment.SWEEPING_EDGE)) {
						if (e.getCause() == DamageCause.ENTITY_SWEEP_ATTACK) {
							int level = i.getEnchantmentLevel(Enchantment.SWEEPING_EDGE);
							e.setDamage(e.getDamage() + 10 * level);
						}
					}
					if (i.containsEnchantment(Serration.enchantment)) {
						int level = i.getEnchantmentLevel(Serration.enchantment);
						e.setDamage(e.getDamage() + 20 * level);
					}
					if (i.containsEnchantment(Enchantment.DAMAGE_UNDEAD)) {
						if (e.getEntity().getType() == EntityType.ZOMBIE || e.getEntity().getType() == EntityType.SKELETON || e.getEntity().getType() == EntityType.PHANTOM || e.getEntity().getType() == EntityType.SKELETON_HORSE || e.getEntity().getType() == EntityType.STRAY || e.getEntity().getType() == EntityType.HUSK || e.getEntity().getType() == EntityType.DROWNED || e.getEntity().getType() == EntityType.PIG_ZOMBIE || e.getEntity().getType() == EntityType.WITHER_SKELETON || e.getEntity().getType() == EntityType.WITHER) {
							e.setDamage(e.getDamage() - (2.5 * i.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD)));
							e.setDamage(e.getDamage() + 20 * i.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD));
						}
					}
					if (i.containsEnchantment(Enchantment.DAMAGE_ARTHROPODS)) {
						if (e.getEntity().getType() == EntityType.SPIDER || e.getEntity().getType() == EntityType.CAVE_SPIDER || e.getEntity().getType() == EntityType.SILVERFISH || e.getEntity().getType() == EntityType.ENDERMITE) {
							e.setDamage(e.getDamage() - (2.5 * i.getEnchantmentLevel(Enchantment.DAMAGE_ALL)));
							e.setDamage(e.getDamage() + 20 * i.getEnchantmentLevel(Enchantment.DAMAGE_ALL));
						}
					}
					if (i.containsEnchantment(Enchantment.IMPALING)) {
						if (e.getEntity().getType() == EntityType.PLAYER || e.getEntity().getType() == EntityType.GUARDIAN || e.getEntity().getType() == EntityType.ELDER_GUARDIAN || e.getEntity().getType() == EntityType.DOLPHIN || e.getEntity().getType() == EntityType.COD || e.getEntity().getType() == EntityType.PUFFERFISH || e.getEntity().getType() == EntityType.TROPICAL_FISH || e.getEntity().getType() == EntityType.SALMON || e.getEntity().getType() == EntityType.SQUID || e.getEntity().getType() == EntityType.TURTLE) {
							e.setDamage(e.getDamage() - (2.5 * i.getEnchantmentLevel(Enchantment.IMPALING)));
							e.setDamage(e.getDamage() + 10 * i.getEnchantmentLevel(Enchantment.IMPALING));
						}
					}
				}
				try {
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
				}
				e.setDamage(e.getDamage() * sweep);
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
				}.runTaskLater(main, 5L);
			}
		}
	}
}
