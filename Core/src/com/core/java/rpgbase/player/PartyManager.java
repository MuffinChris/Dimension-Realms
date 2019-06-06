package com.core.java.rpgbase.player;

import com.core.java.essentials.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class PartyManager {

    public List<Party> parties;

    public PartyManager() {
        parties = new ArrayList<Party>();
        new BukkitRunnable() {
            @Override
            public void run() {
                cleanParties();
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 1L, 200L);
    }

    public void cleanParties() {
        for (int z = parties.size(); z >= 0; z--) {
            if (!(parties.get(z).getLeader() instanceof Player)) {
                parties.remove(parties.get(z));
            }
        }
    }

    public List<Party> getParties() {
        return parties;
    }

}
