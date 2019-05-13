package com.core.java.enchantments;

import java.lang.reflect.Field;
import java.util.Map;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

import com.core.java.essentials.Main;

public class Serration extends Enchantment{
	   
	public static Serration enchantment;

	private Main main = Main.getInstance();
   
	public Serration() {
	   super(new NamespacedKey(Main.getInstance(), "Serration"));
	}

	@Override
	public boolean canEnchantItem(ItemStack item) {
	   if(item.getType().toString().toLowerCase().contains("AXE")) {
		   return true;
	   }
	   return false;
	}

	@Override
	public boolean conflictsWith(Enchantment arg0) {
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.TOOL;
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	public String getName() {
		return "Serration";
	}

	@Override
	public int getStartLevel() {
		return 1;
	}

	public static void register() {
		enchantment = new Serration();
		try {
			Field acceptingNew = Enchantment.class.getDeclaredField("acceptingNew");
			acceptingNew.setAccessible(true);
			acceptingNew.set(null, true);
			Enchantment.registerEnchantment(enchantment);
			Main.so("&cCORE ENCHANTMENTS&7: &fSerration registered!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
  
	@SuppressWarnings({ "unchecked" })
	public static void unregister() {
		try {
			Field bnf = Enchantment.class.getDeclaredField("byName");
			bnf.setAccessible(true);
			Map<String, Enchantment> byName = (Map<String, Enchantment>) bnf.get(null);
			byName.remove(enchantment.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean isCursed() {
		return false;
	}
	
	@Override
	public boolean isTreasure() {
		return false;
	}
}