package com.java.rpg.classes;

import com.java.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Skillboard {

    private Main main = Main.getInstance();

    private UUID uuid;
    private Scoreboard board;
    private Objective obj;
    private List<Team> teams;
    private int i;

    public Skillboard(Player p) {
        uuid = p.getUniqueId();
        board = Bukkit.getScoreboardManager().getNewScoreboard();
        obj = board.registerNewObjective("ServerName", "dummy", Main.color("&e&lSkill Cooldowns"));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        teams = new ArrayList<>();
        p.setScoreboard(board);
        Score cds = obj.getScore(Main.color("&f<> ----- <>"));
        cds.setScore(15);
        i = 0;
    }

    public void updateScoreboard(Player p) {
        if (main.getPC().containsKey(p) && main.getPC().get(p).getCooldowns() != null) {
            for (String name : main.getPC().get(p).getCooldowns().keySet()) {
                if (i == 15) {
                    i = 0;
                }
                if (!teams.contains(board.getTeam(name))) {
                    teams.add(board.registerNewTeam(name));
                    if (board.getTeam(name).getEntries() == null) {
                        board.getTeam(name).addEntry(Main.color("&" + i + "a"));
                        //board.getTeam(name).addEntry(ChatColor.YELLOW + name);
                    }
                    board.getTeam(name).setPrefix(Main.color("&6" + name + "&e: &f" + main.getPC().get(p).getCooldown(main.getPC().get(p).getSkillFromName(name)).replace("CD:", "") + "s" ));
                    obj.getScore(Main.color("&" + i + "a")).setScore(i);
                    //obj.getScore(Main.color(ChatColor.YELLOW + name)).setScore(i);
                    if (!main.getPC().get(p).getCooldown(main.getPC().get(p).getSkillFromName(name)).contains("CD")) {
                        String rm = "";
                        for (String e : board.getTeam(name).getEntries()) {
                            rm = e;
                        }
                        if (rm != "") {
                            board.getTeam(name).removeEntry(rm);
                        }
                        teams.remove(board.getTeam(name));
                    }
                }
                i++;
            }
        }
        p.setScoreboard(board);
    }

}
