package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.party.Party;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class WorldOnFire extends Skill implements Listener {

    private Main main = Main.getInstance();

    private int range = 8;
    private int hp = 5;
    private int mana = 5;
    private int ramp = 1;
    private int initRamp = 10;
    private Map<Player, Integer> amp;

    public void toggleEnd(Player p) {
        super.toggleEnd(p);
        main.getPC().get(p.getUniqueId()).setPStrength((main.getPC().get(p.getUniqueId()).getPStrength() - initRamp - amp.get(p)));
        amp.remove(p);
    }

    public boolean toggleCont(Player p) {
        DecimalFormat df = new DecimalFormat("#.##");
        if (!super.toggleCont(p)) {
            return false;
        }
        if (p.getFireTicks() > 0) {
            amp.replace(p, amp.get(p) + ramp);
            main.getPC().get(p.getUniqueId()).setPStrength(main.getPC().get(p.getUniqueId()).getPStrength() + ramp);
        }
        for (LivingEntity ent : p.getLocation().getNearbyLivingEntities(range)) {
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
                if (p.equals(pl)) {
                    continue;
                }
            }
            if (ent.getFireTicks() > 0) {
                amp.replace(p, amp.get(p) + ramp);
                main.getPC().get(p.getUniqueId()).setPStrength(main.getPC().get(p.getUniqueId()).getPStrength() + ramp);
            }
        }
        for (double alpha = 0; alpha < Math.PI; alpha+= Math.PI/64) {
            Location loc = p.getLocation();
            Location firstLocation = loc.clone().add( range * Math.cos( alpha ), 0.1, range * Math.sin( alpha ) );
            Location secondLocation = loc.clone().add( range * Math.cos( alpha + Math.PI ), 0.1, range * Math.sin( alpha + Math.PI ) );
            //Location firstLocation = loc.clone().add( Math.cos( alpha ), Math.sin( alpha ) + 1, Math.sin( alpha ) );
            //Location secondLocation = loc.clone().add( Math.cos( alpha + Math.PI ), Math.sin( alpha ) + 1, Math.sin( alpha + Math.PI ) );
            p.getWorld().spawnParticle( Particle.FLAME, firstLocation, 1, 0, 0.01, 0.01, 0.01 );
            p.getWorld().spawnParticle( Particle.FLAME, secondLocation, 1, 0, 0.01, 0.01, 0.01 );
        }
        return false;
    }

    public int toggleInit(Player p) {
        DecimalFormat df = new DecimalFormat("#.##");
        amp.put(p, 0);
        p.setFireTicks(100);
        main.getPC().get(p.getUniqueId()).setPStrength(main.getPC().get(p.getUniqueId()).getPStrength() + initRamp);
        return super.toggleInit(p);
    }

    public WorldOnFire() {
        super("WorldOnFire", 50, 3 * 20, 0, 0, "%player% has conjured a burst of Flame!", "TOGGLE-PASSIVE-CAST");
        setDescription("&bPassive\nCasting WorldOnFire ignites you for 5 seconds.\nPlayers within " + range + " blocks are ignited when you are on fire.\nFor each nearby player on fire, regain" + hp + " health and " + mana + " mana every half a second.\n&bActive\nSurround yourself in a fiery aura, granting you " + initRamp * 100.0 + "% power strength.\nWhile you are on fire and nearby enemies are on fire, gain " + ramp * 100.0 + "% power strength every half a second.");
        setPassiveTicks(1);
        setToggleTicks(10);
        setToggleMana(10);
        amp = new HashMap<>();
    }

    public void passive(Player p) {
        super.passive(p);
        if (p.getFireTicks() > 0) {
            passiveIgnite(p);
        }
        if (p.getWorld().getTime() % 10 == 0) {
            sapEntities(p);
        }
    }

    public void passiveIgnite(Player caster) {
        for (LivingEntity ent : caster.getLocation().getNearbyLivingEntities(range)) {
            if (ent instanceof ArmorStand) {
                continue;
            }
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
            if (ent.getFireTicks() == 0) {
                drawLine(caster, ent);
            }
            ent.setFireTicks(100);
        }
    }

    // Event listen fire damage, ignite nearby
    // On server startup loop through RPGPlayer passives list. Get skill from name in list and run passiveCast.
    // Check if player still has that passive, changing classes CLEAR PASSIVE.

    public void cast(Player p) {
        super.cast(p);
        /*Location loc = p.getLocation();
        Vector vec = p.getLocation().getDirection();
        for (int i = 0; i < 200; i++) {
            loc.setX(loc.getX() + i);
            p.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
        }*/

        /*p.getLineOfSight(null, range).forEach(block -> block.getWorld().playEffect(block.getLocation(), Effect.MOBSPAWNER_FLAMES, 1));
        p.getLineOfSight(null, range).forEach(block -> damageEntities(block, p));*/
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 1.0F, 1.0F);
        p.setFireTicks(5 * 20);
    }

    public void igniteEntities(Player caster) {
        for (LivingEntity ent : caster.getLocation().getNearbyLivingEntities(range)) {
            if (ent instanceof ArmorStand) {
                continue;
            }
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
            if (ent.getFireTicks() == 0) {
                drawLine(caster, ent);
            }
            ent.setFireTicks(100);
        }
    }

    public void sapEntities(Player caster) {
        for (LivingEntity ent : caster.getLocation().getNearbyLivingEntities(range)) {
            if (ent instanceof ArmorStand) {
                continue;
            }
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
            if (ent.getFireTicks() > 0) {
                if (!caster.isDead() && !ent.isDead()) {
                    ent.getWorld().spawnParticle(Particle.FLAME, ent.getLocation(), 15, 0.04, 0.04, 0.04, 0.04);
                    ent.getWorld().playSound(ent.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.5F, 1.0F);

                    if (caster.getHealth() + hp <= caster.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()) {
                        caster.setHealth(caster.getHealth() + hp);
                    } else {
                        caster.setHealth(caster.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                    }
                    drawLine(caster, ent);
                    //caster.getLocation().toVector().subtract(ent.getLocation().toVector()).forEach(block -> igniteBlocks(block, caster));
                    main.getPC().get(caster.getUniqueId()).setMana(main.getPC().get(caster.getUniqueId()).getCMana() + mana);
                }
            }
        }
    }

    public void drawLine(Player caster, LivingEntity ent) {
        Location point1 = new Location(ent.getWorld(), ent.getEyeLocation().getX(), (ent.getEyeLocation().getY() - 0.7), ent.getEyeLocation().getZ());
        Location point2 = new Location(caster.getWorld(), caster.getEyeLocation().getX(), (caster.getEyeLocation().getY() - 0.7), caster.getEyeLocation().getZ());
        double distance = point1.distance(point2);
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(0.3);
        double length = 0;
        for (; length < distance; p1.add(vector)) {
            caster.getWorld().spawnParticle(Particle.FLAME, p1.getX(), p1.getY(), p1.getZ(), 1, 0.01, 0.01, 0.01, 0.01);
            length += 0.3;
        }
    }

}
