package com.core.java.essentials.commands;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.core.java.essentials.Main;
import com.core.java.rpgbase.player.Armor;

import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.NBTTagDouble;
import net.minecraft.server.v1_14_R1.NBTTagInt;
import net.minecraft.server.v1_14_R1.NBTTagList;
import net.minecraft.server.v1_14_R1.NBTTagString;

public class GUICommand implements CommandExecutor {

	private Main plugin = Main.getInstance();
	
	public static Inventory combatInv = Bukkit.createInventory(null, 27, Main.color("&4&lCOMBAT INFO"));
	
	public static void createCombatInv() {
		ItemStack sword = new ItemStack(Material.IRON_SWORD);
		ItemMeta swordMeta = sword.getItemMeta();
		swordMeta.setDisplayName(Main.color("&aCUSTOM DAMAGE"));
		ArrayList<String> lore = new ArrayList<>();
		lore.add(Main.color(""));
		lore.add(Main.color("&fThe Value &4AD &frepresents bonus Attack Damage."));
		lore.add(Main.color("&fIt can be upgraded and altered."));
		lore.add(Main.color("&4Attack Damage &fis added onto to your Weapon Damage!"));
		swordMeta.setLore(lore);
		sword.setItemMeta(swordMeta);
		combatInv.setItem(11, sword);
		
		ItemStack shield = new ItemStack(Material.SHIELD);
		ItemMeta shieldMeta = shield.getItemMeta();
		shieldMeta.setDisplayName(Main.color("&aCUSTOM DEFENSES"));
		lore.clear();
		lore.add(Main.color(""));
		lore.add(Main.color("&fThe Value &cHP &frepresents your Health."));
		lore.add(Main.color("&fIt is displayed in your Stats Bar."));
		lore.add(Main.color("&cHealth &fis modified by armor (full sets) &8[&f/armor&8]&f."));
		lore.add(Main.color(""));
		shieldMeta.setLore(lore);
		shield.setItemMeta(shieldMeta);
		combatInv.setItem(12, shield);
		
		ItemStack emerald = new ItemStack(Material.EMERALD);
		ItemMeta emeraldMeta = emerald.getItemMeta();
		emeraldMeta.setDisplayName(Main.color("&aDAMAGE TYPES"));
		lore.clear();
		lore.add(Main.color(""));
		lore.add(Main.color("&fThere are three key damage types."));
		lore.add(Main.color("&cPhysical&f, &bMagic&f, and &f&lTrue&f."));
		lore.add(Main.color(""));
		lore.add(Main.color("&cPhysical&7: &fAffected by Armor and Physical Resistance."));
		lore.add(Main.color("&bMagic&7: &fAffected by Armor and Magic Resistance."));
		lore.add(Main.color("&f&lTRUE&7: &fPierce resistances and deals full damage."));
		lore.add(Main.color(""));
		emeraldMeta.setLore(lore);
		emerald.setItemMeta(emeraldMeta);
		combatInv.setItem(13, emerald);
	}
	
	public static Inventory armorInv = Bukkit.createInventory(null, 27, Main.color("&e&lARMOR SETS"));
	
