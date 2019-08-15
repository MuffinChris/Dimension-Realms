package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.party.Party;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class FlameTornado extends Skill {

    private Main main = Main.getInstance();

    private double dps = 50;
    private double dpt = 3;
    private double travelspeed = 1;
    private double height = 8;
    private int duration = 20 * 30;
    private int maxradius = 4; // actually diameter lol
    private Map<UUID, Location> tornados;
    private Map<UUID, Map<UUID, Integer>> damage;

    public FlameTornado() {
        super("FlameTornado", 100, 60 * 20, 30, 0, "%player% has shot a fireball!", "CAST");
        setDescription("Materialize a twister of pure fire, which slowly expands to reach a radius of " + maxradius + " blocks in size.\nThe tornado deals " + dps + " damage per second, and follows the nearest enemy at a speed of " + travelspeed + " blocks per second.");
        tornados = new HashMap<>();
        damage = new HashMap<>();
    }

    public void cast(Player p) {
        super.cast(p);
        if (damage.containsKey(p.getUniqueId())) {
            damage.remove(p.getUniqueId());
        }
        damage.put(p.getUniqueId(), new HashMap<>());

        if (tornados.containsKey(p.getUniqueId())) {
            tornados.remove(p.getUniqueId());
        }
        tornados.put(p.getUniqueId(), p.getLocation());
        if (!getTasks().isEmpty()) {
            for (int i : getTasks()) {
                Bukkit.getScheduler().cancelTask(i);
            }
            getTasks().clear();
        }
        new BukkitRunnable() {
            int times = 0;
            public void run() {
                Entity nadoEnt = null;
                EntityType e = EntityType.ZOMBIE;
                double dist = 999;
                Location locOf = tornados.get(p.getUniqueId());
                for (LivingEntity ent : locOf.getNearbyLivingEntities(32)) {
                    if (ent instanceof ArmorStand) {
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
                    if ((dist > ent.getLocation().distance(locOf) && !(!(ent instanceof Player) && e == EntityType.PLAYER)) || (ent instanceof Player && !(e == EntityType.PLAYER))) {
                        dist = ent.getLocation().distance(locOf);
                        e = ent.getType();
                        nadoEnt = ent;
                    }
                }
                if (dist < Double.MAX_VALUE && nadoEnt instanceof Entity) {
                    if (!(dist <= 1)) {
                        locOf = locOf.toVector().clone().add(nadoEnt.getLocation().clone().subtract(new Vector(0, 0.5, 0)).toVector().subtract(locOf.clone().toVector()).normalize().multiply((travelspeed * 1.0) / (locOf.distance(nadoEnt.getLocation())))).toLocation(locOf.getWorld());
                    }
                }
                makeTornado(locOf.clone(), p);
                tornados.remove(p.getUniqueId());
                tornados.put(p.getUniqueId(), locOf);
                times++;
                if (times >= duration) {
                    cancel();
                    if (tornados.containsKey(p.getUniqueId())) {
                        tornados.remove(p.getUniqueId());
                    }
                    if (damage.containsKey(p.getUniqueId())) {
                        damage.remove(p.getUniqueId());
                    }
                }
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);
    }

    public void makeTornado(Location loc, Player caster) {
        double radius = 0.1;
        double height2 = 0.1;
        List<HashMap<Location, Double>> locs = new ArrayList<>();
        boolean maxr = false;
        boolean maxh = false;
        double random = Math.random();
        if (random <= 0.1) {
            loc.getWorld().playSound(loc, Sound.ENTITY_BLAZE_AMBIENT, 1.0F, 1.0F);
        }
        while(!(maxh &&maxr)) {
            if (radius < maxradius) {
                radius += 0.2;
            } else {
                maxr = true;
            }
            if (height2 < height) {
                height2 += 0.4;
            } else {
                maxh = true;
            }
            for (double alpha = 0; alpha < Math.PI; alpha+= Math.PI/16) {
                Location firstLocation = loc.clone().add( radius * Math.cos( alpha ), height2, radius * Math.sin( alpha ) );
                Location secondLocation = loc.clone().add( radius * Math.cos( alpha + Math.PI ), height2, radius * Math.sin( alpha + Math.PI ) );
                loc.getWorld().spawnParticle( Particle.FLAME, firstLocation, 1, 0, 0.01, 0.01, 0.01 );
                loc.getWorld().spawnParticle( Particle.FLAME, secondLocation, 1, 0, 0.01, 0.01, 0.01 );
            }
            HashMap map = new HashMap<>();
            Location addToLocs = loc.clone();
            addToLocs.add(new Vector(0, height2, 0));
            map.put(addToLocs, radius + 0.8);
            locs.add((HashMap) map.clone());
            for (LivingEntity e : loc.getNearbyLivingEntities(radius + 0.8)) {
                if (e instanceof ArmorStand) {
                    continue;
                }
                if (e instanceof Player) {
                    Player pl = (Player) e;
                    if (main.getPM().getParty(pl) instanceof Party && !main.getPM().getParty(pl).getPvp()) {
                        if (main.getPM().getParty(pl).getPlayers().contains(caster)) {
                            continue;
                        }
                    }
                    if (pl.equals(caster)) {
                        continue;
                    }
                }
                for (HashMap<Location, Double> locd : locs) {
                    if (e.getEyeLocation().distance((Location) locd.keySet().toArray()[0]) <= (Double) locd.values().toArray()[0]) {
                        spellDamage(caster, e, dpt);
                        loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_ON_FIRE, 0.25F, 1.0F);
                        break;
                    }
                }
            }
        }
    }

    public void warmup(Player p) {
        super.warmup(p);
        makeTornado(p.getLocation(), p);
    }

}