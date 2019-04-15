package com.core.java.essentials;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class Motd implements Listener {

	@EventHandler
	public void onPing(ServerListPingEvent e) {
		
		e.setMotd(Main.color("&9&lDimension Realms\n&fIn Development"));
		
	}
	
}
