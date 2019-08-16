package com.java.holograms;

import com.java.Main;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;

public class Hologram implements Listener {

    private Main main = Main.getInstance();

    private ArmorStand stand;
    private String text;

    public Hologram() {

    }

    @EventHandler
    public void manipulate(PlayerArmorStandManipulateEvent e)
    {
        if(!e.getRightClicked().isVisible())
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void damageStand(EntityDamageEvent e) {
        if (e.getEntity() instanceof ArmorStand && !((ArmorStand) e.getEntity()).isVisible()) {
            e.setCancelled(true);
        }
    }

    public Hologram(Location loc, String text) {
        this.text = text;
        stand = (ArmorStand) loc.getWorld().spawnEntity(new Location(loc.getWorld(), 0, 0, 0), EntityType.ARMOR_STAND);
        stand.setVisible(false);
        stand.teleport(loc);
        stand.setCustomNameVisible(true);
        stand.setCustomName(Main.color(this.text));
        stand.setAI(false);
        stand.setCollidable(false);
        stand.setInvulnerable(true);
        stand.setGravity(false);
        stand.setCanPickupItems(false);
    }

    public void rise() {
        new BukkitRunnable() {
            int times = 0;
            public void run() {
                stand.teleport(stand.getLocation().add(new Vector(0, 0.05, 0)));
                times++;
                if (times * 0.05 >= 1) {
                    destroy();
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 1L, 1L);
    }

    public void destroy() {
        stand.remove();
        text = null;
    }

}
