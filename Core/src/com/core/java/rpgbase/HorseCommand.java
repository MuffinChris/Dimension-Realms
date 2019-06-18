package com.core.java.rpgbase;

import com.core.java.essentials.Main;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HorseCommand implements CommandExecutor, Listener {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                File pFile = new File("plugins/Core/data/horses/" + p.getUniqueId() + ".yml");
                FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
                if (!pData.contains("Name")) {
                    try {
                        pData.set("Name", "None");
                        pData.set("Speed", 0.1);
                        pData.set("Health", 1000.0);
                        pData.set("Level", 0);
                        pData.save(pFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Horse horse = (Horse) p.getWorld().spawnEntity(p.getLocation(), EntityType.HORSE);
                    horse.setOwner(p);
                    horse.setCustomName(Main.color("&8[&6" + pData.get("Level") + "&8] &e" + p.getName() + "'s Horse"));
                    horse.setCustomNameVisible(true);
                    horse.setCanPickupItems(false);
                    horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(pData.getDouble("Health"));
                    horse.setHealth(Double.valueOf(pData.getDouble("Health")));
                    horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(pData.getDouble("Speed"));
                    Main.msg(p, "&eSpawned in your horse.");
                }
            }
        } else {
            Main.so("Console cannot manage Horses.");
        }
        return false;
    }

}
