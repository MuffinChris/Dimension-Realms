package com.java.rpg;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Damage {

    private LivingEntity target;
    private Double damage;
    private DamageType dt;
    private Player caster;
    public enum DamageType
    {
        ATTACK, SPELL_MAGIC, SPELL_PHYSICAL, TRUE;
    }

    public Damage(Player caster, LivingEntity p, DamageType dt, Double damage) {
        target = p;
        this.dt = dt;
        this.damage = damage;
        this.caster = caster;
    }

    public DamageType getDamageType() {
        return dt;
    }

    public double getDamage() {
        return damage;
    }

    public Player getCaster() {
        return caster;
    }

    public LivingEntity getPlayer() {
        return target;
    }

    public void scrub() {
        target = null;
        dt = null;
        caster = null;
        damage = null;
    }

}
