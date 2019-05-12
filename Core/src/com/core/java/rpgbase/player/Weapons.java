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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
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

import net.minecraft.server.v1_14_R1.EntityPlayer;
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
				if (!i.getItemMeta().hasLore() || !i.getItemMeta().getLore().toString().contains("Damage")) {
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
							lore.add(Main.color("&8« &8Common &7Tier Weapon &8»"));
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
							lore.add(Main.color("&8« &8Common &7Tier Weapon &8»"));
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
							lore.add(Main.color("&e« &eUnique &7Tier Weapon &e»"));
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
			Player p = (Player) e.getDamager();
			if (Double.valueOf(getWeaponAttackDamage(p)) instanceof Double) {
				ItemStack i = p.getInventory().getItemInMainHand();
				if (i.containsEnchantment(Enchantment.DAMAGE_ALL)) {
					int level = i.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
					if (level == 1) {
						e.setDamage(e.getDamage() - 1);
						e.setDamage(e.getDamage() + 5 * i.getEnchantmentLevel(Enchantment.DAMAGE_ALL));
					} else {
						e.setDamage(e.getDamage() - 1 - 0.5 * (level - 1));
						e.setDamage(e.getDamage() + 5 * i.getEnchantmentLevel(Enchantment.DAMAGE_ALL));
					}
				}
				if (i.containsEnchantment(Enchantment.DAMAGE_UNDEAD)) {
					if (e.getEntity().getType() == EntityType.ZOMBIE || e.getEntity().getType() == EntityType.SKELETON || e.getEntity().getType() == EntityType.PHANTOM || e.getEntity().getType() == EntityType.SKELETON_HORSE || e.getEntity().getType() == EntityType.STRAY || e.getEntity().getType() == EntityType.HUSK || e.getEntity().getType() == EntityType.DROWNED || e.getEntity().getType() == EntityType.PIG_ZOMBIE || e.getEntity().getType() == EntityType.WITHER_SKELETON || e.getEntity().getType() == EntityType.WITHER) {
						e.setDamage(e.getDamage() - (2.5 * i.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD)));
						e.setDamage(e.getDamage() + 10 * i.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD));
					}
				}
				if (i.containsEnchantment(Enchantment.DAMAGE_ARTHROPODS)) {
					if (e.getEntity().getType() == EntityType.SPIDER || e.getEntity().getType() == EntityType.CAVE_SPIDER || e.getEntity().getType() == EntityType.SILVERFISH || e.getEntity().getType() == EntityType.ENDERMITE) {
						e.setDamage(e.getDamage() - (2.5 * i.getEnchantmentLevel(Enchantment.DAMAGE_ALL)));
						e.setDamage(e.getDamage() + 10 * i.getEnchantmentLevel(Enchantment.DAMAGE_ALL));
					}
				}
				if (i.containsEnchantment(Enchantment.IMPALING)) {
					if (e.getEntity().getType() == EntityType.PLAYER || e.getEntity().getType() == EntityType.GUARDIAN || e.getEntity().getType() == EntityType.ELDER_GUARDIAN || e.getEntity().getType() == EntityType.DOLPHIN || e.getEntity().getType() == EntityType.COD || e.getEntity().getType() == EntityType.PUFFERFISH || e.getEntity().getType() == EntityType.TROPICAL_FISH || e.getEntity().getType() == EntityType.SALMON || e.getEntity().getType() == EntityType.SQUID || e.getEntity().getType() == EntityType.TURTLE) {
						e.setDamage(e.getDamage() - (2.5 * i.getEnchantmentLevel(Enchantment.IMPALING)));
						e.setDamage(e.getDamage() + 5 * i.getEnchantmentLevel(Enchantment.IMPALING));
					}
				}
			}
			/*CraftPlayer p = (CraftPlayer) e.getDamager();
			try{
	            Method getHandle = p.getClass().getMethod("getHandle");
	            Object entityPlayer = getHandle.invoke(p);
	            Method m = entityPlayer.getClass().getDeclaredMethod("o");
	            m.setAccessible(true);
	            for (Method me : entityPlayer.getClass().getMethods()) {
	            	if (me.getName().contains("o") && me.getName().length() <= 3) {
	            		System.out.print(me.getName() + "   ");
	            		System.out.print(me.getReturnType());
	            	}
	            }
	            //System.out.println(p.getClass().getMethod("o", Float.class).invoke(p, 0.0F));
	        }catch(Exception ex){
	        	ex.printStackTrace();
	        }*/
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
