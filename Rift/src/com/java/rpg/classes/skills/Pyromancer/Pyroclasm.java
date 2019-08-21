package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.party.Party;
import de.slikey.effectlib.Effect;
import de.slikey.effectlib.effect.ArcEffect;
import de.slikey.effectlib.effect.VortexEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.*;

public class Pyroclasm extends Skill {

    private Main main = Main.getInstance();

    private double damage = 100;
    private double empowered = 1.25;
    private int range = 14;
    private int duration = 20 * 15;
    private int maxbounces = 50;
    private double ratio = 0.95;
    private Map<UUID, LivingEntity> target;
    private Map<UUID, LivingEntity> lastTarget;
    private Map<UUID, Location> clasm;
    private Map<UUID, LivingEntity> target2;
    private Map<UUID, LivingEntity> lastTarget2;
    private Map<UUID, Location> clasm2;
    private Map<UUID, LivingEntity> target3;
    private Map<UUID, LivingEntity> lastTarget3;
    private Map<UUID, Location> clasm3;
    double travelspeed = 0.5;

    public Pyroclasm() {
        super("Pyroclasm", 150, 90 * 20, 50, 0, "%player% has shot a fireball!", "CAST-TARGET");
        DecimalFormat df = new DecimalFormat("#");
        setDescription("Launch 3 flaming projectiles at the nearest target, upon hitting they deal " + damage + " damage.\nIf the target is on fire, it deals " + empowered + "x more damage and increases the duration by 1 second.\nThe projectile bounces to the nearest enemy in " + range + " blocks and prioritizes new targets. Each bounce deals " + df.format(ratio * 100.0) + "% damage than the previous.");
        setTargetRange(range);
        target = new HashMap<>();
        clasm = new HashMap<>();
        lastTarget = new HashMap<>();
        target2 = new HashMap<>();
        clasm2 = new HashMap<>();
        lastTarget2 = new HashMap<>();
        target3 = new HashMap<>();
        clasm3 = new HashMap<>();
        lastTarget3 = new HashMap<>();
    }

    public void makeProjectile(Location loc, Player caster) {
        double radius = 0;
        double height2 = 0;
        while (radius <= 0.6) {
            for (double alpha = 0; alpha < Math.PI; alpha += Math.PI / 4) {
                Location firstLocation = loc.clone().add(radius * Math.cos(alpha), height2, radius * Math.sin(alpha));
                Location secondLocation = loc.clone().add(radius * Math.cos(alpha + Math.PI), height2, radius * Math.sin(alpha + Math.PI));
                loc.getWorld().spawnParticle(Particle.FLAME, firstLocation,  0, 0.001, 0.001, 0.001);
                loc.getWorld().spawnParticle(Particle.FLAME, secondLocation,  0, 0.001, 0.001, 0.001);
            }
            radius+=0.2;
            height2+=0.1;
        }

        radius = 0.6;
        height2 = 0.3;
        while (radius > 0) {
            for (double alpha = 0; alpha < Math.PI; alpha += Math.PI / 4) {
                Location firstLocation = loc.clone().add(radius * Math.cos(alpha), height2, radius * Math.sin(alpha));
                Location secondLocation = loc.clone().add(radius * Math.cos(alpha + Math.PI), height2, radius * Math.sin(alpha + Math.PI));
                loc.getWorld().spawnParticle(Particle.FLAME, firstLocation, 0, 0.001, 0.001, 0.001);
                loc.getWorld().spawnParticle(Particle.FLAME, secondLocation,  0, 0.001, 0.001, 0.001);
            }
            radius-=0.2;
            height2+=0.1;
        }

        loc.getWorld().spawnParticle(Particle.LAVA, loc, 1, 0.0, 0.001, 0.001, 0.001);
    }

