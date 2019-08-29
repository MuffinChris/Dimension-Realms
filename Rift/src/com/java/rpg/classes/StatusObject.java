package com.java.rpg.classes;

import com.java.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatusObject {

    private String name;
    private String flavor;

    private List<StatusValue> statuses;

    public boolean active() {
        return (!statuses.isEmpty());
    }

    public String getName() {
        return name;
    }

    public String getFlavor() {
        return flavor;
    }

    public int getValue() {
        int value = 0;
        for (StatusValue s : statuses) {
            value+=s.getValue();
        }
        return value;
    }

    public int ticksMore() {
        int ticks = 0;
        for (StatusValue s : statuses) {
            int ticksleft = s.getDuration() - Integer.valueOf(String.valueOf(Math.round(20 * 0.001 * (System.currentTimeMillis() - s.getTimestamp()))));
            ticks+=ticksleft;
        }
        return ticks;
    }

    public void clearBasedTitle(String title, Player p) {
        List<StatusValue> remove = new ArrayList<>();
        for (StatusValue s : statuses) {
            if (s.getDurationless()) {
                if (s.getSource().equals(title + ":" + p.getDisplayName())) {
                    remove.add(s);
                }
            }
        }
        for (StatusValue rem : remove) {
            statuses.remove(rem);
        }
    }

    public StatusObject(String name, String flavor) {
        this.name = name;
        this.flavor = flavor;
        statuses = new ArrayList<>();
    }

    public List<StatusValue> getStatuses() {
        return statuses;
    }

    public void scrub() {
        name = null;
        for (StatusValue s : statuses) {
            s.scrub();
        }
        statuses.clear();
        statuses = null;
    }

}
