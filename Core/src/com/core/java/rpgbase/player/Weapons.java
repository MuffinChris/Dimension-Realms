package com.core.java.rpgbase.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
	

	@EventHandler
	public void onSwap (PlayerItemHeldEvent e) {
		new BukkitRunnable() {
			public void run() {
				if (e.getPlayer().getInventory().getItemInMainHand() != null) {
					ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
					if (item.getType().toString().toLowerCase().contains("sword")) { 
						net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
						NBTTagCompound itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
						NBTTagList modifiers = new NBTTagList();
						NBTTagCompound itemC = new NBTTagCompound();
						NBTTagCompound itemAS = new NBTTagCompound();
							
						String s = item.getType().toString().toLowerCase();
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
							itemAS.set("Amount", new NBTTagDouble(12.0));
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
					} else if (item.getType().toString().toLowerCase().contains("axe")) { 
						net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
						NBTTagCompound itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
						NBTTagList modifiers = new NBTTagList();
						NBTTagCompound itemC = new NBTTagCompound();
						NBTTagCompound itemAS = new NBTTagCompound();
							
						String s = item.getType().toString().toLowerCase();
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
					} else if (item.getType().toString().toLowerCase().contains("trident")) { 
						net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
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
					}
				}
			}
		}.runTaskLater(Main.getInstance(), 1L);
	}
	
	
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void onClick (PlayerInteractEvent e) {
		if (e.getItem() != null) {
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
		}
	}
}
