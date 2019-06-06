package com.core.java.essentials;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.io.File;
import java.io.IOException;

public class Motd implements Listener {

	@EventHandler
	public void onPing(ServerListPingEvent e) {

		File jFile = new File("plugins/Core/config.yml");
		FileConfiguration jData = YamlConfiguration.loadConfiguration(jFile);
		if (!jData.contains("MOTD")) {
			jData.set("MOTD", "Default MOTD");
			try {
				jData.save(jFile);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		String motd = jData.getString("MOTD");
		e.setMotd(Main.color(motd));
		
	}
	
}
