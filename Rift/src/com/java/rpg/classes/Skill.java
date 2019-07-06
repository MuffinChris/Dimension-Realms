package com.java.rpg.classes;

import org.bukkit.entity.Player;

public class Skill {

    private int manaCost;
    private double cooldown;
    private double warmup;
    private int level;

    private String name;
    private String flavor;
    private String description;

    public Skill(String name, int manaCost, double cooldown, double warmup, int level, String flavor) {
        this.name = name;
        this.manaCost = manaCost;
        this.cooldown = cooldown;
        this.warmup = warmup;
        this.level = level;
        this.flavor = flavor;
    }

    public void setDescription(String desc) {
        description = desc;
    }

    public String getDescription() {
        return description;
    }

    public String getFlavor() {
        return flavor;
    }

    public int getManaCost() {
        return manaCost;
    }

    public double getCooldown() {
        return cooldown;
    }

    public double getWarmup() {
        return warmup;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public void cast(Player p) {

    }

}
