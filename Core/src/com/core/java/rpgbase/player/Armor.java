package com.core.java.rpgbase.player;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

//import com.codingforcookies.armorequip.ArmorEquipEvent;
//import com.codingforcookies.armorequip.ArmorEquipEvent.EquipMethod;
import com.core.java.essentials.Main;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;

import net.minecraft.server.v1_14_R1.Item;
import net.minecraft.server.v1_14_R1.NBTBase;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.NBTTagDouble;
import net.minecraft.server.v1_14_R1.NBTTagInt;
import net.minecraft.server.v1_14_R1.NBTTagList;
import net.minecraft.server.v1_14_R1.NBTTagString;

public class Armor implements Listener {
	
	public static final double basehp = 200;
	public static final double leatherA = 75;
	public static final double goldenA = 100;
	public static final double chainmailA = 125;
	public static final double ironA = 150;
	public static final double diamondA = 200;
	
	public static double leatherADDefense = 0.9;
	public static double goldADDefense = 1.0;
	public static double chainADDefense = 0.8;
	public static double ironADDefense = 0.6;
	public static double diamondADDefense = 0.7;
	
	public static double leatherMagDefense = 1.1;
	public static double goldMagDefense = 0.5;
	public static double chainMagDefense = 1.45;
	public static double ironMagDefense = 1.2;
	public static double diamondMagDefense = 0.8;
	
	/*public static double diamondAS = 0;//-2.0;
	public static double ironAS = 0;
	public static double goldenAS = 0;//-1.0;
	public static double chainmailAS = 0;//1.0;
	public static double leatherAS = 0;//2.0;*/
	
	private Main plugin = Main.getInstance();
	private static double hp = basehp;
	
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
	public void armorDurability (PlayerItemDamageEvent e) {
		String s = e.getItem().getType().toString().toLowerCase();
		if ((s.contains("helmet") || 
				s.contains("chestplate") || s.contains("leggings") ||
				s.contains("boots") || s.contains("cap") || 
				s.contains("tunic") || s.contains("pants"))) {
			e.setDamage(1);
		}
	}
	
