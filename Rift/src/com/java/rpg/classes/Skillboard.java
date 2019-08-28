package com.java.rpg.classes;

import com.java.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.UUID;

public class Skillboard {

    private Main main = Main.getInstance();

    private UUID uuid;
    private BossBar bossbar;
    private BossBar bossbar2;
    private boolean skillbar;
    List<Integer> prefixes = new ArrayList<>();

    private Objective obj;

    public boolean getSkillbar() {
        return skillbar;
    }

    public void setSkillbar(boolean bool) {
        skillbar = bool;
    }

    public void toggleSkillbar() {
        skillbar = !skillbar;
    }

    public void updateSkillbar() {
        if (skillbar) {
            String output = "";
            int slot = 2;
            for (Skill s : main.getRP(Bukkit.getPlayer(uuid)).getPClass().getSkills()) {
                output+="&e" + s.getName() + " &8<&6" + slot + "&8> || ";
                slot++;
            }
            if (output.contains("||")) {
                output = output.substring(0, output.length() - 4);
            }
            bossbar.setTitle(Main.color(output));
            bossbar2.setVisible(true);
            bossbar.setVisible(true);
        } else {
            bossbar.setTitle("");
            bossbar2.setVisible(false);
            bossbar.setVisible(false);
        }
    }

    public void setScoreBoard(Player player) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        obj = board.registerNewObjective("ServerName", "dummy", Main.color("&e&lSkill Cooldowns"));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score cds = obj.getScore(Main.color(""));
        cds.setScore(15);
        prefixes = new ArrayList<>();
        prefixes.add(9);
        prefixes.add(8);
        prefixes.add(7);
        prefixes.add(6);
        prefixes.add(5);
        prefixes.add(4);
        for (int i : prefixes) {
            Team skillOne = board.registerNewTeam("skill" + i);
            skillOne.addEntry(Main.color("&" + i));
            skillOne.setPrefix(Main.color(""));
            obj.getScore(Main.color("&" + i)).setScore(i);
        }

        player.setScoreboard(board);
    }

    public void updateScoreboard(Player p) {
        Scoreboard board = p.getScoreboard();
        for (int i = 9; i > 9 - main.getPC().get(p.getUniqueId()).getCooldowns().size(); i--) {
            int index = 9 - i;
            String name = main.getPC().get(p.getUniqueId()).getCooldowns().keySet().toArray()[index].toString();
            String output = "";
            output += "&e" + name + ": &f" + main.getPC().get(p.getUniqueId()).getCooldown(main.getPC().get(p.getUniqueId()).getSkillFromName(name)).replace("CD:", "") + "s";
            board.getTeam("skill" + i).setPrefix(Main.color(output));
            obj.getScore(Main.color("&" + i)).setScore(i);
        }
        for (int i = 9 - main.getPC().get(p.getUniqueId()).getCooldowns().size(); i >= 4; i--) {
            board.getTeam("skill" + i).setPrefix("");
        }
    }

    public Skillboard(Player p) {
        skillbar = false;
        uuid = p.getUniqueId();
        bossbar2 = Bukkit.getServer().createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
        bossbar = Bukkit.getServer().createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
        uuid = p.getUniqueId();
        bossbar2.addPlayer(p);
        bossbar.addPlayer(p);
        setScoreBoard(p);
        update();
        bossbar2.setVisible(false);
        bossbar.setVisible(false);
    }

    public void scrub() {
        uuid = null;
        bossbar = null;
        bossbar2 = null;
        prefixes = null;
    }

    public void update() {
        Player p = Bukkit.getPlayer(uuid);
        if (main.getPC().get(uuid) instanceof RPGPlayer && main.getPC().get(uuid).getPClass() instanceof PlayerClass) {
            /*double mana = main.getMana(p);
            double maxmana = main.getPC().get(uuid).getPClass().getCalcMana(main.getPC().get(uuid).getLevel());
            double manaprogress = Math.max((1.0D * mana) / (1.0D * maxmana), 0.0);
            bossbar.setProgress(Math.min(manaprogress, 1.0));
            String title = "";
            for (int i = main.getPC().get(uuid).getCooldowns().keySet().size() - 1; i >= 0; i--) {
                String name = main.getPC().get(uuid).getCooldowns().keySet().toArray()[i].toString();
                if (main.getPC().get(uuid).getCooldown(main.getPC().get(uuid).getSkillFromName(name)).contains("CD:")) {
                    if (title != "") {
                        title += "  ";
                    }
                    title += Main.color("&e" + name + ": &f" + main.getPC().get(uuid).getCooldown(main.getPC().get(uuid).getSkillFromName(name)).replace("CD:", "") + "s");
                }
            }
            bossbar.setTitle(title);*/
            updateScoreboard(p);
        }
    }

    public void updateWarmup(Skill s, int warmup) {
        /*
        bossbar.setTitle(Main.color("&bChanneling ") + s.getName() + "...");
        new BukkitRunnable() {
            public void run() {
                if (bossbar.getTitle().equalsIgnoreCase(Main.color("&bChanneling ") + s.getName() + "...")) {
                    bossbar.setTitle("");
                }
            }
        }.runTaskLater(Main.getInstance(), warmup);
        */

    }

    public void updateCast(Skill s) {
        /*
        bossbar.setTitle(Main.color("&bCasted ") + s.getName() + "...");
        new BukkitRunnable() {
            public void run() {
                if (bossbar.getTitle().equalsIgnoreCase(Main.color("&bCasted ") + s.getName() + "...")) {
                    bossbar.setTitle("");
                }
            }
        }.runTaskLater(Main.getInstance(), 20L);*/
    }

    public void clearChannel() {
        /*
        if (bossbar.getTitle().contains("Channeling")) {
            bossbar.setTitle("");
        }*/
    }

    public void clearBoard() {
        /*
        bossbar.setTitle("");
        */

    }

    public void updateToggle(Skill s) {
        /*
        if (bossbar.getTitle().equals("")) {
            bossbar.setTitle(Main.color("&bEnabled ") + s.getName() + "");
        }*/
    }

    public void endToggle(Skill s) {/*
        if (bossbar.getTitle().contains("Enabled")) {
            bossbar.setTitle("");
        }*/
    }


}
