package com.core.java;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.codingforcookies.armorequip.ArmorEquipEvent;
import com.codingforcookies.armorequip.ArmorEquipEvent.EquipMethod;

import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.NBTTagDouble;
import net.minecraft.server.v1_13_R2.NBTTagList;
import net.minecraft.server.v1_13_R2.NBTTagString;

public class Armor implements Listener {
	
	private Plugin plugin = Main.getInstance();
	
	public String getSet (Player p) {
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
			if (e.getCause() == DamageCause.MAGIC) {
				if (set.equals("diamond")) {
					e.setDamage(e.getDamage() * 0.8);
				} else if (set.equals("iron")) {
					e.setDamage(e.getDamage() * 1.2);
				} else if (set.equals("golden")) {
					e.setDamage(e.getDamage() * 0.5);
				} else if (set.equals("chainmail")) {
					e.setDamage(e.getDamage() * 1.45);
				} else if (set.equals("leather")) {
					e.setDamage(e.getDamage() * 1.1);
				}
			} else if (e.getCause() == DamageCause.ENTITY_ATTACK || e.getCause() == DamageCause.ENTITY_SWEEP_ATTACK || e.getCause() == DamageCause.PROJECTILE) {
				if (set.equals("diamond")) {
					e.setDamage(e.getDamage() * 0.75);
				} else if (set.equals("iron")) {
					e.setDamage(e.getDamage() * 0.8);
				} else if (set.equals("golden")) {
					e.setDamage(e.getDamage() * 1.1);
				} else if (set.equals("chainmail")) {
					e.setDamage(e.getDamage() * 0.9);
				} else if (set.equals("leather")) {
					e.setDamage(e.getDamage() * 1.1);
				}
			}
		}
	}
	
	@EventHandler
	public void noArmor (ArmorEquipEvent e) {
		if (e.getMethod() == EquipMethod.SHIFT_CLICK || e.getMethod() == EquipMethod.HOTBAR || e.getMethod() == EquipMethod.HOTBAR_SWAP) {
			if (e.getNewArmorPiece() != null) {
				net.minecraft.server.v1_13_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(e.getNewArmorPiece());
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
		
		double pAS = Double.valueOf(Main.getValue(p, "Attack Speed"));
		
		if (set.equals("diamond")) {
			p.setWalkSpeed(0.15F);
			p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(pAS - 2.0);
			p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.6);
			p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(80.0);
		} else if (set.equals("iron")) {
			p.setWalkSpeed(0.18F);
			p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(pAS);
			p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.3);
			p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(70.0);
		} else if (set.equals("golden")) {
			p.setWalkSpeed(0.19F);
			p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(pAS - 1.0);
			p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.1);
			p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(50.0);
		} else if (set.equals("chainmail")) {
			p.setWalkSpeed(0.22F);
			p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(pAS + 1.0);
			p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.0);
			p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(60.0);
		} else if (set.equals("leather")) {
			p.setWalkSpeed(0.26F);
			p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(pAS + 2.0);
			p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.0);
			p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40.0);
		} else {
			if (p.getWalkSpeed() != 0.2F) {
				p.setWalkSpeed(0.2F);
			}
			p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(6.0);
			p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.0);
			p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40.0);
		}
		p.setHealthScale(20);
	}
	/*
	@EventHandler(priority = EventPriority.LOWEST)
	public void armorBonuses (ArmorEquipEvent e) {
				Player p = (Player) e.getPlayer();
				String set = getSet(p);
				Main.so(set);
				if (set.equals("diamond")) {
					p.setWalkSpeed(0.15F);
					p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0);
					p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.6);
					p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40.0);
				} else if (set.equals("iron")) {
					p.setWalkSpeed(0.18F);
					p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(6.0);
					p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.3);
					p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(32.0);
				} else if (set.equals("golden")) {
					p.setWalkSpeed(0.19F);
					p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(5.0);
					p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.1);
					p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(24.0);
				} else if (set.equals("chainmail")) {
					p.setWalkSpeed(0.22F);
					p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(7.0);
					p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.0);
					p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(26.0);
				} else if (set.equals("leather")) {
					p.setWalkSpeed(0.26F);
					p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(8.0);
					p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.0);
					p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
				} else {
					if (p.getWalkSpeed() != 0.2F) {
						p.setWalkSpeed(0.2F);
					}
					p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(6.0);
					p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.0);
					p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
				}
	}
	*/
	
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
