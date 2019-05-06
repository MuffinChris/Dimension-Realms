package com.core.java.rpgbase;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.core.java.rpgbase.bossbars.BS;
import com.core.java.rpgbase.bossbars.BossBarManager;
import com.core.java.rpgbase.player.Armor;
import com.core.java.essentials.Main;

public class RPGFunctions implements Listener {
	
	private Main plugin = Main.getInstance();
	private double hp = plugin.basehp;
	
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
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void sendHpRegain (EntityRegainHealthEvent e) {
		if (e.getEntity() instanceof Player) {
			Main.sendHp((Player) e.getEntity());
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Armor.updateSet(e.getPlayer(), Armor.getSet(e.getPlayer()));
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void bossbarHP (EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof Player)) {
				DecimalFormat dF = new DecimalFormat("#.##");
				LivingEntity ent = (LivingEntity) e.getEntity();
				Player p = (Player) e.getDamager();
				new BukkitRunnable() {
					@Override
					public void run() {
						double progress = Math.max((ent.getHealth()) / ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue(), 0.0);
						plugin.getBarManager().getBS(p).setInfo(Main.color("&c" + ent.getType() + "&8: &f" + dF.format(ent.getHealth())), BarColor.RED, BarStyle.SOLID, progress, true);
				        if (ent.getHealth() <= 0) {
				        	plugin.getBarManager().getBS(p).setInfo("", BarColor.RED, BarStyle.SOLID, 0.0, false);
				        }
					}
				}.runTaskLater(plugin, 1L);
			} else if (e.getEntity() instanceof Player) {
				DecimalFormat dF = new DecimalFormat("#.##");
				Player ent = (Player) e.getEntity();
				Player p = (Player) e.getDamager();
				new BukkitRunnable() {
					@Override
					public void run() {
						double progress = Math.max((ent.getHealth()) / ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue(), 0.0);
						plugin.getBarManager().getBS(p).setInfo(Main.color("&c" + ent.getName() + "&8: &f" + dF.format(ent.getHealth())), BarColor.RED, BarStyle.SOLID, progress, true);
				        if (ent.getHealth() <= 0) {
				        	plugin.getBarManager().getBS(p).setInfo("", BarColor.RED, BarStyle.SOLID, 0.0, false);
				        }
					}
				}.runTaskLater(plugin, 1L);
			}
		}
	}
	
	@EventHandler
	public void onJoinRPG (PlayerJoinEvent e) {
		File pFile = new File("plugins/Core/data/" + e.getPlayer().getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            pData.set("Username", e.getPlayer().getName());
            if (!pData.isSet("Attack Speed")) {
            	pData.set("Attack Speed", 6.0);
            }
            if (!pData.isSet("Mana")) {
            	pData.set("Mana", 5000);
            }
            if (!pData.isSet("AD")) {
            	pData.set("AD", 10.0);
            }
            pData.save(pFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        UUID uuid = e.getPlayer().getUniqueId();
        plugin.getManaMap().put(uuid, pData.getInt("Mana"));
        plugin.getManaRegenMap().put(uuid, plugin.getManaRegen(e.getPlayer()));
        plugin.getAdMap().put(uuid, pData.getDouble("AD"));
        
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
		}
		
		plugin.getBarManager().setBossBars(e.getPlayer(), new BS(e.getPlayer()));
		
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
