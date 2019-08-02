package com.java.rpg.classes;

import com.java.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Skill {

    private int manaCost;
    private double cooldown;
    private int warmup;
    private int level;

    private int passiveTicks;
    private int toggleTicks;
    private int toggleMana;

    private String name;
    private String flavor;
    private String description;

    private String type;

    public Skill(String name, int manaCost, double cooldown, int warmup, int level, String flavor, String type) {
        this.name = name;
        this.manaCost = manaCost;
        this.cooldown = cooldown;
        this.warmup = warmup;
        this.level = level;
        this.flavor = flavor;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setDescription(String desc) {
        description = desc;
    }

    public int getPassiveTicks() {
        return passiveTicks;
    }

    public void setPassiveTicks(int p) {
        passiveTicks = p;
    }

    public void setToggleTicks(int p) {
        toggleTicks = p;
    }

    public int getToggleTicks() {
        return toggleTicks;
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

    public int getWarmup() {
        return warmup;
    }

    public int getLevel() {
        return level;
    }

    public int getToggleMana() {
        return toggleMana;
    }

    public void setToggleMana(int m) {
        toggleMana = m;
    }

    public String getName() {
        return name;
    }

    public void cast(Player p) {
        Main.getInstance().getPC().get(p.getUniqueId()).getBoard().updateCast(this);
    }

    public int toggleInit(Player p) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            public void run() {
                toggleCont(p);
            }
        }, 0, toggleTicks);
    }

    public void toggleEnd(Player p) {
        Main.getInstance().getPC().get(p.getUniqueId()).getBoard().endToggle(this);
        Main.getInstance().getCM().cleanToggle(p, this);
    }

    public void toggleCont(Player p) {
        if (Main.getInstance().getPC().get(p.getUniqueId()).getCMana() - getToggleMana() <= 0) {
            toggleEnd(p);
        }
        Main.getInstance().getPC().get(p.getUniqueId()).setMana(Main.getInstance().getPC().get(p.getUniqueId()).getCMana() - getToggleMana());
        Main.getInstance().getPC().get(p.getUniqueId()).getBoard().updateToggle(this);
    }

    public void passive(Player p) {

    }

    public void warmup(Player p) {
        Main.getInstance().getPC().get(p.getUniqueId()).getBoard().updateWarmup(this);
    }

}
