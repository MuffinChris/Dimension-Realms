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
import org.bukkit.Server;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.core.java.rpgbase.bossbars.BS;
import com.core.java.rpgbase.entities.PlayerList;
import com.core.java.rpgbase.player.Armor;
import com.core.java.rpgbase.player.Weapons;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import com.core.java.essentials.Main;

public class RPGFunctions implements Listener {
	
	private Main plugin = Main.getInstance();
	private double hp = Armor.basehp;
	
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
        if (plugin.getMRMap().containsKey(uuid)) {
        	plugin.getMRMap().remove(uuid);
        }
        if (plugin.getArmorMap().containsKey(uuid)) {
        	plugin.getArmorMap().remove(uuid);
        }
        
        File pFile = new File("plugins/Core/data/" + e.getPlayer().getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            pData.set("Cmana", plugin.getCManaMap().get(uuid));
            pData.save(pFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        
        if (plugin.getCManaMap().containsKey(uuid)) {
        	plugin.getCManaMap().remove(uuid);
        }
        if (plugin.getBarManager().getBossBars().containsKey(uuid)) {
        	plugin.getBarManager().getBossBars().remove(uuid);
        }
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void sendHpRegain (EntityRegainHealthEvent e) {
		if (e.getEntity() instanceof Player) {
			Main.sendHp((Player) e.getEntity());
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void deathMessages (PlayerDeathEvent e) {
		Player p = (Player) e.getEntity();
		if (e.getEntity() instanceof Player && e.getEntity().getKiller() instanceof Player) {
			if (plugin.getPManager().getPList(e.getEntity()) instanceof PlayerList) {
				List<String> infodmg = new ArrayList<String>();
				for (Player pl : plugin.getPManager().getPList(e.getEntity()).getPlayers()) {
					infodmg.add(Main.color("&c" + pl.getName() + "&8: &f" + plugin.getPManager().getPList(e.getEntity()).getDamage(pl) + "\n"));
				}
				TextComponent info = new TextComponent("INFO");
				info.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(infodmg.toString()).create()));
				Main.so("&8[&4X&8] &c" + p.getName() + " &fwas slain by &c" + p.getKiller().getName());
				e.setDeathMessage("&8[&4X&8] &c" + p.getName() + " &fwas slain by &c" + p.getKiller().getName() + " &8<&c" + info + "&8>");
			} else {
				Main.so("&8[&4X&8] &c" + p.getName() + " &fwas slain by &c" + p.getKiller().getName());
				e.setDeathMessage("&8[&4X&8] &c" + p.getName() + " &fwas slain by &c" + p.getKiller().getName());
			}
		} else {
			Main.so("&8[&4X&8] &c" + p.getName() + " &fwas slain by &c" + p.getKiller().getName());
			e.setDeathMessage("&8[&4X&8] &c" + p.getName() + " &fwas slain by &c" + p.getKiller().getName());
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		new BukkitRunnable() {
			public void run() {
				Armor.updateSet(e.getPlayer());
			}
		}.runTaskLater(plugin, 5L);
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void entityAddDamage (EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if (plugin.getPManager().getPList(e.getEntity()) != null) {
				plugin.getPManager().getPList(e.getEntity()).addDamage(p, e.getDamage());
			} else {
				PlayerList pl = new PlayerList();
				pl.getList().put((Player) e.getDamager(), e.getDamage());
				plugin.getPManager().setPlayerList(e.getEntity(), pl);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onHeal (EntityRegainHealthEvent e) {
		if (e.getEntity() instanceof LivingEntity) {
			if (plugin.getPManager().getPList(e.getEntity()) != null && plugin.getPManager().getPList(e.getEntity()).getPlayers() != null) {
				DecimalFormat dF = new DecimalFormat("#.##");
				LivingEntity ent = (LivingEntity) e.getEntity();
				for (Player p : plugin.getPManager().getPList(e.getEntity()).getPlayers()) {
					new BukkitRunnable() {
						@Override
						public void run() {
							if (plugin.getBarManager().getBS(p) != null && plugin.getBarManager().getBS(p).getTitle().length() > 1 && plugin.getBarManager().getBS(p).getTarget() == e.getEntity()) {
								double progress = Math.max((ent.getHealth()) / ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue(), 0.0);
								String name = ent.getName();
								if (ent.getCustomName() instanceof String) {
									name = ent.getCustomName();
								}
								plugin.getBarManager().getBS(p).setInfo(e.getEntity(), Main.color("&c" + name + "&8: &f" + dF.format(ent.getHealth())), BarColor.RED, BarStyle.SOLID, progress, true, false);
						        if (ent.getHealth() <= 0) {
						        	cleanOnDeath(p, e.getEntity());
						        }
							}
						}
					}.runTaskLater(plugin, 1L);
				}
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void bossbarDmgAgain (EntityDamageEvent e) {
		if (e.getEntity() instanceof LivingEntity) {
			if (plugin.getPManager().getPList(e.getEntity()) != null && plugin.getPManager().getPList(e.getEntity()).getPlayers() != null) {
				DecimalFormat dF = new DecimalFormat("#.##");
				LivingEntity ent = (LivingEntity) e.getEntity();
				for (Player p : plugin.getPManager().getPList(e.getEntity()).getPlayers()) {
					new BukkitRunnable() {
						@Override
						public void run() {
							if (plugin.getBarManager().getBS(p) != null && plugin.getBarManager().getBS(p).getTitle().length() > 1 && plugin.getBarManager().getBS(p).getTarget() == e.getEntity()) {
								double progress = Math.max((ent.getHealth()) / ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue(), 0.0);
								plugin.getBarManager().getBS(p).setInfo(e.getEntity(), Main.color("&c" + ent.getName() + "&8: &f" + dF.format(ent.getHealth())), BarColor.RED, BarStyle.SOLID, progress, true, false);
								if (ent.getHealth() <= 0) {
									cleanOnDeath(p, e.getEntity());
								}
							}
						}
					}.runTaskLater(plugin, 1L);
				}
			}
		}
	}
	
	public void cleanOnDeath(Player p, Entity e) {
		new BukkitRunnable() {
        	public void run() {
	        		if (plugin.getBarManager().getBS(p).getTarget() == e) {
	        			plugin.getBarManager().getBS(p).setInfo(e, "", BarColor.RED, BarStyle.SOLID, 0.0, false, true);
	        		}
        		}
        }.runTaskLater(plugin, 20L);
	}
	
	@EventHandler (priority = EventPriority.HIGH)
	public void bossbarHPInit (EntityDamageByEntityEvent e) {
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
				if (plugin.getPManager().getPList(e.getEntity()) != null) {
					plugin.getPManager().getPList(e.getEntity()).addDamage(p, e.getDamage());
				} else {
					PlayerList pl = new PlayerList();
					pl.getList().put((Player) e.getDamager(), e.getDamage());
					plugin.getPManager().setPlayerList(e.getEntity(), pl);
				}
				BlockData blood = Material.REDSTONE_BLOCK.createBlockData();
				ent.getWorld().spawnParticle(Particle.BLOCK_DUST, ent.getLocation(), 100, 0.5, 1, 0.5, blood);
				new BukkitRunnable() {
					@Override
					public void run() {
						double progress = Math.max((ent.getHealth()) / ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue(), 0.0);
						String name = ent.getName();
						if (ent.getCustomName() instanceof String) {
							name = ent.getCustomName();
						}
						plugin.getBarManager().getBS(p).setInfo(e.getEntity(), Main.color("&c" + name + "&8: &f" + dF.format(ent.getHealth())), BarColor.RED, BarStyle.SOLID, progress, true, true);
					    if (ent.getHealth() <= 0) {
					    	cleanOnDeath(p, e.getEntity());
					     }
					}
				}.runTaskLater(plugin, 2L);
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
				if (plugin.getPManager().getPList(e.getEntity()) != null) {
					plugin.getPManager().getPList(e.getEntity()).addDamage(p, e.getDamage());
				} else {
					PlayerList pl = new PlayerList();
					pl.getList().put((Player) e.getDamager(), e.getDamage());
					plugin.getPManager().setPlayerList(e.getEntity(), pl);
				}
				new BukkitRunnable() {
					@Override
					public void run() {
						double progress = Math.max((ent.getHealth()) / ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue(), 0.0);
						plugin.getBarManager().getBS(p).setInfo(e.getEntity(), Main.color("&c" + ent.getName() + "&8: &f" + dF.format(ent.getHealth())), BarColor.RED, BarStyle.SOLID, progress, true, true);
				        if (ent.getHealth() <= 0) {
				        	cleanOnDeath(p, e.getEntity());
				        }
					}
				}.runTaskLater(plugin, 2L);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void onJoinRPG (PlayerJoinEvent e) {
		File pFile = new File("plugins/Core/data/" + e.getPlayer().getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            pData.set("Username", e.getPlayer().getName());
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
            if (!pData.isSet("ManaRegen")) {
            	pData.set("ManaRegen", 2);
            }
            if (!pData.isSet("Cmana")) {
            	pData.set("Cmana", 0);
            }
            if (!pData.isSet("AD")) {
            	pData.set("AD", 1.0);
            }
            if (!pData.isSet("HP")) {
            	pData.set("HP", 0.0);
            }
            if (!pData.isSet("AttackSpeed")) {
            	pData.set("AttackSpeed", 4.0);
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
            if (!pData.isSet("SPAD")) {
            	pData.set("SPAD", 0);
            }
            if (!pData.isSet("SPHP")) {
            	pData.set("SPHP", 0);
            }
            if (!pData.isSet("SPM")) {
            	pData.set("SPM", 0);
            }
            if (!pData.isSet("SPMR")) {
            	pData.set("SPMR", 0);
            }
            pData.save(pFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        UUID uuid = e.getPlayer().getUniqueId();
        plugin.getManaMap().put(uuid, pData.getInt("Mana"));
        plugin.getManaRegenMap().put(uuid, pData.getInt("ManaRegen"));
        plugin.getAdMap().put(uuid, pData.getDouble("AD"));
        plugin.getLevelMap().put(uuid, pData.getInt("Level"));
        plugin.getExpMap().put(uuid, pData.getInt("Exp"));
        plugin.getSPMap().put(uuid, pData.getInt("SP"));
        plugin.getCManaMap().put(uuid, pData.getInt("Cmana"));
        List<String> abs = new ArrayList<String>();
        abs.add(pData.getString("AbilityOne"));
        abs.add(pData.getString("AbilityTwo"));
        abs.add(pData.getString("AbilityThree"));
        abs.add(pData.getString("AbilityFour"));
        plugin.getAbilities().put(uuid, abs);
        
		plugin.updateAttackSpeed(e.getPlayer());
		e.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(pData.getDouble("AD"));
		e.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(pData.getDouble("AttackSpeed"));
		e.getPlayer().getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.8);
		if (e.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() < hp) {
			e.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
			e.getPlayer().setHealth(hp);
		}
		
		e.getPlayer().setHealthScale(20);
		
		if (!e.getPlayer().hasPlayedBefore()) {
			e.getPlayer().setHealth(hp);
			giveSpawnItems(e.getPlayer(), true);
			welcomePlayer(e.getPlayer());
		}
		
		if (!plugin.getPManager().getPList().containsKey(e.getPlayer())) {
			plugin.getPManager().getPList().put(e.getPlayer(), new PlayerList());
		}
		plugin.getBarManager().setBossBars(e.getPlayer(), new BS(e.getPlayer()));
		Armor.updateSet(e.getPlayer());
		
	}

	public void welcomePlayer (Player p) {
		plugin.addJoins();
		Bukkit.getServer().broadcastMessage(Main.color("&8&l>>> &eWelcome &6" + p.getName() + " &8(#" + plugin.getJoins() + "&8) " + "&eto the server!"));
	}
	
	@EventHandler
	public void respawnItems (PlayerRespawnEvent e) {
		giveSpawnItems(e.getPlayer(), false);
	}
	
	public void giveSpawnItems(Player p, boolean newplayer) {
		
		p.getInventory().setItem(0, new ItemStack(Material.WOODEN_SWORD));
		Weapons.updateMainHand(p);
		ItemStack bread = new ItemStack(Material.BREAD);
		bread.setAmount(8);
		
		if (newplayer) {
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
			
			bread.setAmount(12);
		}
		
		p.getInventory().setItem(4, bread);
	}
	
}
