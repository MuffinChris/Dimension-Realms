package com.java.rpg.player;

import com.java.Main;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onDamage (EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Main.sendHp((Player) e.getEntity());
        }
    }

    @EventHandler
    public void onDamageTwo (EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            BlockData blood = Material.REDSTONE_BLOCK.createBlockData();
            e.getEntity().getWorld().spawnParticle(Particle.BLOCK_DUST, e.getEntity().getLocation(), 100, 0.5, 1, 0.5, blood);
            e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.BLOCK_GRASS_BREAK, 0.5F, 0.5F);
        }
    }

}