	@EventHandler
	public void armorDamage (EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			Main.sendHp(p);
			//String set = getSet(p);
			double armor = plugin.getArmorMap().get(p.getUniqueId());
			double mr = plugin.getMRMap().get(p.getUniqueId());
			if (e.getCause() == DamageCause.CUSTOM) {
				/*if (set.equals("diamond")) {
					e.setDamage(e.getDamage() * diamondMagDefense);
				} else if (set.equals("iron")) {
					e.setDamage(e.getDamage() * ironMagDefense);
				} else if (set.equals("golden")) {
					e.setDamage(e.getDamage() * goldMagDefense);
				} else if (set.equals("chainmail")) {
					e.setDamage(e.getDamage() * chainMagDefense);
				} else if (set.equals("leather")) {
					e.setDamage(e.getDamage() * leatherMagDefense);
				}*/
				e.setDamage(e.getDamage() * (1-mr));
			} else if (e.getCause() == DamageCause.ENTITY_ATTACK || e.getCause() == DamageCause.ENTITY_SWEEP_ATTACK || e.getCause() == DamageCause.PROJECTILE) {
				/*if (set.equals("diamond")) {
					e.setDamage(e.getDamage() * diamondADDefense);
				} else if (set.equals("iron")) {
					e.setDamage(e.getDamage() * ironADDefense);
				} else if (set.equals("golden")) {
					e.setDamage(e.getDamage() * goldADDefense);
				} else if (set.equals("chainmail")) {
					e.setDamage(e.getDamage() * chainADDefense);
				} else if (set.equals("leather")) {
					e.setDamage(e.getDamage() * leatherADDefense);
				}*/
				e.setDamage(e.getDamage() * (1-armor));
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
	public void onSwap (PlayerItemHeldEvent e) {
		new BukkitRunnable() {
			public void run() {
				fixInventory(e.getPlayer());
			}
		}.runTaskLater(plugin, 1L);
	}
	
	@EventHandler
	public void onPickup (EntityPickupItemEvent e) {
		if (e.getEntity() instanceof Player) {
			new BukkitRunnable() {
				public void run() {
					fixInventory((Player) e.getEntity());
				}
			}.runTaskLater(plugin, 1L);
		}
	}
	
	@EventHandler
	public void onCraft (CraftItemEvent e) {
		for (HumanEntity ent : e.getInventory().getViewers()) {
			if (ent instanceof Player) {
				new BukkitRunnable() {
					public void run() {
						fixInventory((Player) ent);
					}
				}.runTaskLater(plugin, 1L);
			}
		}
	}
	
	@EventHandler
	public void armorChange (PlayerArmorChangeEvent e) {
		Player p = (Player) e.getPlayer();
		new BukkitRunnable() {
			public void run() {
				updateSet(p);
			}
		}.runTaskLater(plugin, 1L);
		/*if (e.getMethod() == EquipMethod.SHIFT_CLICK || e.getMethod() == EquipMethod.HOTBAR || e.getMethod() == EquipMethod.HOTBAR_SWAP) {
			if (e.getNewArmorPiece() != null) {
				net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(e.getNewArmorPiece());
				if (nmsStack.getTag() == null) {
					NBTTagCompound itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
					NBTTagList modifiers = new NBTTagList();
					NBTTagCompound itemC = new NBTTagCompound();
					
					itemC.set("AttributeName", new NBTTagString("generic.armor"));
					itemC.set("Name", new NBTTagString("generic.armor"));
					itemC.set("Amount", new NBTTagDouble(0));
					itemC.set("Operation", new NBTTagInt(0));
			        itemC.set("UUIDLeast", new NBTTagInt(894654));
			        itemC.set("UUIDMost", new NBTTagInt(2872));
					
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
					e.getNewArmorPiece().setAmount(0);
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
				}
			}
		} else {
			new BukkitRunnable() {
				public void run() {
					fixArmor(e.getPlayer());
				}
			}.runTaskLater(plugin, 1L);
		}*/
		/*String set = getSet(p);
		if (e.getOldArmorPiece() != null) {
			if (e.getOldArmorPiece().getType() != Material.AIR) {
				if (e.getNewArmorPiece() == null || e.getNewArmorPiece().getType() == Material.AIR) {
					set = "nothing";
				}
			}
		}*/
		
	}
	
	public static boolean isArmor(String s) {
		return (s.contains("HELMET") || s.contains("CHESTPLATE") || s.contains("LEGGINGS") || s.contains("BOOTS"));
	}
	
	public static ItemStack fixItem(ItemStack i) {
		if (i != null && i.getType() != null && isArmor(i.getType().toString())) {
			net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(i);
			if (!i.hasItemMeta() || !i.getItemMeta().hasLore() || nmsStack.getTag() == null || nmsStack.getTag().getList("AttributeModifiers", 0) instanceof NBTTagList) {
				NBTTagCompound itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
				NBTTagList modifiers = new NBTTagList();
				NBTTagCompound itemC = new NBTTagCompound();
				
				itemC.set("AttributeName", new NBTTagString("generic.armor"));
				itemC.set("Name", new NBTTagString("generic.armor"));
				itemC.set("Amount", new NBTTagDouble(0));
				itemC.set("Operation", new NBTTagInt(0));
		        itemC.set("UUIDLeast", new NBTTagInt(894654));
		        itemC.set("UUIDMost", new NBTTagInt(2872));
				
				String item = i.toString().toLowerCase();
				
				
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
				
				List<String> lore = new ArrayList<String>();
				String type = nItem.getType().toString().toLowerCase();
				lore.add("");
				lore.add(Main.color("&6Level:&7 1"));
				if (type.contains("turtle")) {
					lore.add(Main.color("&4Health:&7 100"));
					lore.add(Main.color("&cArmor:&7 10%"));
					lore.add(Main.color("&bMagic Resist:&7 10%"));
				}
				if (type.contains("diamond")) {
					if (type.contains("helmet")) {
						lore.add(Main.color("&4Health:&7 100"));
						lore.add(Main.color("&cArmor:&7 5%"));
						lore.add(Main.color("&bMagic Resist:&7 5%"));
					}
					if (type.contains("chestplate")) {
						lore.add(Main.color("&4Health:&7 150"));
						lore.add(Main.color("&cArmor:&7 15%"));
						lore.add(Main.color("&bMagic Resist:&7 10%"));
					}
					if (type.contains("leggings")) {
						lore.add(Main.color("&4Health:&7 125"));
						lore.add(Main.color("&cArmor:&7 10%"));
						lore.add(Main.color("&bMagic Resist:&7 5%"));
					}
					if (type.contains("boots")) {
						lore.add(Main.color("&4Health:&7 50"));
						lore.add(Main.color("&cArmor:&7 5%"));
						lore.add(Main.color("&bMagic Resist:&7 5%"));
					}
				}
				if (type.contains("iron")) {
					if (type.contains("helmet")) {
						lore.add(Main.color("&4Health:&7 75"));
						lore.add(Main.color("&cArmor:&7 5%"));
						lore.add(Main.color("&bMagic Resist:&7 5%"));
					}
					if (type.contains("chestplate")) {
						lore.add(Main.color("&4Health:&7 125"));
						lore.add(Main.color("&cArmor:&7 15%"));
						lore.add(Main.color("&bMagic Resist:&7 5%"));
					}
					if (type.contains("leggings")) {
						lore.add(Main.color("&4Health:&7 100"));
						lore.add(Main.color("&cArmor:&7 10%"));
						lore.add(Main.color("&bMagic Resist:&7 5%"));
					}
					if (type.contains("boots")) {
						lore.add(Main.color("&4Health:&7 35"));
						lore.add(Main.color("&cArmor:&7 5%"));
						lore.add(Main.color("&bMagic Resist:&7 5%"));
					}
				}
				if (type.contains("chain")) {
					if (type.contains("helmet")) {
						lore.add(Main.color("&4Health:&7 50"));
						lore.add(Main.color("&cArmor:&7 8%"));
						lore.add(Main.color("&bMagic Resist:&7 5%"));
					}
					if (type.contains("chestplate")) {
						lore.add(Main.color("&4Health:&7 100"));
						lore.add(Main.color("&cArmor:&7 12%"));
						lore.add(Main.color("&bMagic Resist:&7 8%"));
					}
					if (type.contains("leggings")) {
						lore.add(Main.color("&4Health:&7 75"));
						lore.add(Main.color("&cArmor:&7 5%"));
						lore.add(Main.color("&bMagic Resist:&7 5%"));
					}
					if (type.contains("boots")) {
						lore.add(Main.color("&4Health:&7 25"));
						lore.add(Main.color("&cArmor:&7 5%"));
						lore.add(Main.color("&bMagic Resist:&7 2%"));
					}
				}
				if (type.contains("gold")) {
					if (type.contains("helmet")) {
						lore.add(Main.color("&4Health:&7 50"));
						lore.add(Main.color("&cArmor:&7 5%"));
						lore.add(Main.color("&bMagic Resist:&7 15%"));
					}
					if (type.contains("chestplate")) {
						lore.add(Main.color("&4Health:&7 75"));
						lore.add(Main.color("&cArmor:&7 5%"));
						lore.add(Main.color("&bMagic Resist:&7 15%"));
					}
					if (type.contains("leggings")) {
						lore.add(Main.color("&4Health:&7 50"));
						lore.add(Main.color("&cArmor:&7 5%"));
						lore.add(Main.color("&bMagic Resist:&7 15%"));
					}
					if (type.contains("boots")) {
						lore.add(Main.color("&4Health:&7 20"));
						lore.add(Main.color("&cArmor:&7 5%"));
						lore.add(Main.color("&bMagic Resist:&7 15%"));
					}
				}
				if (type.contains("leather")) {
					if (type.contains("helmet")) {
						lore.add(Main.color("&4Health:&7 25"));
						lore.add(Main.color("&cArmor:&7 3%"));
						lore.add(Main.color("&bMagic Resist:&7 3%"));
					}
					if (type.contains("chestplate")) {
						lore.add(Main.color("&4Health:&7 50"));
						lore.add(Main.color("&cArmor:&7 3%"));
						lore.add(Main.color("&bMagic Resist:&7 3%"));
					}
					if (type.contains("leggings")) {
						lore.add(Main.color("&4Health:&7 25"));
						lore.add(Main.color("&cArmor:&7 3%"));
						lore.add(Main.color("&bMagic Resist:&7 3%"));
					}
					if (type.contains("boots")) {
						lore.add(Main.color("&4Health:&7 15"));
						lore.add(Main.color("&cArmor:&7 3%"));
						lore.add(Main.color("&bMagic Resist:&7 3%"));
					}
				}
				
				ItemMeta meta = nItem.getItemMeta();
				meta.setLore(lore);
				
				nItem.setItemMeta(meta);
				return nItem;
			}
		}
		return i;
	}
	
	public static void fixInventory(Player p) {
		for (int i = 0; i < p.getInventory().getContents().length; i++) {
			if (p.getInventory().getItem(i) instanceof ItemStack) {
				ItemStack it = p.getInventory().getItem(i);
				if (it != null && it.getType() != null && isArmor(it.getType().toString()) && isNotUpdated(it)) {
					p.getInventory().setItem(i, fixItem(it));
				}
			}
		}
	}
	
	public static boolean isNotUpdated(ItemStack i) {
		return !(i != null && i.hasItemMeta() && i.getItemMeta().hasLore() && i.getItemMeta().getLore().toString().toLowerCase().contains("level"));
	}
	
	public static void fixArmor(Player p) {
		for (ItemStack i : p.getInventory().getArmorContents()) {
			ItemStack nItem = fixItem(i);
			i.setAmount(0);
			String item = nItem.getType().toString().toLowerCase();
			if (item.contains("helmet") || item.contains("cap")) {
				p.getInventory().setHelmet(nItem);
			}
			if (item.contains("chestplate") || item.contains("tunic")) {
				p.getInventory().setChestplate(nItem);
			}
			if (item.contains("leggings") || item.contains("pants")) {
				p.getInventory().setLeggings(nItem);
			}
			if (item.contains("boots")) {
				p.getInventory().setBoots(nItem);
			}
		}
	}
	
	public static void updateSet(Player p) {
		fixInventory(p);
		if (p.getInventory().getArmorContents() != null) {
			for (ItemStack i : p.getInventory().getArmorContents()) {
				if (i != null && i.getType() != Material.AIR) {
					if (i.hasItemMeta() && i.getItemMeta().hasLore()) {
						int index = 0;
						for (String s : i.getItemMeta().getLore()) {
							if (s.contains("Level")) {
								break;
							} else {
								index++;
							}
						}
						if (index < i.getItemMeta().getLore().size()) {
							String hpb = i.getItemMeta().getLore().get(index);
							hpb = ChatColor.stripColor(hpb);
							hpb = hpb.replace("Level: ", "");
							int level = Main.getInstance().getLevel(p);
							if (Integer.valueOf(hpb) instanceof Integer) {
								if (Integer.valueOf(hpb) <= level) {
								} else {
									ItemStack newitem = new ItemStack(Material.STONE);
									newitem = i;
									Main.msg(p, "&cYou do not meet this item's level requirements.");
									if (p.getInventory().firstEmpty() == -1) {
										p.getWorld().dropItem(p.getLocation(), newitem);
									} else {
										p.getInventory().addItem(newitem);
									}
									i.setAmount(0);
								}
							}
						}
					}
				}
			}
		}
		if (p.getHealth() > 0.0) {
			//double hpPercent = p.getHealth() / p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
			
			double hp = basehp;
			if (p.getInventory().getArmorContents() != null) {
				for (ItemStack i : p.getInventory().getArmorContents()) {
					if (i != null && i.getType() != Material.AIR) {
						if (i.hasItemMeta() && i.getItemMeta().hasLore()) {
							int index = 0;
							for (String s : i.getItemMeta().getLore()) {
								if (s.contains("Health")) {
									break;
								} else {
									index++;
								}
							}
							if (index < i.getItemMeta().getLore().size()) {
								String hpb = i.getItemMeta().getLore().get(index);
								hpb = ChatColor.stripColor(hpb);
								hpb = hpb.replace("Health: ", "");
								if (Double.valueOf(hpb) instanceof Double) {
									hp+=Double.valueOf(hpb);
								}
							}
						}
					}
				}
			}
			hp+=Double.valueOf(Main.getValue(p, "HP"));
			p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
			if (p.getHealth() >= hp) {
				p.setHealth(hp);
			}
			
			double am = 0.0;
			double mr = 0.0;
			
			p.setHealthScale(20);
			
			if (p.getInventory().getArmorContents() != null) {
				for (ItemStack i : p.getInventory().getArmorContents()) {
					if (i != null && i.getType() != Material.AIR) {
						if (i.hasItemMeta() && i.getItemMeta().hasLore()) {
							int index = 0;
							for (String s : i.getItemMeta().getLore()) {
								if (s.contains("Armor")) {
									break;
								} else {
									index++;
								}
							}
							if (index < i.getItemMeta().getLore().size()) {
								String hpb = i.getItemMeta().getLore().get(index);
								hpb = ChatColor.stripColor(hpb);
								hpb = hpb.replace("Armor: ", "");
								hpb = hpb.replace("%", "");
								if (Double.valueOf(hpb) instanceof Double) {
									am+=(Double.valueOf(hpb)/100);
								}
							}
						}
					}
				}
			}
			
			if (p.getInventory().getArmorContents() != null) {
				for (ItemStack i : p.getInventory().getArmorContents()) {
					if (i != null && i.getType() != Material.AIR) {
						if (i.hasItemMeta() && i.getItemMeta().hasLore()) {
							int index = 0;
							for (String s : i.getItemMeta().getLore()) {
								if (s.contains("Magic")) {
									break;
								} else {
									index++;
								}
							}
							if (index < i.getItemMeta().getLore().size()) {
								String hpb = i.getItemMeta().getLore().get(index);
								hpb = ChatColor.stripColor(hpb);
								hpb = hpb.replace("Magic Resist: ", "");
								hpb = hpb.replace("%", "");
								if (Double.valueOf(hpb) instanceof Double) {
									mr+=(Double.valueOf(hpb)/100);
								}
							}
						}
					}
				}
			}
			
			if (Main.getInstance().getMRMap().containsKey(p.getUniqueId())) {
				Main.getInstance().getMRMap().replace(p.getUniqueId(), mr);
			} else {
				Main.getInstance().getMRMap().put(p.getUniqueId(), mr);
			}
			if (Main.getInstance().getArmorMap().containsKey(p.getUniqueId())) {
				Main.getInstance().getArmorMap().replace(p.getUniqueId(), am);
			} else {
				Main.getInstance().getArmorMap().put(p.getUniqueId(), am);
			}
			
			/*if (set.equals("diamond")) {
				p.setWalkSpeed(0.16F);
				p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp + diamondA);
				if (p.getHealth() >= hp + diamondA) {
					p.setHealth(hp + diamondA);
				} else {
					p.setHealth(hpPercent * p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
				}
			} else if (set.equals("iron")) {
				p.setWalkSpeed(0.18F);
				p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp + ironA);
				if (p.getHealth() >= hp + ironA) {
					p.setHealth(hp + ironA);
				} else {
					p.setHealth(hpPercent * p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
				}
			} else if (set.equals("golden")) {
				p.setWalkSpeed(0.19F);
				p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp + goldenA);
				if (p.getHealth() >= hp + goldenA) {
					p.setHealth(hp + goldenA);
				} else {
					p.setHealth(hpPercent * p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
				}
			} else if (set.equals("chainmail")) {
				p.setWalkSpeed(0.22F);
				p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp + chainmailA);
				if (p.getHealth() >= hp + chainmailA) {
					p.setHealth(hp + chainmailA);
				} else {
					p.setHealth(hpPercent * p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
				}
			} else if (set.equals("leather")) {
				p.setWalkSpeed(0.26F);
				p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp + leatherA);
				if (p.getHealth() >= hp + leatherA) {
					p.setHealth(hp + leatherA);
				} else {
					p.setHealth(hpPercent * p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
				}
			} else {
				if (p.getWalkSpeed() != 0.2F) {
					p.setWalkSpeed(0.2F);
				}
				p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
				if (p.getHealth() >= hp) {
					p.setHealth(hp);
				} else {
					p.setHealth(hpPercent * p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
				}
			}*/
		}
	}
	
	/*@EventHandler
	public void armorClick (InventoryClickEvent e) {
		if (e.getSlotType() == SlotType.ARMOR) {
			if (!e.getClick().isShiftClick()) {
				e.setCancelled(true);
				Main.msg((Player) e.getWhoClicked(), "&cERROR: &fPlease use Shift-Click to change Armor!");
			}
		}
	}*/
	
}
