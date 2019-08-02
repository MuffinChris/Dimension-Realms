package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.party.Party;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class InfernoVault extends Skill implements Listener {

    private Main main = Main.getInstance();

    private double vaultdamage = 50;
    private double landdamage = 150;
    private int range = 3;

    public InfernoVault() {
        super("InfernoVault", 100, 15 * 20, 0, 0, "%player% has shot a fireball!", "CAST");
        setDescription("Combust the location at your feet dealing" +  vaultdamage + " damage, launching yourself in the air.\nLanding creates another explosion, dealing " + landdamage + " damage.");
    }

    public void cast(Player p) {
        super.cast(p);
        /*Location loc = p.getLocation();
        Vector vec = p.getLocation().getDirection();
        for (int i = 0; i < 200; i++) {
            loc.setX(loc.getX() + i);
            p.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
        }*/

    }

    @EventHandler
    public void onHit (EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Arrow) {
            Arrow a = (Arrow) e.getDamager();
            if (a.getCustomName() instanceof String && a.getCustomName().contains("Fireball:") && a.getShooter() instanceof Player) {
                Player shooter = (Player) a.getShooter();
                if (e.getEntity() instanceof Player) {
                    Player p = (Player) e.getEntity();
                    if (main.getPM().getParty(p) instanceof Party && !main.getPM().getParty(p).getPvp()) {
                        if (main.getPM().getParty(p).getPlayers().contains(a.getShooter())) {
                            e.setDamage(0);
                            a.remove();
                            e.setCancelled(true);
                            return;
                        }
                    }
                    if (p.equals(shooter)) {
                        a.remove();
                        e.setDamage(0);
                        e.setCancelled(true);
                        return;
                    }
                    //((CraftPlayer)p).getHandle().getDataWatcher().set(new DataWatcherObject<>(10, DataWatcherRegistry.b),0);
                }
                if (e.getEntity() instanceof LivingEntity) {
                    LivingEntity ent = (LivingEntity) e.getEntity();
                    ent.setKiller(shooter);
                    lightEntities(e.getEntity(), shooter);
                    ent.damage(Double.valueOf(a.getCustomName().replace("Fireball:", "")));
                    ent.getWorld().spawnParticle(Particle.LAVA, ent.getLocation(), 50, 0.04, 0.04, 0.04, 0.04);
                }
                e.setDamage(0);
                a.remove();
                e.setCancelled(true);
            }
        }
    }

    public void lightEntities(Entity e, Player caster) {
        for (LivingEntity ent : e.getLocation().getNearbyLivingEntities(1)) {
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
                    return;
                }
            }
            ent.setFireTicks(60);
        }
        e.setFireTicks(60);
    }

    /*public boolean damageEntities(Location loc, Player caster, Entity e) {
        Main.so("de");
        for (LivingEntity ent : loc.getNearbyLivingEntities(0.5)) {
            Main.so("pp");
            if (ent instanceof Player) {
                Player p = (Player) ent;
                if (main.getPM().getParty(p) instanceof Party) {
                    if (main.getPM().getParty(p).getPlayers().contains(caster)) {
                        continue;
                    }
                }
                if (p.equals(caster)) {
                    return false;
                }
            }
            ent.setKiller(caster);
            ent.damage(Double.valueOf(e.getCustomName().replace("Fireball:", "")));
            Main.so("" + Double.valueOf(e.getCustomName().replace("Fireball:", "")));
            ent.setFireTicks(60);
            e.remove();
            ent.getWorld().spawnParticle(Particle.LAVA, e.getLocation(), 50, 0.04, 0.04, 0.04, 0.04);
            return true;
        }
        return false;
    }*/
}
