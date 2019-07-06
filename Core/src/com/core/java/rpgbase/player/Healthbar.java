package com.core.java.rpgbase.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Healthbar implements Listener {

    Scoreboard board;

    public Healthbar() {

    }

    public Healthbar(Player p) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();

        board = manager.getNewScoreboard();
        Objective health = board.registerNewObjective("health", "player", "hb");
        health.setDisplaySlot(DisplaySlot.BELOW_NAME);
        health.setDisplayName("&c" + p.getHealth() + " HP");
    }

    @EventHandler
    public void onJoin (PlayerJoinEvent e) {
        new Healthbar(e.getPlayer());
    }

}
