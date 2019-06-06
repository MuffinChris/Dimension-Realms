package com.core.java.rpgbase;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
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

import com.core.java.essentials.Main;
import com.core.java.rpgbase.bossbars.BS;
import com.core.java.rpgbase.entities.PlayerList;
import com.core.java.rpgbase.player.Armor;
import com.core.java.rpgbase.player.Weapons;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

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
		if (plugin.getGodMode().containsKey(uuid)) {
			plugin.getGodMode().remove(uuid);
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
					infodmg.add(Main.color("&7" + pl.getName() + "&8: &c" + plugin.getPManager().getPList(e.getEntity()).getDamage(pl) + "&f\n"));
				}
				TextComponent info = new TextComponent(Main.color("&8[&4X&8] &c" + p.getName() + " &fwas slain by &c" + p.getKiller().getName() + " &8<&cINFO&8>"));
				//BaseComponent[] info = new ComponentBuilder("INFO").color(ChatColor.RED)
				//	    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent(infodmg.toString()))).create();
				info.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(infodmg.toString()).create()));
				//Main.so("&8[&4X&8] &c" + p.getName() + " &fwas slain by &c" + p.getKiller().getName());
				//Bukkit.getServer().broadcastMessage("");
				Bukkit.getServer().broadcast(info);
				e.setDeathMessage("");
			} else {
				e.setDeathMessage(Main.color("&8[&4X&8] &c" + p.getName() + " &fwas slain by &c" + p.getKiller().getName()));
			}
		} else {
			if (e.getEntity().getKiller() instanceof Entity) {
				e.setDeathMessage(Main.color("&8[&4X&8] &c" + p.getName() + " &fwas slain by &c" + p.getKiller().getName()));
			} else if (e.getEntity().getKiller() instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e.getEntity().getKiller();
				e.setDeathMessage(Main.color("&8[&4X&8] &c" + p.getName() + " &fwas slain by &c" + event.getDamager().getName()));
			} else {
				e.setDeathMessage(Main.color("&8[&4X&8] &c" + p.getName() + " &fdied!"));
			}
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
	
	@EventHandler (priority = EventPriority.HIGH)
	public void bossbarDmgAgain (EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Main.sendHp((Player) e.getEntity());
		}
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
        		if (p.isOnline()) {
					if (plugin.getBarManager().getBS(p).getTarget() == e) {
						plugin.getBarManager().getBS(p).setInfo(e, "", BarColor.RED, BarStyle.SOLID, 0.0, false, true);
					}
				}
        	}
        }.runTaskLater(plugin, 40L);
	}
	
	@EventHandler
	public void bossbarHPInit (EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player || e.getDamager() instanceof Projectile) {
			if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof Player) && e.getEntity().getType() != EntityType.ARMOR_STAND) {
				DecimalFormat dF = new DecimalFormat("#.##");
				LivingEntity ent = (LivingEntity) e.getEntity();
				Player p;
				if (e.getDamager() instanceof Player) {
					p = (Player) e.getDamager();
				} else {
					Projectile a = (Projectile) e.getDamager();
					if (a.getShooter() instanceof Player) {
						p = (Player) a.getShooter();
					} else {
						return;
					}
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
					Projectile a = (Projectile) e.getDamager();
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
		plugin.presetPlayer(e.getPlayer());
		plugin.hashmapUpdate(e.getPlayer());
		e.getPlayer().getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.8);
		if (e.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() < hp) {
			e.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
			e.getPlayer().setHealth(hp);
		}
		
		e.getPlayer().setHealthScale(40);
		
		if (!e.getPlayer().hasPlayedBefore()) {
			giveSpawnItems(e.getPlayer(), true);
			welcomePlayer(e.getPlayer());
		}
		
		if (!plugin.getPManager().getPList().containsKey(e.getPlayer())) {
			plugin.getPManager().getPList().put(e.getPlayer(), new PlayerList());
		}
		plugin.getBarManager().setBossBars(e.getPlayer(), new BS(e.getPlayer()));
		Armor.updateSet(e.getPlayer());
		Weapons.updateInventory(e.getPlayer());
		plugin.getGodMode().put(e.getPlayer().getUniqueId(), false);
	}

	public void welcomePlayer (Player p) {
		plugin.addJoins();
		Bukkit.getServer().broadcastMessage(Main.color("&6&l>>> &eWelcome &6" + p.getName() + " &8(#" + plugin.getJoins() + "&8) " + "&eto the server!"));
	}
	
	@EventHandler
	public void respawnItems (PlayerRespawnEvent e) {
		giveSpawnItems(e.getPlayer(), false);
	}
	
	public void giveSpawnItems(Player p, boolean newplayer) {
		
		p.getInventory().setItem(0, new ItemStack(Material.WOODEN_SWORD));
		Weapons.updateInventory(p);
		ItemStack bread = new ItemStack(Material.BREAD);
		bread.setAmount(4);
		
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
			
			bread.setAmount(16);
		}
		
		p.getInventory().setItem(4, bread);
	}
	
}
