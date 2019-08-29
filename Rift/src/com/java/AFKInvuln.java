package com.java;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AFKInvuln implements Listener {


    Map<UUID, Location> hasMoved = new HashMap<>();

    @EventHandler
    public void onJoin (PlayerJoinEvent e) {
        if (hasMoved.containsKey(e.getPlayer())) {
            hasMoved.remove(e.getPlayer());
        }
        hasMoved.put(e.getPlayer().getUniqueId(), e.getPlayer().getLocation());
        e.getPlayer().setCollidable(false);
        Main.msg(e.getPlayer(), "   &aLogin Shield Enabled.");
        new BukkitRunnable() {
            public void run() {
                if (hasMoved.containsKey(e.getPlayer().getUniqueId())) {
                    if (haveTheyMoved(e.getPlayer())) {
                        hasMoved.remove(e.getPlayer().getUniqueId());
                        Main.msg(e.getPlayer(), "   &cLogin Shield Disabled.");
                        e.getPlayer().setCollidable(true);
                        cancel();
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 5);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void invuln (EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (hasMoved.containsKey(p.getUniqueId())) {
                if (haveTheyMoved(p)) {
                    hasMoved.remove(p.getUniqueId());
                    Main.msg(p, "   &cLogin Shield Disabled.");
                    p.setCollidable(true);
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void rp (PlayerResourcePackStatusEvent e) {
        if (e.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED) {
            Main.msg(e.getPlayer(), "&c&lIt is high recommended you use the resource pack! If you wish to do so, edit this server's settings to Enable resource packs.");
        }
        if (e.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
            Main.msg(e.getPlayer(), "&c&lFailed to download resource pack.");
        }
        /*if (e.getStatus() == PlayerResourcePackStatusEvent.Status.ACCEPTED) {
            Main.msg(e.getPlayer(), "&a&lResource Pack Enabled!");
        }*/
    }

    public boolean haveTheyMoved(Player p) {
        if (hasMoved.containsKey(p.getUniqueId())) {
            Location loc = hasMoved.get(p.getUniqueId());
            Location lo = p.getLocation();
            return !(loc.getX() == lo.getX() && loc.getZ() == lo.getZ() && loc.getY() == lo.getY() && loc.getPitch() == lo.getPitch() && loc.getYaw() == lo.getYaw());
        }
        return true;
    }

}
