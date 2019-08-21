package com.java.rpg.classes;

import com.java.Main;
import com.java.rpg.Damage;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Skill {

    private Main main = Main.getInstance();

    // Description should be list.

    public void spellDamage(Player caster, LivingEntity target, double damage) {
        if (target.isDead()) {
            return;
        }
        //target.setKiller(caster);
        //caster.damage(0.25, target);
        //target.damage(Math.max(damage - 0.25, 0.0));
        main.getDmg().add(new Damage(caster, target, Damage.DamageType.SPELL_MAGIC, damage));
        target.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1.0);
        target.setNoDamageTicks(0);
        target.damage(damage, caster);
        target.setNoDamageTicks(5);
        target.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.0);

    }

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

    private int targetRange;

    public int getTargetRange() {
        return targetRange;
    }

    public void setTargetRange(int t) {
        targetRange = t;
    }

    private List<Integer> tasks;

    public Skill(String name, int manaCost, double cooldown, int warmup, int level, String flavor, String type) {
        this.name = name;
        this.manaCost = manaCost;
        this.cooldown = cooldown;
        this.warmup = warmup;
        this.level = level;
        this.flavor = flavor;
        this.type = type;
        tasks = new ArrayList<>();
    }

    public List<Integer> getTasks() {
        return tasks;
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

    public void targetParticles(Player p, LivingEntity t) {

    }

    public void cast(Player p) {
        Main.getInstance().getPC().get(p.getUniqueId()).getBoard().updateCast(this);
    }

    public void target(Player p, LivingEntity t) {
        Main.getInstance().getPC().get(p.getUniqueId()).getBoard().updateCast(this);
    }

    public int toggleInit(Player p) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            public void run() {
                toggleCont(p);
            }
        }, 1, toggleTicks);
    }

    public void toggleEnd(Player p) {
        Main.getInstance().getPC().get(p.getUniqueId()).getBoard().endToggle(this);
        Main.getInstance().getCM().cleanToggle(p, this);
    }

    public boolean toggleCont(Player p) {
        if (Main.getInstance().getPC().get(p.getUniqueId()).getCMana() - getToggleMana() <= 0) {
            toggleEnd(p);
            return false;
        }
        Main.getInstance().getPC().get(p.getUniqueId()).setMana(Main.getInstance().getPC().get(p.getUniqueId()).getCMana() - getToggleMana());
        Main.getInstance().getPC().get(p.getUniqueId()).getBoard().updateToggle(this);
        return true;
    }

    public void passive(Player p) {

    }

    public void warmup(Player p) {
        Main.getInstance().getPC().get(p.getUniqueId()).getBoard().updateWarmup(this, getWarmup());
    }

}
