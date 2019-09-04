package com.java;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import java.text.DecimalFormat;

public class Sleep implements Listener {

    private Main main = Main.getInstance();

    @EventHandler
    public void onSleep (PlayerBedEnterEvent e) {
        if (e.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            main.setSleepers(main.getSleepers() + 1);
            if (main.getSleepers() * 4 >= Bukkit.getOnlinePlayers().size()) {
                Bukkit.broadcastMessage(Main.color("&8\u00BB &e" + main.getSleepers() + " &6/ &e" + Bukkit.getOnlinePlayers().size() + " &fare sleeping, it is now &edaytime&f!"));
                e.getPlayer().getWorld().setTime(0);
                main.setSleepers(0);
            } else {
                DecimalFormat dF = new DecimalFormat("#");
                Bukkit.broadcastMessage(Main.color("&8\u00BB &e" + main.getSleepers() + " &6/ &e" + Bukkit.getOnlinePlayers().size() + " &fare sleeping."));
                Bukkit.broadcastMessage(Main.color("&8\u00BB &e" + dF.format(Math.max(1, Math.ceil(Bukkit.getOnlinePlayers().size() / 4.0))) + " &fplayers TOTAL need to sleep for daytime!"));
            }
        }
    }

    @EventHandler
    public void onLeave (PlayerBedLeaveEvent e) {
        main.setSleepers(Math.max(0, main.getSleepers() - 1));
    }

}
