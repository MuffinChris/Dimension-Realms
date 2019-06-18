package com.core.java.essentials.commands;

import com.core.java.essentials.Main;
import net.minecraft.server.v1_14_R1.BiomeColdOcean;
import net.minecraft.server.v1_14_R1.BiomeOcean;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RTPCommand implements CommandExecutor, Listener {

    private Main main = Main.getInstance();

    @EventHandler (priority = EventPriority.HIGHEST)
    public void noFall(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (main.getFall().containsKey(p.getUniqueId())) {
                    if (main.getFall().get(p.getUniqueId())) {
                        e.setCancelled(true);
                        main.getFall().remove(p.getUniqueId());
                        p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 10);
                    }
                }
             }
        }
    }

    @EventHandler
    public void leave (PlayerQuitEvent e) {
        if (main.getFall().containsKey(e.getPlayer().getUniqueId())) {
            main.getFall().remove(e.getPlayer().getUniqueId());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                Main.msg(p, "&7Searching for a suitable location...");
                new BukkitRunnable() {
                    public void run() {
                        File locFile = new File("plugins/Core/locations/rtp.yml");
                        FileConfiguration locData = YamlConfiguration.loadConfiguration(locFile);

                        double rand = Math.random();
                        double neg = Math.random();
                        if (neg <= 0.45) {
                            rand*=-1;
                        }
                        double dist = rand * locData.getDouble("Radius");
                        double xcor = dist + locData.getDouble("CenterX");
                        rand = Math.random();
                        neg = Math.random();
                        if (neg <= 0.45) {
                            rand*=-1;
                        }
                        dist = rand * locData.getDouble("Radius");
                        double zcor = dist + locData.getDouble("CenterZ");
                        Location tp = new Location(p.getWorld(), xcor, 256, zcor);
                        while(p.getWorld().getBiome((int) xcor, (int) zcor).toString().toLowerCase().contains("ocean")) {

                            rand = Math.random();
                            neg = Math.random();
                            if (neg <= 0.45) {
                                rand *= -1;
                            }
                            dist = rand * locData.getDouble("Radius");
                            xcor = dist + locData.getDouble("CenterX");
                            rand = Math.random();
                            neg = Math.random();
                            if (neg <= 0.45) {
                                rand *= -1;
                            }
                            dist = rand * locData.getDouble("Radius");
                            zcor = dist + locData.getDouble("CenterZ");

                            tp = new Location(p.getWorld(), xcor, 256, zcor);
                        }

                        Chunk chunk = tp.getChunk();
                        for (int i = -4; i < 5; i++) {
                            for (int z = -4; z < 5; z++) {
                                Chunk c = p.getWorld().getChunkAt(chunk.getX() + i, chunk.getZ() + z);
                                c.load();
                            }
                        }

                        p.teleport(tp);
                        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 1.0F, 1.0F);

                        Main.msg(p, "&b&lRandomly Teleported through a Dimensional Rift.");
                        if (main.getFall().containsKey(p.getUniqueId())) {
                            main.getFall().replace(p.getUniqueId(), true);
                        } else {
                            main.getFall().put(p.getUniqueId(), true);
                        }
                    }
                }.runTaskLater(Main.getInstance(), 1L);

            } else {
                if (p.hasPermission("core.admin")) {
                    if (args.length == 1) {
                        Main.msg(p, "Usage: /rtp <center/radius> <x,z/int>");
                    } else if (args.length == 2) {
                        if (args[0].equalsIgnoreCase("center")) {
                            if (args[1].split(",") instanceof String[]) {
                                double x = 0;
                                double z = 0;
                                File locFile = new File("plugins/Core/locations/rtp.yml");
                                FileConfiguration locData = YamlConfiguration.loadConfiguration(locFile);
                                try {
                                    x = Double.valueOf(args[1].split(",")[0]);
                                    z = Double.valueOf(args[1].split(",")[1]);
                                    locData.set("CenterX", x);
                                    locData.set("CenterZ", z);
                                    locData.save(locFile);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Main.msg(p, "&cFatal Error.");
                                }
                                Main.msg(p, "&aSet RTP Center to: &f" + x + "&a, &f" + z);
                            } else {
                                Main.msg(p, "&cInvalid args.");
                            }
                        }
                        if (args[0].equalsIgnoreCase("radius")) {
                            if (Double.valueOf(args[1]) instanceof Double) {
                                File locFile = new File("plugins/Core/locations/rtp.yml");
                                FileConfiguration locData = YamlConfiguration.loadConfiguration(locFile);
                                try {
                                    locData.set("Radius", Double.valueOf(args[1]));
                                    locData.save(locFile);
                                } catch (IOException exception) {
                                    exception.printStackTrace();
                                }
                                Main.msg(p, "&aSet RTP radius to: " + args[1]);
                            } else {
                                Main.msg(p, "&cInvalid Double.");
                            }
                        }
                    } else {
                        Main.msg(p, "Usage: /rtp <center/radius> <x,z/int>");
                    }
                } else {
                    Main.msg(p, Main.getInstance().noperm);
                }
            }
        }
        return false;
    }

}
