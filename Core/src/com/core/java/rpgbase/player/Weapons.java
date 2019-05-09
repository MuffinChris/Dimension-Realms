package com.core.java.rpgbase.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.core.java.essentials.Main;

import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.NBTTagDouble;
import net.minecraft.server.v1_14_R1.NBTTagInt;
import net.minecraft.server.v1_14_R1.NBTTagList;
import net.minecraft.server.v1_14_R1.NBTTagString;

public class Weapons  implements Listener {
	
	@EventHandler
	public void onClick (PlayerInteractEvent e) {
		if (e.getItem() != null) {
			if (e.getItem().getType().toString().toLowerCase().contains("sword")) { 
				net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(e.getItem());
				NBTTagCompound itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
				NBTTagList modifiers = new NBTTagList();
				NBTTagCompound itemC = new NBTTagCompound();
					
				String s = e.getItem().getType().toString().toLowerCase();
				double ogdmg = 2;
				if (s.contains("diamond")) {
					ogdmg = 7;
				}
				if (s.contains("iron")) {
					ogdmg = 6;
				}
				if (s.contains("stone")) {
					ogdmg = 5;
				}
				if (s.contains("wood")) {
					ogdmg = 4;
				}
				if (s.contains("gold")) {
					ogdmg = 4;
				}
				itemC.set("AttributeName", new NBTTagString("generic.attackDamage"));
				itemC.set("Name", new NBTTagString("generic.attackDamage"));
				if (nmsStack.getTag() == null || !(Double.valueOf(nmsStack.getTag().getDouble("Amount")) instanceof Double)) {
					itemC.set("Amount", new NBTTagDouble(ogdmg * 4.0));
					modifiers.add(itemC);
					itemTagC.set("AttributeModifiers", modifiers);
					itemTagC.set("Slot", new NBTTagString("mainhand"));
					itemC.set("Operation", new NBTTagInt(0));
			        itemC.set("UUIDLeast", new NBTTagInt(894654));
			        itemC.set("UUIDMost", new NBTTagInt(2872));
			        itemC.set("Slot", new NBTTagString("mainhand"));
					nmsStack.setTag(itemTagC);
					ItemStack nItem = CraftItemStack.asBukkitCopy(nmsStack);
					/*ItemMeta meta = nItem.getItemMeta();
					List<String> lore = new ArrayList<String>();
					lore.add(Main.color("&cAttack Damage: &f" + ogdmg * 4.0));
					meta.setLore(lore);
					nItem.setItemMeta(meta);*/
					e.getPlayer().getInventory().getItemInMainHand().setAmount(0);
					e.getPlayer().getInventory().setItemInMainHand(nItem);
				}
			} else if (e.getItem().getType().toString().toLowerCase().contains("axe")) { 
				net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(e.getItem());
				NBTTagCompound itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
				NBTTagList modifiers = new NBTTagList();
				NBTTagCompound itemC = new NBTTagCompound();
					
				String s = e.getItem().getType().toString().toLowerCase();
				double ogdmg = 2;
				if (s.contains("diamond")) {
					ogdmg = 6;
				}
				if (s.contains("iron")) {
					ogdmg = 5;
				}
				if (s.contains("stone")) {
					ogdmg = 4;
				}
				if (s.contains("wood")) {
					ogdmg = 3;
				}
				if (s.contains("gold")) {
					ogdmg = 3;
				}
				itemC.set("AttributeName", new NBTTagString("generic.attackDamage"));
				itemC.set("Name", new NBTTagString("generic.attackDamage"));
				if (nmsStack.getTag() == null || !(Double.valueOf(nmsStack.getTag().getDouble("Amount")) instanceof Double)) {
					itemC.set("Amount", new NBTTagDouble(ogdmg * 4.0));
					modifiers.add(itemC);
					itemTagC.set("AttributeModifiers", modifiers);
					itemTagC.set("Slot", new NBTTagString("mainhand"));
					itemC.set("Operation", new NBTTagInt(0));
			        itemC.set("UUIDLeast", new NBTTagInt(894654));
			        itemC.set("UUIDMost", new NBTTagInt(2872));
			        itemC.set("Slot", new NBTTagString("mainhand"));
					nmsStack.setTag(itemTagC);
					ItemStack nItem = CraftItemStack.asBukkitCopy(nmsStack);
					/*ItemMeta meta = nItem.getItemMeta();
					List<String> lore = new ArrayList<String>();
					lore.add(Main.color("&cAttack Damage: &f" + ogdmg * 4.0));
					meta.setLore(lore);
					nItem.setItemMeta(meta);*/
					e.getPlayer().getInventory().getItemInMainHand().setAmount(0);
					e.getPlayer().getInventory().setItemInMainHand(nItem);
				}
			}
		}
	}
}
