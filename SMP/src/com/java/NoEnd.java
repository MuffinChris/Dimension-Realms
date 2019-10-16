package com.java;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class NoEnd implements Listener {

    @EventHandler
    public void end (PlayerPortalEvent e) {
        /*if (e.getTo().getWorld().getName().equalsIgnoreCase("world_the_end")) {
            e.setCancelled(true);
            Main.msg(e.getPlayer(), "&cYou cannot go to the end right now!");
        }*/
    }

}
