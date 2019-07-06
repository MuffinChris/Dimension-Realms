package com.java.rpg.classes.skills;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.party.Party;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityDestroy;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public class Fireball extends Skill implements Listener {

    private Main main = Main.getInstance();

    private double damage = 200;
    private int range = 3;

    public Fireball() {
        super("Fireball", 75, 4 * 20, 0, 0, "%player% has shot a fireball!");
        setDescription("Shoot a flaming projectile that travels for " + range + " seconds,\ndealing " + damage + " damage and igniting them for 3 seconds.");
    }

    public void cast(Player p) {
        /*Location loc = p.getLocation();
        Vector vec = p.getLocation().getDirection();
        for (int i = 0; i < 200; i++) {
            loc.setX(loc.getX() + i);
            p.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
        }*/
        Location loc = new Location(p.getWorld(), p.getEyeLocation().getX(), p.getEyeLocation().getY() - 0.1, p.getEyeLocation().getZ());
        final Arrow arrow = (Arrow) p.getWorld().spawn(loc, Arrow.class);
        arrow.setCustomName("Fireball:" + damage);
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(arrow.getEntityId());
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            arrow.setShooter(p);
            arrow.setVelocity(p.getEyeLocation().getDirection().multiply(2));
            arrow.setBounce(false);
            arrow.setGravity(false);
            arrow.setKnockbackStrength(0);
            arrow.setSilent(true);
            final BukkitScheduler scheduler = Bukkit.getScheduler();
            final int task = scheduler.scheduleSyncRepeatingTask(main, new Runnable(){
                @Override
                public void run() {
                    if (!arrow.isDead()) {
                        for (Player player : arrow.getLocation().getNearbyPlayers(48)) {
                            player.spawnParticle(Particle.FLAME, arrow.getLocation(), 15, 0.04, 0.04, 0.04, 0.04);
                            //player.spawnParticle(Particle.FLAME, snowball.getLocation(), 200, 0.1, 0.1, 0.1, 0.1);
                            /*if (damageEntities(arrow.getLocation(), p)) {
                                new BukkitRunnable() {
                                    public void run() {
                                        arrow.remove();
                                    }
                                }.runTaskLater(main, 1L);
                            }*/
                        }
                    }
                }
            }, 1, 1);
            final int task2 = scheduler.scheduleSyncRepeatingTask(main, new Runnable(){
                @Override
                public void run() {
                    if (!arrow.isDead()) {
                        for (Player player : arrow.getLocation().getNearbyPlayers(48)) {
                            player.spawnParticle(Particle.FLAME, arrow.getLocation(), 15, 0.04, 0.04, 0.04, 0.04);
                            //player.spawnParticle(Particle.FLAME, snowball.getLocation(), 200, 0.1, 0.1, 0.1, 0.1);
                            /*if (damageEntities(arrow.getLocation(), p)) {
                                new BukkitRunnable() {
                                    public void run() {
                                        arrow.remove();
                                    }
                                }.runTaskLater(main, 1L);
                            }*/
                            if (arrow.isOnGround()) {
                                arrow.remove();
                                arrow.getWorld().spawnParticle(Particle.LAVA, arrow.getLocation(), 50, 0.04, 0.04, 0.04, 0.04);
                            }
                        }
                    }
                }
            }, 0, 1);

            scheduler.scheduleSyncDelayedTask(main, new Runnable(){
                @Override
                public void run(){
                    scheduler.cancelTask(task);
                    scheduler.cancelTask(task2);
                    arrow.remove();
                }
            }, 20 * range);
        }
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
    }

    @EventHandler
    public void onHit (EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Arrow) {
            Arrow a = (Arrow) e.getDamager();
            if (a.getCustomName() instanceof String && a.getCustomName().contains("Fireball:")) {
                if (e.getEntity() instanceof Player) {
                    Player p = (Player) e.getEntity();
                    if (main.getPM().getParty(p) instanceof Party) {
                        if (main.getPM().getParty(p).getPlayers().contains(a.getShooter())) {
                            e.setDamage(0);
                            return;
                        }
                    }
                    if (p.equals(a.getShooter())) {
                        return;
                    }
                }
                e.setDamage(Double.valueOf(a.getCustomName().replace("Fireball:", "")));
                e.getEntity().setFireTicks(60);
                e.getEntity().getWorld().spawnParticle(Particle.LAVA, a.getLocation(), 50, 0.04, 0.04, 0.04, 0.04);
            }
        }
    }

    @EventHandler
    public void dissapear (ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow) {
            if (e.getEntity().getCustomName() instanceof String && e.getEntity().getCustomName().contains("FIREBALL:")) {
                e.getEntity().getWorld().spawnParticle(Particle.LAVA, e.getEntity().getLocation(), 50, 0.04, 0.04, 0.04, 0.04);
                e.getEntity().remove();
            }
        }
    }

    public boolean damageEntities(Location loc, Player caster) {
        Main.so("Ran dmg entities");
        for (LivingEntity ent : loc.getNearbyLivingEntities(0.25)) {
            if (ent instanceof Player) {
                Player p = (Player) ent;
                if (main.getPM().getParty(p) instanceof Party) {
                    if (main.getPM().getParty(p).getPlayers().contains(caster)) {
                        continue;
                    }
                }
                if (p.equals(caster)) {
                    continue;
                }
            }
            ent.setKiller(caster);
            ent.damage(damage);
            ent.setFireTicks(60);
            Main.so("true");
            return true;
        }
        return false;
    }
}