	public static void createArmorInv() {
		ItemStack leather = new ItemStack(Material.LEATHER_CHESTPLATE);
		net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(leather);
		NBTTagCompound itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
		NBTTagList modifiers = new NBTTagList();
		NBTTagCompound itemC = new NBTTagCompound();
		itemC.set("AttributeName", new NBTTagString("generic.armor"));
		itemC.set("Name", new NBTTagString("generic.armor"));
		itemC.set("Amount", new NBTTagDouble(0));
		itemC.set("Slot", new NBTTagString("chest"));
		itemC.set("Operation", new NBTTagInt(0));
        itemC.set("UUIDLeast", new NBTTagInt(894654));
        itemC.set("UUIDMost", new NBTTagInt(2872));
		modifiers.add(itemC);
		itemTagC.set("AttributeModifiers", modifiers);
		nmsStack.setTag(itemTagC);
		leather = CraftItemStack.asBukkitCopy(nmsStack);
		ItemMeta data = leather.getItemMeta();
		data.setDisplayName(Main.color("&eLeather Armor Set"));
		ArrayList<String> lore = new ArrayList<>();
		lore.add(Main.color("&7&m--------------------"));
		lore.add(Main.color(""));
		lore.add(Main.color("&7»&a + Hitpoints (" + String.valueOf(Armor.leatherA) + "HP)"));
		//lore.add(Main.color("&7»&8 = Knockback Resistance (0%)"));
		lore.add(Main.color("&7»&a + Movement Speed (30%)"));
		//lore.add(Main.color("&7»&a + Attack Speed (33%)"));
		lore.add(Main.color("&7»&a + Physical Resistance (10%)"));
		lore.add(Main.color("&7»&c - Magic Resistance (-10%)"));
		lore.add(Main.color(""));
		lore.add(Main.color("&7&m--------------------"));
		data.setLore(lore);
		leather.setItemMeta(data);
		
		ItemStack golden = new ItemStack(Material.GOLDEN_CHESTPLATE);
		nmsStack = CraftItemStack.asNMSCopy(golden);
		itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
		modifiers = new NBTTagList();
		itemC = new NBTTagCompound();
		itemC.set("AttributeName", new NBTTagString("generic.armor"));
		itemC.set("Name", new NBTTagString("generic.armor"));
		itemC.set("Amount", new NBTTagDouble(0));
		itemC.set("Slot", new NBTTagString("chest"));
		itemC.set("Operation", new NBTTagInt(0));
        itemC.set("UUIDLeast", new NBTTagInt(894654));
        itemC.set("UUIDMost", new NBTTagInt(2872));
		modifiers.add(itemC);
		itemTagC.set("AttributeModifiers", modifiers);
		nmsStack.setTag(itemTagC);
		golden = CraftItemStack.asBukkitCopy(nmsStack);
		data = golden.getItemMeta();
		data.setDisplayName(Main.color("&eGolden Armor Set"));
		lore = new ArrayList<>();
		lore.add(Main.color("&7&m--------------------"));
		lore.add(Main.color(""));
		lore.add(Main.color("&7»&a + Hitpoints (" + String.valueOf(Armor.goldenA) + "HP)"));
		//lore.add(Main.color("&7»&a + Knockback Resistance (10%)"));
		lore.add(Main.color("&7»&c - Movement Speed (-5%)"));
		//lore.add(Main.color("&7»&c - Attack Speed (-16%)"));
		lore.add(Main.color("&7»&8 = Physical Resistance (0%)"));
		lore.add(Main.color("&7»&a + Magic Resistance (50%)"));
		lore.add(Main.color(""));
		lore.add(Main.color("&7&m--------------------"));
		data.setLore(lore);
		golden.setItemMeta(data);
		
		ItemStack chainmail = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		nmsStack = CraftItemStack.asNMSCopy(chainmail);
		itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
		modifiers = new NBTTagList();
		itemC = new NBTTagCompound();
		itemC.set("AttributeName", new NBTTagString("generic.armor"));
		itemC.set("Name", new NBTTagString("generic.armor"));
		itemC.set("Amount", new NBTTagDouble(0));
		itemC.set("Slot", new NBTTagString("chest"));
		itemC.set("Operation", new NBTTagInt(0));
        itemC.set("UUIDLeast", new NBTTagInt(894654));
        itemC.set("UUIDMost", new NBTTagInt(2872));
		modifiers.add(itemC);
		itemTagC.set("AttributeModifiers", modifiers);
		nmsStack.setTag(itemTagC);
		chainmail = CraftItemStack.asBukkitCopy(nmsStack);
		data = chainmail.getItemMeta();
		data.setDisplayName(Main.color("&eChainmail Armor Set"));
		lore = new ArrayList<>();
		lore.add(Main.color("&7&m--------------------"));
		lore.add(Main.color(""));
		lore.add(Main.color("&7»&a + Hitpoints (" + String.valueOf(Armor.chainmailA) + "HP)"));
		//lore.add(Main.color("&7»&8 = Knockback Resistance (0%)"));
		lore.add(Main.color("&7»&a + Movement Speed (10%)"));
		//lore.add(Main.color("&7»&a + Attack Speed (16%)"));
		lore.add(Main.color("&7»&a + Physical Resistance (20%)"));
		lore.add(Main.color("&7»&c - Magic Resistance (-45%)"));
		lore.add(Main.color(""));
		lore.add(Main.color("&7&m--------------------"));
		data.setLore(lore);
		chainmail.setItemMeta(data);
		
		ItemStack iron = new ItemStack(Material.IRON_CHESTPLATE);
		nmsStack = CraftItemStack.asNMSCopy(iron);
		itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
		modifiers = new NBTTagList();
		itemC = new NBTTagCompound();
		itemC.set("AttributeName", new NBTTagString("generic.armor"));
		itemC.set("Name", new NBTTagString("generic.armor"));
		itemC.set("Amount", new NBTTagDouble(0));
		itemC.set("Slot", new NBTTagString("chest"));
		itemC.set("Operation", new NBTTagInt(0));
        itemC.set("UUIDLeast", new NBTTagInt(894654));
        itemC.set("UUIDMost", new NBTTagInt(2872));
		modifiers.add(itemC);
		itemTagC.set("AttributeModifiers", modifiers);
		nmsStack.setTag(itemTagC);
		iron = CraftItemStack.asBukkitCopy(nmsStack);
		data = iron.getItemMeta();
		data.setDisplayName(Main.color("&eIron Armor Set"));
		lore = new ArrayList<>();
		lore.add(Main.color("&7&m--------------------"));
		lore.add(Main.color(""));
		lore.add(Main.color("&7»&a + Hitpoints (" + String.valueOf(Armor.ironA) + "HP)"));
		//lore.add(Main.color("&7»&a + Knockback Resistance (30%)"));
		lore.add(Main.color("&7»&c - Movement Speed (-10%)"));
		//lore.add(Main.color("&7»&8 = Attack Speed (0%)"));
		lore.add(Main.color("&7»&a + Physical Resistance (40%)"));
		lore.add(Main.color("&7»&c - Magic Resistance (-20%)"));
		lore.add(Main.color(""));
		lore.add(Main.color("&7&m--------------------"));
		data.setLore(lore);
		iron.setItemMeta(data);
		
		ItemStack diamond = new ItemStack(Material.DIAMOND_CHESTPLATE);
		nmsStack = CraftItemStack.asNMSCopy(diamond);
		itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
		modifiers = new NBTTagList();
		itemC = new NBTTagCompound();
		itemC.set("AttributeName", new NBTTagString("generic.armor"));
		itemC.set("Name", new NBTTagString("generic.armor"));
		itemC.set("Amount", new NBTTagDouble(0));
		itemC.set("Slot", new NBTTagString("chest"));
		itemC.set("Operation", new NBTTagInt(0));
        itemC.set("UUIDLeast", new NBTTagInt(894654));
        itemC.set("UUIDMost", new NBTTagInt(2872));
		modifiers.add(itemC);
		itemTagC.set("AttributeModifiers", modifiers);
		nmsStack.setTag(itemTagC);
		diamond = CraftItemStack.asBukkitCopy(nmsStack);
		data = diamond.getItemMeta();
		data.setDisplayName(Main.color("&eDiamond Armor Set"));
		lore = new ArrayList<>();
		lore.add(Main.color("&7&m--------------------"));
		lore.add(Main.color(""));
		lore.add(Main.color("&7»&a + Hitpoints (" + String.valueOf(Armor.diamondA) + "HP)"));
		//lore.add(Main.color("&7»&a + Knockback Resistance (60%)"));
		lore.add(Main.color("&7»&c - Movement Speed (-20%)"));
		//lore.add(Main.color("&7»&c - Attack Damage (-33%)"));
		lore.add(Main.color("&7»&a + Physical Resistance (30%)"));
		lore.add(Main.color("&7»&a + Magic Resistance (20%)"));
		lore.add(Main.color(""));
		lore.add(Main.color("&7&m--------------------"));
		data.setLore(lore);
		diamond.setItemMeta(data);
		
		armorInv.setItem(11, leather);
		armorInv.setItem(12, golden);
		armorInv.setItem(13, chainmail);
		armorInv.setItem(14, iron);
		armorInv.setItem(15, diamond);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("armor")) {
				
				p.openInventory(armorInv);
			
			}
		} else {
			Main.so("&cGUI Commands cannot be accessed from the Console");
		}
		return false;
	}

}
