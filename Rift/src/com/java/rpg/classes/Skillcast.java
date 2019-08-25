package com.java.rpg.classes;

import com.java.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Skillcast implements Listener {

    private Main main = Main.getInstance();

    //private Map<UUID, List<Integer>> lastSlot = new HashMap<>();

    /*public Skillcast() {
        new BukkitRunnable() {
            public void run() {

            }
        }.runTaskTimer(Main.getInstance(), 10, 2);
    }*/

    @EventHandler
    public void skillBar (PlayerSwapHandItemsEvent e) {
        if (!e.getPlayer().isSneaking() && main.getRP(e.getPlayer()).getPClass().getSkills().size() > 0) {
            e.setCancelled(true);
            main.getRP(e.getPlayer()).getBoard().toggleSkillbar();
            main.getRP(e.getPlayer()).getBoard().updateSkillbar();
            e.getPlayer().getInventory().setHeldItemSlot(0);
        }
    }

    @EventHandler
    public void skillBarCast (PlayerItemHeldEvent e) {
        if (main.getRP(e.getPlayer()).getBoard().getSkillbar()) {
            if (e.getNewSlot() == 0) {
                return;
            }
            int slot = e.getNewSlot();
            /*if (lastSlot.containsKey(e.getPlayer().getUniqueId()) && slot == (int) (lastSlot.get(e.getPlayer().getUniqueId()).toArray()[0])) {
                return;
            }*/
            if (slot > main.getRP(e.getPlayer()).getPClass().getSkills().size()) {
                return;
            }
            e.setCancelled(true);
            /*List<Integer> list = new ArrayList<>();
            list.add(e.getPreviousSlot());
            list.add(slot);
            lastSlot.put(e.getPlayer().getUniqueId(), e.getPreviousSlot());*/
            e.getPlayer().performCommand("skill " + ((Skill) main.getRP(e.getPlayer()).getPClass().getSkills().toArray()[slot - 1]).getName());
        }
    }

}
