package com.core.java.rpgbase.player;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.codingforcookies.armorequip.ArmorEquipEvent;
import com.codingforcookies.armorequip.ArmorEquipEvent.EquipMethod;
import com.core.java.essentials.Main;

import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.NBTTagDouble;
import net.minecraft.server.v1_14_R1.NBTTagList;
import net.minecraft.server.v1_14_R1.NBTTagString;

public class Armor implements Listener {
	
	public static double leatherADDefense = 1.1;
	public static double goldADDefense = 1.1;
	public static double chainADDefense = 0.9;
	public static double ironADDefense = 0.8;
	public static double diamondADDefense = 0.75;
	
	public static double leatherMagDefense = 1.1;
	public static double goldMagDefense = 0.5;
	public static double chainMagDefense = 1.45;
	public static double ironMagDefense = 1.2;
	public static double diamondMagDefense = 0.8;
	
	public static double diamondAS = -2.0;
	public static double ironAS = 0;
	public static double goldenAS = -1.0;
	public static double chainmailAS = 1.0;
	public static double leatherAS = 2.0;
	
	private Main plugin = Main.getInstance();
	private static double hp = Main.basehp;
	
	public static String getSet (Player p) {
		String helmet = "nothing";
		String chestplate = "nothing";
		String leggings = "nothing";
		String boots = "nothing";
		String set = "nothing";
		if (p.getInventory().getHelmet() != null) {
			helmet = p.getInventory().getHelmet().getType().toString().toLowerCase();
		}
		if (p.getInventory().getChestplate() != null) {
			chestplate = p.getInventory().getChestplate().getType().toString().toLowerCase();
		}
		if (p.getInventory().getLeggings() != null) {
			leggings = p.getInventory().getLeggings().getType().toString().toLowerCase();
		}
		if (p.getInventory().getBoots() != null) {
			boots = p.getInventory().getBoots().getType().toString().toLowerCase();
		}
		if (helmet.contains("diamond") && chestplate.contains("diamond") && leggings.contains("diamond") && boots.contains("diamond")) {
			set = "diamond";
		} else if (helmet.contains("iron") && chestplate.contains("iron") && leggings.contains("iron") && boots.contains("iron")) {
			set = "iron";
		} else if (helmet.contains("golden") && chestplate.contains("golden") && leggings.contains("golden") && boots.contains("golden")) {
			set = "golden";
		} else if (helmet.contains("chainmail") && chestplate.contains("chainmail") && leggings.contains("chainmail") && boots.contains("chainmail")) {
			set = "chainmail";
		} else if (helmet.contains("leather") && chestplate.contains("leather") && leggings.contains("leather") && boots.contains("leather")) {
			set = "leather";
		} else {
			set = "nothing";
		}
		return set;
	}
	
