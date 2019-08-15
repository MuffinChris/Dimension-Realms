package com.java.rpg;

import com.java.Main;
import com.java.rpg.classes.RPGPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class DamageListener implements Listener {

    private Main main = Main.getInstance();

    @EventHandler
    public void onLeave (PlayerQuitEvent e) {
        List<Damage> remove = new ArrayList<>();
        for (Damage d : main.getDmg()) {
            if (d.getCaster() == e.getPlayer()) {
                remove.add(d);
            }
        }

        try {
            for (Damage d : remove) {
                main.getDmg().remove(d);
            }
        } catch (ConcurrentModificationException ex) {
            Main.so("&cConcModExec when clearing Damage List of logged of player " + e.getPlayer().getName());
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void attack (EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player || e.getDamager() instanceof Projectile) {
            Player damager;
            if (e.getDamager() instanceof Projectile) {
                Projectile p = (Projectile) e.getDamager();
                if (p.getShooter() instanceof Player) {
                    damager = (Player) p.getShooter();
                } else {
                    return;
                }
            } else {
                damager = (Player) e.getDamager();
            }
            if (e.getEntity() instanceof LivingEntity) {
                main.getDmg().add(new Damage(damager, (LivingEntity) e.getEntity(), Damage.DamageType.ATTACK, e.getDamage()));
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onDamage (EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player || e.getDamager() instanceof Projectile) {
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                if (e.getDamager() instanceof Player) {
                    Player d = (Player) e.getDamager();
                    if (main.getPM().hasParty(p) && main.getPM().hasParty(d)) {
                        if (main.getPM().getParty(p).getPlayers().contains(d) && !main.getPM().getParty(p).getPvp()) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                }
                if (e.getDamager() instanceof Projectile) {
                    if (((Projectile) e.getDamager()).getShooter() instanceof Player) {
                        Player d = (Player) ((Projectile) e.getDamager()).getShooter();
                        if (main.getPM().hasParty(p) && main.getPM().hasParty(d)) {
                            if (main.getPM().getParty(p).getPlayers().contains(d) && !main.getPM().getParty(p).getPvp()) {
                                e.setCancelled(true);
                                return;
                            }
                        }
                    }
                }
            }
            int index = 0;
            boolean found = false;
            Player damager;
            if (e.getDamager() instanceof Projectile) {
                Projectile p = (Projectile) e.getDamager();
                if (p.getShooter() instanceof Player) {
                    damager = (Player) p.getShooter();
                } else {
                    return;
                }
            } else {
                damager = (Player) e.getDamager();
            }
            for (Damage d : main.getDmg()) {
                if (d.getPlayer() == e.getEntity()) {
                    if (d.getCaster() == damager) {
                        found = true;
                        double pstrength = 1.0;
                        if (d.getCaster().isOnline()) {
                            pstrength = ((main.getPC().get(damager.getUniqueId()).getPStrength() * 1.0) / 100.0);
                        }
                        if (d.getDamageType() == Damage.DamageType.SPELL_MAGIC) {
                            double damage = d.getDamage() * pstrength;
                            if (e.getEntity() instanceof Player && main.getPC().containsKey((Player) e.getEntity())) {
                                Player p = (Player) e.getEntity();
                                RPGPlayer rp = main.getPC().get(p.getUniqueId());
                                double mr = rp.getPClass().getCalcMR(rp.getLevel());
                                damage = damage * (300.0 / (300 + mr));
                            }
                            e.setDamage(damage);
                            break;
                        }
                        if (d.getDamageType() == Damage.DamageType.SPELL_PHYSICAL) {
                            double damage = d.getDamage() * pstrength;
                            if (e.getEntity() instanceof Player && main.getPC().containsKey((Player) e.getEntity())) {
                                Player p = (Player) e.getEntity();
                                RPGPlayer rp = main.getPC().get(p.getUniqueId());
                                double am = rp.getPClass().getCalcArmor(rp.getLevel());
                                damage = damage * (300.0 / (300 + am));
                            }
                            e.setDamage(damage);
                            break;
                        }
                        if (d.getDamageType() == Damage.DamageType.ATTACK) {
                            double damage = d.getDamage();
                            BlockData blood = Material.REDSTONE_BLOCK.createBlockData();
                            e.getEntity().getWorld().spawnParticle(Particle.BLOCK_DUST, e.getEntity().getLocation(), 100, 0.5, 1, 0.5, blood);
                            double crit = Math.random();
                            if (crit < 0.25) {
                                e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.BLOCK_GRASS_BREAK, 0.5F, 0.5F);
                                damage*=1.25;
                            }
                            if (e.getEntity() instanceof Player && main.getPC().containsKey((Player) e.getEntity())) {
                                Player p = (Player) e.getEntity();
                                RPGPlayer rp = main.getPC().get(p.getUniqueId());
                                double am = rp.getPClass().getCalcArmor(rp.getLevel());
                                damage = damage * (300.0 / (300 + am));
                            }
                            e.setDamage(damage);
                            break;
                        }
                    }
                }
                index++;
            }
            if (found) {
                main.getDmg().get(index).scrub();
                main.getDmg().remove(index);
            }
        }
    }

}
