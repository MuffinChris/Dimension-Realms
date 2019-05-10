package com.core.java.essentials;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class KDRListener implements Listener {
	
	@EventHandler
	public void kdr(PlayerDeathEvent e) {
		Player victim = (Player) e.getEntity();
		File pFile = new File("plugins/Core/data/" + victim.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        int deaths = pData.getInt("Deaths");
        try {
            pData.set("Deaths", deaths + 1);
            pData.save(pFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        if (e.getEntity().getKiller() instanceof Player) {
        	Player attacker = (Player) victim.getKiller();
        	pFile = new File("plugins/Core/data/" + attacker.getUniqueId() + ".yml");
            pData = YamlConfiguration.loadConfiguration(pFile);
            int kills = pData.getInt("Kills");
            try {
                pData.set("Kills", kills + 1);
                pData.save(pFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
	}
	
}
