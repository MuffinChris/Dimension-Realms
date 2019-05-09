package com.core.java.rpgbase;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.core.java.rpgbase.bossbars.BS;
import com.core.java.rpgbase.player.Armor;
import com.core.java.essentials.Main;

public class RPGFunctions implements Listener {
	
	private Main plugin = Main.getInstance();
	private double hp = Main.basehp;
	
	@EventHandler
	public void onLeaveRPG (PlayerQuitEvent e) {
		UUID uuid = e.getPlayer().getUniqueId();
        if (plugin.getManaMap().containsKey(uuid)) {
        	plugin.getManaMap().remove(uuid);
        }
        if (plugin.getManaRegenMap().containsKey(uuid)) {
        	plugin.getManaRegenMap().remove(uuid);
        }
        if (plugin.getAdMap().containsKey(uuid)) {
        	plugin.getAdMap().remove(uuid);
        }
        if (plugin.getBarManager().getBossBars().containsKey(uuid)) {
        	plugin.getBarManager().getBossBars().remove(uuid);
        }
        if (plugin.getAbilities().containsKey(uuid)) {
        	plugin.getAbilities().remove(uuid);
        }
        if (plugin.getLevelMap().containsKey(uuid)) {
        	plugin.getLevelMap().remove(uuid);
        }
        if (plugin.getExpMap().containsKey(uuid)) {
        	plugin.getExpMap().remove(uuid);
        }
        if (plugin.getSPMap().containsKey(uuid)) {
        	plugin.getSPMap().remove(uuid);
        }
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void sendHpRegain (EntityRegainHealthEvent e) {
		if (e.getEntity() instanceof Player) {
			Main.sendHp((Player) e.getEntity());
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		new BukkitRunnable() {
			public void run() {
				Armor.updateSet(e.getPlayer(), Armor.getSet(e.getPlayer()));
			}
		}.runTaskLater(plugin, 5L);
	}
	
	
	/*@EventHandler (priority = EventPriority.LOWEST)
	public void absorption (EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Entity) {
			if (e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				if (p.hasPotionEffect(PotionEffectType.ABSORPTION)) {
					int factor = 10;
					int dmgreduc = p.getPotionEffect(PotionEffectType.ABSORPTION).getAmplifier() * factor;
					int duration = p.getPotionEffect(PotionEffectType.ABSORPTION).getDuration();
					if (e.getDamage() >= dmgreduc) {
						double damage = e.getDamage() - dmgreduc;
						e.setDamage(damage);
						p.removePotionEffect(PotionEffectType.ABSORPTION);
					} else {
						p.removePotionEffect(PotionEffectType.ABSORPTION);
						p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, duration, (int) ((Math.floor(dmgreduc - e.getDamage())/factor))));
						e.setDamage(0);
					}
					PotionEffect pot = p.getPotionEffect(PotionEffectType.ABSORPTION);
					p.removePotionEffect(PotionEffectType.ABSORPTION);
					new BukkitRunnable() {
						public void run() {
							p.addPotionEffect(pot);
						}
					}.runTaskLater(plugin, 1L);
				}
			}
		}
	}*/
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void hitDelay (EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			if (e.getEntity() instanceof LivingEntity) {
				LivingEntity ent = (LivingEntity) e.getEntity();
				new BukkitRunnable() {
					public void run() {
						ent.setNoDamageTicks(0);
					}
				}.runTaskLater(plugin, 5L);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void bossbarHP (EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player || e.getDamager() instanceof Arrow) {
			if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof Player) && e.getEntity().getType() != EntityType.ARMOR_STAND) {
				DecimalFormat dF = new DecimalFormat("#.##");
				LivingEntity ent = (LivingEntity) e.getEntity();
				Player p;
				if (e.getDamager() instanceof Player) {
					p = (Player) e.getDamager();
				} else {
					Arrow a = (Arrow) e.getDamager();
					if (a.getShooter() instanceof Player) {
						p = (Player) a.getShooter();
					} else {
						return;
					}
				}
				BlockData blood = Material.REDSTONE_BLOCK.createBlockData();
				ent.getWorld().spawnParticle(Particle.BLOCK_DUST, ent.getLocation(), 100, 0, 1, 0, blood);
				new BukkitRunnable() {
					@Override
					public void run() {
						double progress = Math.max((ent.getHealth()) / ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue(), 0.0);
						plugin.getBarManager().getBS(p).setInfo(Main.color("&c" + ent.getCustomName() + "&8: &f" + dF.format(ent.getHealth())), BarColor.RED, BarStyle.SOLID, progress, true);
					    if (ent.getHealth() <= 0) {
					        new BukkitRunnable() {
					        	public void run() {
					        		plugin.getBarManager().getBS(p).setInfo("", BarColor.RED, BarStyle.SOLID, 0.0, false);
					        	}
					        }.runTaskLater(plugin, 20L);
					       }
					}
				}.runTaskLater(plugin, 1L);
			} else if (e.getEntity() instanceof Player) {
				DecimalFormat dF = new DecimalFormat("#.##");
				Player ent = (Player) e.getEntity();
				Player p;
				if (e.getDamager() instanceof Player) {
					p = (Player) e.getDamager();
				} else {
					Arrow a = (Arrow) e.getDamager();
					if (a.getShooter() instanceof Player) {
						p = (Player) a.getShooter();
					} else {
						return;
					}
				}
				new BukkitRunnable() {
					@Override
					public void run() {
						double progress = Math.max((ent.getHealth()) / ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue(), 0.0);
						plugin.getBarManager().getBS(p).setInfo(Main.color("&c" + ent.getName() + "&8: &f" + dF.format(ent.getHealth())), BarColor.RED, BarStyle.SOLID, progress, true);
				        if (ent.getHealth() <= 0) {
				        	new BukkitRunnable() {
				        		public void run() {
				        			plugin.getBarManager().getBS(p).setInfo("", BarColor.RED, BarStyle.SOLID, 0.0, false);
				        		}
				        	}.runTaskLater(plugin, 20L);
				        }
					}
				}.runTaskLater(plugin, 1L);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void onJoinRPG (PlayerJoinEvent e) {
		File pFile = new File("plugins/Core/data/" + e.getPlayer().getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            pData.set("Username", e.getPlayer().getName());
            if (!pData.isSet("Attack Speed")) {
            	pData.set("Attack Speed", 16.0);
            }
            if (!pData.isSet("Level")) {
            	pData.set("Level", 1);
            }
            if (!pData.isSet("Exp")) {
            	pData.set("Exp", 0);
            }
            if (!pData.isSet("SP")) {
            	pData.set("SP", 0);
            }
            if (!pData.isSet("Mana")) {
            	pData.set("Mana", 5000);
            }
            if (!pData.isSet("AD")) {
            	pData.set("AD", 10.0);
            }
            if (!pData.isSet("AbilityOne")) {
            	pData.set("AbilityOne", "None");
            }
            if (!pData.isSet("AbilityTwo")) {
            	pData.set("AbilityTwo", "None");
            }
            if (!pData.isSet("AbilityThree")) {
            	pData.set("AbilityThree", "None");
            }
            if (!pData.isSet("AbilityFour")) {
            	pData.set("AbilityFour", "None");
            }
            if (!pData.isSet("Kills")) {
            	pData.set("Kills", 0);
            }
            if (!pData.isSet("Deaths")) {
            	pData.set("Deaths", 0);
            }
            pData.save(pFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        UUID uuid = e.getPlayer().getUniqueId();
        plugin.getManaMap().put(uuid, pData.getInt("Mana"));
        plugin.getManaRegenMap().put(uuid, plugin.getManaRegen(e.getPlayer()));
        plugin.getAdMap().put(uuid, pData.getDouble("AD"));
        plugin.getLevelMap().put(uuid, pData.getInt("Level"));
        plugin.getExpMap().put(uuid, pData.getInt("Exp"));
        plugin.getSPMap().put(uuid, pData.getInt("SP"));
        List<String> abs = new ArrayList<String>();
        abs.add(pData.getString("AbilityOne"));
        abs.add(pData.getString("AbilityTwo"));
        abs.add(pData.getString("AbilityThree"));
        abs.add(pData.getString("AbilityFour"));
        plugin.getAbilities().put(uuid, abs);
        
		e.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(pData.getDouble("Attack Speed"));
		e.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(pData.getDouble("AD"));
		if (e.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() < hp) {
			e.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
			e.getPlayer().setHealth(hp);
		}
		
		e.getPlayer().setHealthScale(20);
		
		if (!e.getPlayer().hasPlayedBefore()) {
			e.getPlayer().setHealth(hp);
			giveSpawnItems(e.getPlayer());
			welcomePlayer(e.getPlayer());
		}
		
		plugin.getBarManager().setBossBars(e.getPlayer(), new BS(e.getPlayer()));
		
	}

	public void welcomePlayer (Player p) {
		plugin.addJoins();
		Bukkit.getServer().broadcastMessage(Main.color("&8&l>>> &eWelcome &6" + p.getName() + " &8(#" + plugin.getJoins() + "&8)" + "&eto the server!"));
	}
	
	@EventHandler
	public void respawnItems (PlayerRespawnEvent e) {
		giveSpawnItems(e.getPlayer());
	}
	
	public void giveSpawnItems(Player p) {
		ItemStack codex = new ItemStack(Material.ENCHANTED_BOOK);
		ItemMeta meta = codex.getItemMeta();
		meta.setDisplayName(Main.color("&8» &bGuide Codex &8«"));
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add(Main.color("&7Right-Click for Help!"));
		lore.add("");
		meta.setLore(lore);
		codex.setItemMeta(meta);
		p.getInventory().setItem(8, codex);
		
		p.getInventory().setItem(0, new ItemStack(Material.WOODEN_SWORD));
		
		ItemStack bread = new ItemStack(Material.BREAD);
		bread.setAmount(8);
		p.getInventory().setItem(4, bread);
	}
	
}