    public void target(Player p, LivingEntity t) {
        super.target(p, t);
        if (clasm.containsKey(p.getUniqueId())) {
            clasm.remove(p.getUniqueId());
        }
        if (target.containsKey(p.getUniqueId())) {
            target.remove(p.getUniqueId());
        }
        target.put(p.getUniqueId(), t);
        clasm.put(p.getUniqueId(), p.getEyeLocation());
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
        new BukkitRunnable() {
            int times = 0;
            int bounces = 0;
            public void run() {
                LivingEntity nadoEnt = null;
                EntityType e = EntityType.ZOMBIE;
                double dist = 999;
                Location locOf = clasm.get(p.getUniqueId());
                if (target.containsKey(p.getUniqueId())) {
                    if (target.get(p.getUniqueId()).isDead()) {
                        target.remove(p.getUniqueId());
                    }
                }
                if (lastTarget.containsKey(p.getUniqueId())) {
                    if (lastTarget.get(p.getUniqueId()).isDead()) {
                        lastTarget.remove(p.getUniqueId());
                    }
                }
                if (!target.containsKey(p.getUniqueId())) {
                    boolean looking = true;
                    DecimalFormat dF = new DecimalFormat("#");
                    while (looking && getNearbyEnts(locOf, p).size() > 0) {
                        LivingEntity randomEnt = (LivingEntity) getNearbyEnts(locOf, p).toArray()[Integer.valueOf(dF.format(Math.floor(Math.random() * getNearbyEnts(locOf, p).size())))];
                        if (!(lastTarget.containsKey(p.getUniqueId()) && lastTarget.get(p.getUniqueId()) == randomEnt)) {
                            dist = randomEnt.getEyeLocation().distance(locOf);
                            e = randomEnt.getType();
                            nadoEnt = randomEnt;
                            looking = false;
                        } else {
                            looking = true;
                        }
                    }
                    if (nadoEnt instanceof LivingEntity) {
                        target.put(p.getUniqueId(), nadoEnt);
                        locOf = locOf.add(target.get(p.getUniqueId()).getEyeLocation().subtract(new Vector(0, 0.5, 0)).toVector().subtract(locOf.toVector()).normalize().multiply((travelspeed * 1.0)));
                    }
                    if (!(target.get(p.getUniqueId()) instanceof LivingEntity)) {
                        times = duration + 100;
                    }
                } else {
                    locOf = locOf.add(target.get(p.getUniqueId()).getEyeLocation().subtract(new Vector(0, 0.5, 0)).toVector().subtract(locOf.toVector()).normalize().multiply((travelspeed * 1.0)));
                    if (locOf.distance(target.get(p.getUniqueId()).getEyeLocation().subtract(new Vector(0, 0.5, 0))) <= 0.5) {
                        bounces++;
                        if (target.get(p.getUniqueId()) instanceof Player) {
                            bounces+=9;
                        }
                        if (target.get(p.getUniqueId()).getFireTicks() > 0) {
                            target.get(p.getUniqueId()).setFireTicks(Math.min(40 + target.get(p.getUniqueId()).getFireTicks(), 100));
                            target.get(p.getUniqueId()).getWorld().playSound(target.get(p.getUniqueId()).getEyeLocation(), Sound.ITEM_BUCKET_FILL_LAVA, 1.0F, 1.0F);
                            spellDamage(p, target.get(p.getUniqueId()), damage * empowered * (Math.pow(ratio, bounces)));
                        } else {
                            target.get(p.getUniqueId()).getWorld().playSound(target.get(p.getUniqueId()).getEyeLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
                            target.get(p.getUniqueId()).setFireTicks(Math.min(40 + target.get(p.getUniqueId()).getFireTicks(), 100));
                            spellDamage(p, target.get(p.getUniqueId()), damage * (Math.pow(ratio, bounces)));
                        }
                        if (lastTarget.containsKey(p.getUniqueId())) {
                            lastTarget.remove(p.getUniqueId());
                        }
                        lastTarget.put(p.getUniqueId(), target.get(p.getUniqueId()));
                        target.remove(p.getUniqueId());
                        times-=20;
                        if (bounces >= maxbounces) {
                            times = duration;
                        }
                    }
                }
                makeProjectile(locOf.clone(), p);
                clasm.remove(p.getUniqueId());
                clasm.put(p.getUniqueId(), locOf);
                times++;
                if (times >= duration) {
                    cancel();
                    if (lastTarget.containsKey(p.getUniqueId())) {
                        lastTarget.remove(p.getUniqueId());
                    }
                    if (clasm.containsKey(p.getUniqueId())) {
                        clasm.remove(p.getUniqueId());
                    }
                    if (target.containsKey(p.getUniqueId())) {
                        target.remove(p.getUniqueId());
                    }
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 1);
        new BukkitRunnable() {
            public void run() {
                if (clasm2.containsKey(p.getUniqueId())) {
                    clasm2.remove(p.getUniqueId());
                }
                if (target2.containsKey(p.getUniqueId())) {
                    target2.remove(p.getUniqueId());
                }
                target2.put(p.getUniqueId(), t);
                clasm2.put(p.getUniqueId(), p.getEyeLocation());
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
            }
        }.runTaskLater(Main.getInstance(), 9);
        new BukkitRunnable() {
            int times = 0;
            int bounces = 0;
            public void run() {
                LivingEntity nadoEnt = null;
                EntityType e = EntityType.ZOMBIE;
                double dist = 999;
                Location locOf = clasm2.get(p.getUniqueId());
                if (target2.containsKey(p.getUniqueId())) {
                    if (target2.get(p.getUniqueId()).isDead()) {
                        target2.remove(p.getUniqueId());
                    }
                }
                if (lastTarget2.containsKey(p.getUniqueId())) {
                    if (lastTarget2.get(p.getUniqueId()).isDead()) {
                        lastTarget2.remove(p.getUniqueId());
                    }
                }
                if (!target2.containsKey(p.getUniqueId())) {
                    boolean looking = true;
                    DecimalFormat dF = new DecimalFormat("#");
                    while (looking && getNearbyEnts(locOf, p).size() > 0) {
                        LivingEntity randomEnt = (LivingEntity) getNearbyEnts(locOf, p).toArray()[Integer.valueOf(dF.format(Math.floor(Math.random() * getNearbyEnts(locOf, p).size())))];
                        if (!(lastTarget2.containsKey(p.getUniqueId()) && lastTarget2.get(p.getUniqueId()) == randomEnt)) {
                            dist = randomEnt.getEyeLocation().distance(locOf);
                            e = randomEnt.getType();
                            nadoEnt = randomEnt;
                            looking = false;
                        } else {
                            looking = true;
                        }
                    }
                    if (nadoEnt instanceof LivingEntity) {
                        target2.put(p.getUniqueId(), nadoEnt);
                        locOf = locOf.add(target2.get(p.getUniqueId()).getEyeLocation().subtract(new Vector(0, 0.5, 0)).toVector().subtract(locOf.toVector()).normalize().multiply((travelspeed * 1.0)));
                    }
                    if (!(target2.get(p.getUniqueId()) instanceof LivingEntity)) {
                        times = duration + 100;
                    }
                } else {
                    locOf = locOf.add(target2.get(p.getUniqueId()).getEyeLocation().subtract(new Vector(0, 0.5, 0)).toVector().subtract(locOf.toVector()).normalize().multiply((travelspeed * 1.0)));
                    if (locOf.distance(target2.get(p.getUniqueId()).getEyeLocation().subtract(new Vector(0, 0.5, 0))) <= 0.5) {
                        bounces++;
                        if (target2.get(p.getUniqueId()) instanceof Player) {
                            bounces+=9;
                        }
                        if (target2.get(p.getUniqueId()).getFireTicks() > 0) {
                            target2.get(p.getUniqueId()).setFireTicks(Math.min(40 + target2.get(p.getUniqueId()).getFireTicks(), 100));
                            target2.get(p.getUniqueId()).getWorld().playSound(target2.get(p.getUniqueId()).getEyeLocation(), Sound.ITEM_BUCKET_FILL_LAVA, 1.0F, 1.0F);
                            spellDamage(p, target2.get(p.getUniqueId()), damage * empowered * (Math.pow(ratio, bounces)));
                        } else {
                            target2.get(p.getUniqueId()).getWorld().playSound(target2.get(p.getUniqueId()).getEyeLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
                            target2.get(p.getUniqueId()).setFireTicks(Math.min(40 + target2.get(p.getUniqueId()).getFireTicks(), 100));
                            spellDamage(p, target2.get(p.getUniqueId()), damage * (Math.pow(ratio, bounces)));
                        }
                        if (lastTarget2.containsKey(p.getUniqueId())) {
                            lastTarget2.remove(p.getUniqueId());
                        }
                        lastTarget2.put(p.getUniqueId(), target2.get(p.getUniqueId()));
                        target2.remove(p.getUniqueId());
                        times -= 20;
                        if (bounces >= maxbounces) {
                            times = duration;
                        }
                    }
                }
                makeProjectile(locOf.clone(), p);
                clasm2.remove(p.getUniqueId());
                clasm2.put(p.getUniqueId(), locOf);
                times++;
                if (times >= duration) {
                    cancel();
                    if (lastTarget2.containsKey(p.getUniqueId())) {
                        lastTarget2.remove(p.getUniqueId());
                    }
                    if (clasm2.containsKey(p.getUniqueId())) {
                        clasm2.remove(p.getUniqueId());
                    }
                    if (target2.containsKey(p.getUniqueId())) {
                        target2.remove(p.getUniqueId());
                    }
                }
            }
        }.runTaskTimer(Main.getInstance(), 10, 1);
        new BukkitRunnable() {
            public void run() {
                if (clasm3.containsKey(p.getUniqueId())) {
                    clasm3.remove(p.getUniqueId());
                }
                if (target3.containsKey(p.getUniqueId())) {
                    target3.remove(p.getUniqueId());
                }
                target3.put(p.getUniqueId(), t);
                clasm3.put(p.getUniqueId(), p.getEyeLocation());
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
            }
        }.runTaskLater(Main.getInstance(), 19);
        new BukkitRunnable() {
            int times = 0;
            int bounces = 0;
            public void run() {
                LivingEntity nadoEnt = null;
                EntityType e = EntityType.ZOMBIE;
                double dist = 999;
                Location locOf = clasm3.get(p.getUniqueId());
                if (target3.containsKey(p.getUniqueId())) {
                    if (target3.get(p.getUniqueId()).isDead()) {
                        target3.remove(p.getUniqueId());
                    }
                }
                if (lastTarget3.containsKey(p.getUniqueId())) {
                    if (lastTarget3.get(p.getUniqueId()).isDead()) {
                        lastTarget3.remove(p.getUniqueId());
                    }
                }
                if (!target3.containsKey(p.getUniqueId())) {
                    boolean looking = true;
                    DecimalFormat dF = new DecimalFormat("#");
                    while (looking && getNearbyEnts(locOf, p).size() > 0) {
                        LivingEntity randomEnt = (LivingEntity) getNearbyEnts(locOf, p).toArray()[Integer.valueOf(dF.format(Math.floor(Math.random() * getNearbyEnts(locOf, p).size())))];
                        if (!(lastTarget3.containsKey(p.getUniqueId()) && lastTarget3.get(p.getUniqueId()) == randomEnt)) {
                            dist = randomEnt.getEyeLocation().distance(locOf);
                            e = randomEnt.getType();
                            nadoEnt = randomEnt;
                            looking = false;
                        } else {
                            looking = true;
                        }
                    }
                    if (nadoEnt instanceof LivingEntity) {
                        target3.put(p.getUniqueId(), nadoEnt);
                        locOf = locOf.add(target3.get(p.getUniqueId()).getEyeLocation().subtract(new Vector(0, 0.5, 0)).toVector().subtract(locOf.toVector()).normalize().multiply((travelspeed * 1.0)));
                    }
                    if (!(target3.get(p.getUniqueId()) instanceof LivingEntity)) {
                        times = duration + 100;
                    }
                } else {
                    locOf = locOf.add(target3.get(p.getUniqueId()).getEyeLocation().subtract(new Vector(0, 0.5, 0)).toVector().subtract(locOf.toVector()).normalize().multiply((travelspeed * 1.0)));
                    if (locOf.distance(target3.get(p.getUniqueId()).getEyeLocation().subtract(new Vector(0, 0.5, 0))) <= 0.5) {
                        bounces++;
                        if (target3.get(p.getUniqueId()) instanceof Player) {
                            bounces+=9;
                        }
                        if (target3.get(p.getUniqueId()).getFireTicks() > 0) {
                            target3.get(p.getUniqueId()).setFireTicks(Math.min(40 + target3.get(p.getUniqueId()).getFireTicks(), 100));
                            target3.get(p.getUniqueId()).getWorld().playSound(target3.get(p.getUniqueId()).getEyeLocation(), Sound.ITEM_BUCKET_FILL_LAVA, 1.0F, 1.0F);
                            spellDamage(p, target3.get(p.getUniqueId()), damage * empowered * (Math.pow(ratio, bounces)));
                        } else {
                            target3.get(p.getUniqueId()).getWorld().playSound(target3.get(p.getUniqueId()).getEyeLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
                            target3.get(p.getUniqueId()).setFireTicks(Math.min(40 + target3.get(p.getUniqueId()).getFireTicks(), 100));
                            spellDamage(p, target3.get(p.getUniqueId()), damage * (Math.pow(ratio, bounces)));
                        }
                        if (lastTarget3.containsKey(p.getUniqueId())) {
                            lastTarget3.remove(p.getUniqueId());
                        }
                        lastTarget3.put(p.getUniqueId(), target3.get(p.getUniqueId()));
                        target3.remove(p.getUniqueId());
                        times -= 20;
                        if (bounces >= maxbounces) {
                            times = duration;
                        }
                    }
                }
                makeProjectile(locOf.clone(), p);
                clasm3.remove(p.getUniqueId());
                clasm3.put(p.getUniqueId(), locOf);
                times++;
                if (times >= duration) {
                    cancel();
                    if (lastTarget3.containsKey(p.getUniqueId())) {
                        lastTarget3.remove(p.getUniqueId());
                    }
                    if (clasm3.containsKey(p.getUniqueId())) {
                        clasm3.remove(p.getUniqueId());
                    }
                    if (target3.containsKey(p.getUniqueId())) {
                        target3.remove(p.getUniqueId());
                    }
                }
            }
        }.runTaskTimer(Main.getInstance(), 20, 1);
    }

    public void targetParticles(Player p, LivingEntity t) {
        t.getWorld().spawnParticle(Particle.LAVA, t.getLocation(), 10, 0, 0.01, 0.01, 0.01);
    }

    public void warmup(Player p) {
        super.warmup(p);
        Effect arc = new VortexEffect(main.getEm());
        arc.iterations = 20;
        arc.setTargetPlayer(p);
        arc.start();
        p.getWorld().spawnParticle(Particle.FLAME, p.getLocation(), 10, 0, 0.04, 0.04, 0.04);
    }

    public List<LivingEntity> getNearbyEnts(Location loc, Player p) {
        List<LivingEntity> list = new ArrayList<>();
        Collection<Entity> nearbyEnts = loc.getNearbyEntities(range, range, range);
        for (Entity ent : nearbyEnts) {
            if (ent instanceof ArmorStand || !(ent instanceof LivingEntity)) {
                continue;
            }
            if (ent instanceof Player) {
                Player pl = (Player) ent;
                if (main.getPM().getParty(pl) instanceof Party && !main.getPM().getParty(pl).getPvp()) {
                        if (main.getPM().getParty(pl).getPlayers().contains(p)) {
                            continue;
                        }
                    }
                if (pl.equals(p)) {
                    continue;
                }
            }
            list.add((LivingEntity) ent);
        }
        return list;
    }

}
