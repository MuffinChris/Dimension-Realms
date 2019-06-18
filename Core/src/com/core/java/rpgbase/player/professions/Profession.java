package com.core.java.rpgbase.player.professions;

import org.bukkit.Material;

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

    public Profession(String n, int ml, int lvl, int xp, String desc) {
        name = n;
        maxlevel = ml;
        level = lvl;
        exp = xp;
        this.desc = desc;
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

    public void levelup() {
        if (exp >= getMaxExp(level) && level < maxlevel) {
            level++;
            exp = 0;
        }
    }

    public String getPercent() {
        DecimalFormat df = new DecimalFormat("#.##");
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
