package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.party.Party;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftFireball;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftLargeFireball;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public class MeteorShower extends Skill implements Listener {

    private Main main = Main.getInstance();

    private double damage = 50;
    private int duration = 10;
    private int range = 8;

    public MeteorShower() {
        super("MeteorShower", 150, 30 * 20, 40, 0, "%player% has created a Meteor Shower!", "CAST");
        setDescription("Rain hell on opponents within " + range + " blocks,\nrandomly creating meteors dealing " + damage + " damage for " + duration + " seconds.\nMeteors target entities on fire first!");
    }

    public void warmup(Player p) {
        super.warmup(p);
        p.getWorld().spawnParticle(Particle.FLAME, p.getLocation(), 5, 0.1, 0.1, 0.1);
        p.getWorld().spawnParticle(Particle.LAVA, p.getLocation(), 5, 0.3, 0.3, 0.3);
    }

    public void cast(Player p) {
        super.cast(p);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0F, 1.0F);
        new BukkitRunnable() {
            int count = 0;
            public void run() {
                Location l = p.getLocation();
                int negative = 1;
                int negative2 = 1;
                if (Math.random() < 0.45) {
                    negative = -1;
                }
                if (Math.random() < 0.45) {
                    negative2 = -1;
                }
                Location loc = new Location(l.getWorld(), l.getX() + Math.random() * range * negative, l.getY(), l.getZ() + Math.random() * range * negative2);

                spawnFireball(p, loc);
                count++;
                if (count * 8 >= duration * 20) {
                    cancel();
                }
            }
        }.runTaskTimer(main, 0L, 8L);
    }

    public void spawnFireball(Player p, Location l) {
        int negative = 1;
        int negative2 = 1;
        if (Math.random() < 0.4) {
            negative = -1;
        }
        if (Math.random() < 0.4) {
            negative2 = -1;
        }
        Location loc = new Location(l.getWorld(), l.getX() + Math.random() * 10 * negative, l.getY() + 20, l.getZ() + Math.random() * 10 * negative2);
        CraftLargeFireball fireball = (CraftLargeFireball) l.getWorld().spawn(loc, CraftLargeFireball.class);
        //fireball.setDirection(e.getLocation().toVector());
        fireball.setShooter(p);
        fireball.setCustomName("Meteor:" + damage);
        fireball.setBounce(false);
        fireball.setGravity(true);
        //fireball.setVelocity(new Vector(0, -1.0, 0));
        double dist = 100;
        Location loca = l;
        for (LivingEntity e : l.getNearbyLivingEntities(16)) {
            if (e.getFireTicks() > 0) {
                if (e instanceof Player && ((Player) e) == p) {
                    continue;
                }
                if (e.getLocation().distance(fireball.getLocation()) < dist) {
                    dist = e.getLocation().distance(fireball.getLocation());
                    loca = e.getLocation();
                }
            }
        }
        fireball.setDirection(loca.toVector().subtract(fireball.getLocation().toVector()).normalize());
        fireball.setVelocity(new Vector(0, 0, 0));
        fireball.setInvulnerable(true);
        fireball.setIsIncendiary(false);

        final BukkitScheduler scheduler = Bukkit.getScheduler();
        final int task = scheduler.scheduleSyncRepeatingTask(main, new Runnable(){
            @Override
            public void run() {
                if (!fireball.isDead()) {
                    p.getWorld().spawnParticle(Particle.LAVA, fireball.getLocation(), 5, 0.5, 0.5, 0.5, 0.5);
                } else {
                    damageEntities(fireball.getLocation(), p, Double.valueOf(fireball.getCustomName().replace("Meteor:", "")));
                }
            }
        }, 1, 1);
        scheduler.scheduleSyncDelayedTask(main, new Runnable(){
            @Override
            public void run(){
                damageEntities(fireball.getLocation(), p, Double.valueOf(fireball.getCustomName().replace("Meteor:", "")));
                scheduler.cancelTask(task);
                fireball.remove();
            }
        }, 100);
    }

    @EventHandler
    public void explode (EntityExplodeEvent e) {
        if (e.getEntity() instanceof CraftLargeFireball) {
            CraftLargeFireball fireball = (CraftLargeFireball) e.getEntity();
            if (fireball.getCustomName() instanceof String && fireball.getCustomName().contains("Meteor:") && fireball.getShooter() instanceof Player) {
                fireball.getWorld().playSound(fireball.getLocation().subtract(new Vector(0, 1, 0)), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0F, 1.0F);
                fireball.getWorld().spawnParticle(Particle.LAVA, fireball.getLocation().subtract(new Vector(0, 1, 0)), 10, 0.75, 0.75, 0.75);
                fireball.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, fireball.getLocation().subtract(new Vector(0, 1, 0)), 3, 0.5, 0.5, 0.5);
                Player caster = (Player) fireball.getShooter();
                damageEntities(fireball.getLocation().subtract(new Vector(0, 1, 0)), caster, Double.valueOf(fireball.getCustomName().replace("Meteor:", "")));
                fireball.remove();
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHit (ProjectileHitEvent e) {
        if (e.getEntity() instanceof CraftLargeFireball) {
            CraftLargeFireball fireball = (CraftLargeFireball) e.getEntity();
            if (fireball.getCustomName() instanceof String && fireball.getCustomName().contains("Meteor:") && fireball.getShooter() instanceof Player) {
                fireball.getWorld().playSound(fireball.getLocation().subtract(new Vector(0, 1, 0)), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0F, 1.0F);
                fireball.getWorld().spawnParticle(Particle.LAVA, fireball.getLocation().subtract(new Vector(0, 1, 0)), 10, 0.75, 0.75, 0.75);
                fireball.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, fireball.getLocation().subtract(new Vector(0, 1, 0)), 3, 0.5, 0.5, 0.5);
                Player caster = (Player) fireball.getShooter();
                damageEntities(fireball.getLocation().subtract(new Vector(0, 1, 0)), caster, Double.valueOf(fireball.getCustomName().replace("Meteor:", "")));
                fireball.remove();
            }
        }
    }

    @EventHandler
    public void onDamage (EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof CraftLargeFireball) {
            CraftLargeFireball fireball = (CraftLargeFireball) e.getDamager();
            if (fireball.getCustomName() instanceof String && fireball.getCustomName().contains("Meteor:") && fireball.getShooter() instanceof Player) {
                fireball.getWorld().playSound(fireball.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0F, 1.0F);
                fireball.getWorld().spawnParticle(Particle.LAVA, fireball.getLocation(), 10, 0.75, 0.75, 0.75);
                fireball.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, fireball.getLocation(), 3, 0.1, 0.1, 0.1);
                Player caster = (Player) fireball.getShooter();
                damageEntities(fireball.getLocation(), caster, Double.valueOf(fireball.getCustomName().replace("Meteor:", "")));
                fireball.remove();
                e.setDamage(0);
                e.setCancelled(true);
            }
        }
    }

    public void damageEntities(Location l, Player caster, double dmg) {
        for (LivingEntity ent : l.getNearbyLivingEntities(2.4)) {
            if (ent instanceof Player) {
                Player p = (Player) ent;
                if (main.getPM().getParty(p) instanceof Party && !main.getPM().getParty(p).getPvp()) {
                    if (main.getPM().getParty(p).getPlayers().contains(caster)) {
                        continue;
                    }
                }
                if (p.equals(caster)) {
                    continue;
                }
            }
            ent.setKiller(caster);
            spellDamage(caster, ent, dmg);
            ent.setFireTicks(100);
        }
    }
}