	@EventHandler
	public void armorDamage (EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			Main.sendHp(p);
			String set = getSet(p);
			if (e.getCause() == DamageCause.CUSTOM) {
				if (set.equals("diamond")) {
					e.setDamage(e.getDamage() * diamondMagDefense);
				} else if (set.equals("iron")) {
					e.setDamage(e.getDamage() * ironMagDefense);
				} else if (set.equals("golden")) {
					e.setDamage(e.getDamage() * goldMagDefense);
				} else if (set.equals("chainmail")) {
					e.setDamage(e.getDamage() * chainMagDefense);
				} else if (set.equals("leather")) {
					e.setDamage(e.getDamage() * leatherMagDefense);
				}
			} else if (e.getCause() == DamageCause.ENTITY_ATTACK || e.getCause() == DamageCause.ENTITY_SWEEP_ATTACK || e.getCause() == DamageCause.PROJECTILE) {
				if (set.equals("diamond")) {
					e.setDamage(e.getDamage() * diamondADDefense);
				} else if (set.equals("iron")) {
					e.setDamage(e.getDamage() * ironADDefense);
				} else if (set.equals("golden")) {
					e.setDamage(e.getDamage() * goldADDefense);
				} else if (set.equals("chainmail")) {
					e.setDamage(e.getDamage() * chainADDefense);
				} else if (set.equals("leather")) {
					e.setDamage(e.getDamage() * leatherADDefense);
				}
			}
		}
	}
	
	/*
	@EventHandler (priority = EventPriority.LOWEST)
	public void entityDamage (EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getDamager() instanceof Player) {
				return;
			} else {
				Player p = (Player) e.getEntity();
				e.setDamage(e.getDamage() + p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() * 0.03D);
			}
		}
	}*/
	
	@EventHandler
	public void noArmor (ArmorEquipEvent e) {
		if (e.getMethod() == EquipMethod.SHIFT_CLICK || e.getMethod() == EquipMethod.HOTBAR || e.getMethod() == EquipMethod.HOTBAR_SWAP) {
			if (e.getNewArmorPiece() != null) {
				net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(e.getNewArmorPiece());
				NBTTagCompound itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
				NBTTagList modifiers = new NBTTagList();
				NBTTagCompound itemC = new NBTTagCompound();
				
				itemC.set("AttributeName", new NBTTagString("generic.armor"));
				itemC.set("Name", new NBTTagString("generic.armor"));
				itemC.set("Amount", new NBTTagDouble(0));
				
				String item = e.getNewArmorPiece().toString().toLowerCase();
				
				if (item.contains("helmet") || item.contains("cap")) {
					itemC.set("Slot", new NBTTagString("head"));
				}
				if (item.contains("chestplate") || item.contains("tunic")) {
					itemC.set("Slot", new NBTTagString("chest"));
				}
				if (item.contains("leggings") || item.contains("pants")) {
					itemC.set("Slot", new NBTTagString("legs"));
				}
				if (item.contains("boots")) {
					itemC.set("Slot", new NBTTagString("feet"));
				}
				
				modifiers.add(itemC);
				itemTagC.set("AttributeModifiers", modifiers);
				nmsStack.setTag(itemTagC);
				ItemStack nItem = CraftItemStack.asBukkitCopy(nmsStack);
				
				if (item.contains("helmet") || item.contains("cap")) {
					e.getPlayer().getInventory().setHelmet(nItem);
				}
				if (item.contains("chestplate") || item.contains("tunic")) {
					e.getPlayer().getInventory().setChestplate(nItem);
				}
				if (item.contains("leggings") || item.contains("pants")) {
					e.getPlayer().getInventory().setLeggings(nItem);
				}
				if (item.contains("boots")) {
					e.getPlayer().getInventory().setBoots(nItem);
				}
				
				e.getNewArmorPiece().setAmount(0);
			}
		}
		Player p = (Player) e.getPlayer();
		String set = getSet(p);
		if (e.getOldArmorPiece() != null) {
			if (e.getOldArmorPiece().getType() != Material.AIR) {
				if (e.getNewArmorPiece() == null || e.getNewArmorPiece().getType() == Material.AIR) {
					set = "nothing";
				}
			}
		}
		
		updateSet(p, set);
		
	}
	
	public static void updateSet(Player p, String set) {
		if (p.getHealth() > 0) {
			double pAS = Double.valueOf(Main.getValue(p, "Attack Speed"));
			double hpPercent = p.getHealth() / p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
			if (set.equals("diamond")) {
				p.setWalkSpeed(0.15F);
				p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(pAS + diamondAS);
				p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.6);
				p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp + Main.diamondA);
				if (p.getHealth() >= hp + Main.diamondA) {
					p.setHealth(hp + Main.diamondA);
				} else {
					p.setHealth(hpPercent * p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
				}
			} else if (set.equals("iron")) {
				p.setWalkSpeed(0.18F);
				p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(pAS + ironAS);
				p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.3);
				p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp + Main.ironA);
				if (p.getHealth() >= hp + Main.ironA) {
					p.setHealth(hp + Main.ironA);
				} else {
					p.setHealth(hpPercent * p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
				}
			} else if (set.equals("golden")) {
				p.setWalkSpeed(0.19F);
				p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(pAS + goldenAS);
				p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.1);
				p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp + Main.goldenA);
				if (p.getHealth() >= hp + Main.goldenA) {
					p.setHealth(hp + Main.goldenA);
				} else {
					p.setHealth(hpPercent * p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
				}
			} else if (set.equals("chainmail")) {
				p.setWalkSpeed(0.22F);
				p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(pAS + chainmailAS);
				p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.0);
				p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp + Main.chainmailA);
				if (p.getHealth() >= hp + Main.chainmailA) {
					p.setHealth(hp + Main.chainmailA);
				} else {
					p.setHealth(hpPercent * p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
				}
			} else if (set.equals("leather")) {
				p.setWalkSpeed(0.26F);
				p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(pAS + leatherAS);
				p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.0);
				p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp + Main.leatherA);
				if (p.getHealth() >= hp + Main.leatherA) {
					p.setHealth(hp + Main.leatherA);
				} else {
					p.setHealth(hpPercent * p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
				}
			} else {
				if (p.getWalkSpeed() != 0.2F) {
					p.setWalkSpeed(0.2F);
				}
				p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(6.0);
				p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.0);
				p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
				if (p.getHealth() >= hp) {
					p.setHealth(hp);
				} else {
					p.setHealth(hpPercent * p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
				}
			}
			p.setHealthScale(20);
		}
	}
	
	@EventHandler
	public void armorClick (InventoryClickEvent e) {
		if (e.getSlotType() == SlotType.ARMOR) {
			if (!e.getClick().isShiftClick()) {
				e.setCancelled(true);
				Main.msg((Player) e.getWhoClicked(), "&cERROR: Please use Shift-Click to change Armor!");
			}
		}
	}
	
}
