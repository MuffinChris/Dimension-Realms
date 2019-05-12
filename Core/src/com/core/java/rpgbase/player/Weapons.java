package com.core.java.rpgbase.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.core.java.essentials.Main;

import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.NBTTagDouble;
import net.minecraft.server.v1_14_R1.NBTTagInt;
import net.minecraft.server.v1_14_R1.NBTTagList;
import net.minecraft.server.v1_14_R1.NBTTagString;

public class Weapons  implements Listener {
	
	private Main main = Main.getInstance();

	@EventHandler
	public void onSwap (PlayerItemHeldEvent e) {
		new BukkitRunnable() {
			public void run() {
				updateMainHand(e.getPlayer());
			}
		}.runTaskLater(main, 1L);
	}
	
	public void updateMainHand (Player p) {
		if (p.getInventory().getItemInMainHand() != null) {
			ItemStack i = p.getInventory().getItemInMainHand();
			if (i.getType() != Material.AIR) {
				if (!i.getItemMeta().hasLore() || !i.getItemMeta().getLore().contains("Damage")) {
					if (i.getType() != null) {
						String s = i.getType().toString().toLowerCase();
						if (s.contains("sword")) {
							double ogdmg = 5;
							if (s.contains("diamond")) {
								ogdmg = 15;
							}
							if (s.contains("iron")) {
								ogdmg = 12;
							}
							if (s.contains("stone")) {
								ogdmg = 9;
							}
							if (s.contains("wood")) {
								ogdmg = 6;
							}
							if (s.contains("gold")) {
								ogdmg = 14;
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
							lore.add(Main.color("&7Common Tier Weapon"));
							lore.add(Main.color(""));
							lore.add(Main.color("&6Damage: &7" + ogdmg));
							lore.add(Main.color("&6Attack Speed: &73.0"));
							lore.add(Main.color(""));
							meta.setLore(lore);
							nItem.setItemMeta(meta);
							
							p.getInventory().getItemInMainHand().setAmount(0);
							p.getInventory().setItemInMainHand(nItem);
						} else if (s.contains("axe")) {
							double ogdmg = 8;
							if (s.contains("diamond")) {
								ogdmg = 24;
							}
							if (s.contains("iron")) {
								ogdmg = 19;
							}
							if (s.contains("stone")) {
								ogdmg = 14;
							}
							if (s.contains("wood")) {
								ogdmg = 9;
							}
							if (s.contains("gold")) {
								ogdmg = 23;
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
							lore.add(Main.color("&7Common Tier Weapon"));
							lore.add(Main.color(""));
							lore.add(Main.color("&6Damage: &7" + ogdmg));
							lore.add(Main.color("&6Attack Speed: &72.0"));
							lore.add(Main.color(""));
							meta.setLore(lore);
							nItem.setItemMeta(meta);
							p.getInventory().getItemInMainHand().setAmount(0);
							p.getInventory().setItemInMainHand(nItem);
						} else if (s.contains("trident")) {
							double ogdmg = 35;
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
							lore.add(Main.color("&eUnique &7Tier Weapon"));
							lore.add(Main.color(""));
							lore.add(Main.color("&6Damage: &7" + ogdmg));
							lore.add(Main.color("&6Attack Speed: &71.0"));
							lore.add(Main.color(""));
							meta.setLore(lore);
							nItem.setItemMeta(meta);
							p.getInventory().getItemInMainHand().setAmount(0);
							p.getInventory().setItemInMainHand(nItem);
						}
					}
				}
			}
		}
		main.updateAttackSpeed(p);
	}
	
	public static double getWeaponAttackDamage(Player p) {
		if (p.getInventory().getItemInMainHand() != null) {
			ItemStack i = p.getInventory().getItemInMainHand();
			if (i.hasItemMeta() && i.getItemMeta().hasLore() && i.getItemMeta().getLore().toString().contains("Damage")) {
				int index = 0;
				for (String s : i.getItemMeta().getLore()) {
					if (s.contains("Damage")) {
						break;
					} else {
						index++;
					}
				}
				String ad = i.getItemMeta().getLore().get(index);
				ad = ChatColor.stripColor(ad);
				ad = ad.replace("Damage: ", "");
				if (Double.valueOf(ad) instanceof Double) {
					return Double.valueOf(ad);
				}
			}
		}
		return 0;
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void bonusDmg (EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			e.setDamage(e.getDamage() + getWeaponAttackDamage((Player) e.getDamager()));
		}
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void onClick (PlayerInteractEvent e) {
		new BukkitRunnable() {
			public void run() {
				updateMainHand(e.getPlayer());
			}
		}.runTaskLater(main, 1L);
		/*if (e.getItem() != null) {
			if (e.getItem().getType().toString().toLowerCase().contains("sword")) { 
				net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(e.getItem());
				NBTTagCompound itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
				NBTTagList modifiers = new NBTTagList();
				NBTTagCompound itemC = new NBTTagCompound();
				NBTTagCompound itemAS = new NBTTagCompound();
					
				String s = e.getItem().getType().toString().toLowerCase();
				double ogdmg = 5;
				if (s.contains("diamond")) {
					ogdmg = 15;
				}
				if (s.contains("iron")) {
					ogdmg = 12;
				}
				if (s.contains("stone")) {
					ogdmg = 9;
				}
				if (s.contains("wood")) {
					ogdmg = 6;
				}
				if (s.contains("gold")) {
					ogdmg = 14;
				}
				if (nmsStack.getTag() == null || !(Double.valueOf(nmsStack.getTag().getDouble("Amount")) instanceof Double)) {
					itemC.set("AttributeName", new NBTTagString("generic.attackDamage"));
					itemC.set("Name", new NBTTagString("generic.attackDamage"));
					itemC.set("Amount", new NBTTagDouble(ogdmg));
					itemC.set("Slot", new NBTTagString("mainhand"));
					itemC.set("Operation", new NBTTagInt(0));
			        itemC.set("UUIDLeast", new NBTTagInt(894654));
			        itemC.set("UUIDMost", new NBTTagInt(2872));
			        
			        itemAS.set("AttributeName", new NBTTagString("generic.attackSpeed"));
					itemAS.set("Name", new NBTTagString("generic.attackSpeed"));
					itemAS.set("Amount", new NBTTagDouble(16.0));
					itemAS.set("Slot", new NBTTagString("mainhand"));
					itemAS.set("Operation", new NBTTagInt(0));
			        itemAS.set("UUIDLeast", new NBTTagInt(894654));
			        itemAS.set("UUIDMost", new NBTTagInt(2872));
			        
					modifiers.add(itemC);
					modifiers.add(itemAS);
					
					itemTagC.set("AttributeModifiers", modifiers);
					
					nmsStack.setTag(itemTagC);
					ItemStack nItem = CraftItemStack.asBukkitCopy(nmsStack);
					e.getPlayer().getInventory().getItemInMainHand().setAmount(0);
					e.getPlayer().getInventory().setItemInMainHand(nItem);
				}
			} else if (e.getItem().getType().toString().toLowerCase().contains("axe")) { 
				net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(e.getItem());
				NBTTagCompound itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
				NBTTagList modifiers = new NBTTagList();
				NBTTagCompound itemC = new NBTTagCompound();
				NBTTagCompound itemAS = new NBTTagCompound();
					
				String s = e.getItem().getType().toString().toLowerCase();
				double ogdmg = 4;
				if (s.contains("diamond")) {
					ogdmg = 22;
				}
				if (s.contains("iron")) {
					ogdmg = 18;
				}
				if (s.contains("stone")) {
					ogdmg = 14;
				}
				if (s.contains("wood")) {
					ogdmg = 10;
				}
				if (s.contains("gold")) {
					ogdmg = 21;
				}
				if (nmsStack.getTag() == null || !(Double.valueOf(nmsStack.getTag().getDouble("Amount")) instanceof Double)) {
					itemC.set("AttributeName", new NBTTagString("generic.attackDamage"));
					itemC.set("Name", new NBTTagString("generic.attackDamage"));
					itemC.set("Amount", new NBTTagDouble(ogdmg));
					itemC.set("Slot", new NBTTagString("mainhand"));
					itemC.set("Operation", new NBTTagInt(0));
			        itemC.set("UUIDLeast", new NBTTagInt(894654));
			        itemC.set("UUIDMost", new NBTTagInt(2872));
			        
			        itemAS.set("AttributeName", new NBTTagString("generic.attackSpeed"));
					itemAS.set("Name", new NBTTagString("generic.attackSpeed"));
					itemAS.set("Amount", new NBTTagDouble(-1.0));
					itemAS.set("Slot", new NBTTagString("mainhand"));
					itemAS.set("Operation", new NBTTagInt(0));
			        itemAS.set("UUIDLeast", new NBTTagInt(894654));
			        itemAS.set("UUIDMost", new NBTTagInt(2872));
			        
					modifiers.add(itemC);
					modifiers.add(itemAS);
					
					itemTagC.set("AttributeModifiers", modifiers);
					
					nmsStack.setTag(itemTagC);
					ItemStack nItem = CraftItemStack.asBukkitCopy(nmsStack);
					e.getPlayer().getInventory().getItemInMainHand().setAmount(0);
					e.getPlayer().getInventory().setItemInMainHand(nItem);
				}
			} else if (e.getItem().getType().toString().toLowerCase().contains("trident")) { 
				net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(e.getItem());
				NBTTagCompound itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
				NBTTagList modifiers = new NBTTagList();
				NBTTagCompound itemC = new NBTTagCompound();
				NBTTagCompound itemAS = new NBTTagCompound();
				double ogdmg = 35;
				if (nmsStack.getTag() == null || !(Double.valueOf(nmsStack.getTag().getDouble("Amount")) instanceof Double)) {
					itemC.set("AttributeName", new NBTTagString("generic.attackDamage"));
					itemC.set("Name", new NBTTagString("generic.attackDamage"));
					itemC.set("Amount", new NBTTagDouble(ogdmg));
					itemC.set("Slot", new NBTTagString("mainhand"));
					itemC.set("Operation", new NBTTagInt(0));
			        itemC.set("UUIDLeast", new NBTTagInt(894654));
			        itemC.set("UUIDMost", new NBTTagInt(2872));
			        
			        itemAS.set("AttributeName", new NBTTagString("generic.attackSpeed"));
					itemAS.set("Name", new NBTTagString("generic.attackSpeed"));
					itemAS.set("Amount", new NBTTagDouble(-1.5));
					itemAS.set("Slot", new NBTTagString("mainhand"));
					itemAS.set("Operation", new NBTTagInt(0));
			        itemAS.set("UUIDLeast", new NBTTagInt(894654));
			        itemAS.set("UUIDMost", new NBTTagInt(2872));
			        
					modifiers.add(itemC);
					modifiers.add(itemAS);
					
					itemTagC.set("AttributeModifiers", modifiers);
					
					nmsStack.setTag(itemTagC);
					ItemStack nItem = CraftItemStack.asBukkitCopy(nmsStack);
					e.getPlayer().getInventory().getItemInMainHand().setAmount(0);
					e.getPlayer().getInventory().setItemInMainHand(nItem);
				}
			}
		}*/
	}
}
