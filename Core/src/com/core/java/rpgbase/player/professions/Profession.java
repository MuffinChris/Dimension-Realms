package com.core.java.rpgbase.player.professions;

import com.core.java.Constants;
import com.core.java.essentials.Main;
import com.destroystokyo.paper.Title;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class Profession {

    private String name;
    private int maxlevel;
    private int level;
    private double exp;
    private String desc;

    public String getDesc() {
        return desc;
    }

    public String getProgress() {
        DecimalFormat dF = new DecimalFormat("#.##");
        return dF.format((getLevel() * 100.0F) / (getMaxLevel() * 1.0F)) + "%";
    }

    public double getRawProgress() {
        return (getLevel() * 1.0F) / (getMaxLevel() * 1.0F);
    }

    public Profession(String n, int ml, int lvl, double xp, String desc) {
        name = n;
        maxlevel = ml;
        level = lvl;
        exp = xp;
        this.desc = desc;
    }

    public void giveExp(double xp, Player p, boolean silent) {
        exp+=xp;
        DecimalFormat df = new DecimalFormat("#");
        if (!silent) {
            Main.msg(p, "&7[+" + df.format(xp) + " (" + name + " <" + getPercent() + ">) XP]");
        }
        levelup(p);
    }

    public void setExp(double xp, Player p) {
        exp=xp;
        levelup(p);
    }

    public double getMaxExp(int lvl) {
        return 20 * Math.pow(lvl, 2) + 500;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxLevel() {
        return maxlevel;
    }

    public double getExp() {
        return exp;
    }

    public void setLevel(int i) {
        level = i;
    }

    public void setExp(double d) {
        exp = d;
    }

    public double getRawPercent() {
        return getExp() / getMaxExp(level);
    }

    public void levelup(Player p) {
        int init = level;
        while (exp >= getMaxExp(level) && level < maxlevel) {
            exp = exp - getMaxExp(level);
            level++;
        }
        if (init != level) {
            Main.msg(p, "");
            Main.msg(p, "&7» &e&lLEVEL UP &8(&e" + name + "&8)&e: &6" + (init) + " &f-> &6" + level);
            Main.msg(p, "");
            p.sendTitle(new Title(Main.color("&e&lLEVEL UP &8(&e" + name + "&8)&e!"), Main.color("&6" + (init) + " &f-> &6" + level), 5, 40, 5));
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
        }

    }

    public String getPercent() {
        DecimalFormat df = new DecimalFormat("#.##");
        if (((1.0D * exp) / (1.0D * getMaxExp(level)) > 1)) {
            return "100%";
        }
        return df.format((100.0D * exp) / (1.0D * getMaxExp(level))) + "%";
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        if (name.equals("Mining")) {
            Material m = Material.WOODEN_PICKAXE;
            if (level >= 10) {
                m = Material.STONE_PICKAXE;
            }
            if (level >= 20) {
                m = Material.GOLDEN_PICKAXE;
            }
            if (level >= 30) {
                m = Material.IRON_PICKAXE;
            }
            if (level >= 40) {
                m = Material.DIAMOND_PICKAXE;
            }
            return m;
        }
        if (name.equals("Enchanting")) {
            return Material.ENCHANTING_TABLE;
        }
        if (name.equals("Farming")) {
            Material m = Material.WOODEN_HOE;
            if (level >= 10) {
                m = Material.STONE_HOE;
            }
            if (level >= 20) {
                m = Material.GOLDEN_HOE;
            }
            if (level >= 30) {
                m = Material.IRON_HOE;
            }
            if (level >= 40) {
                m = Material.DIAMOND_HOE;
            }
            return m;
        }
        if (name.equals("Fishing")) {
            return Material.FISHING_ROD;
        }
        if (name.equals("Lumber")) {
            Material m = Material.WOODEN_AXE;
            if (level >= 10) {
                m = Material.STONE_AXE;
            }
            if (level >= 20) {
                m = Material.GOLDEN_AXE;
            }
            if (level >= 30) {
                m = Material.IRON_AXE;
            }
            if (level >= 40) {
                m = Material.DIAMOND_AXE;
            }
            return m;
        }
        return null;
    }

}
