package com.core.java;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class Motd implements Listener {

	@EventHandler
	public void onPing(ServerListPingEvent e) {
		
		e.setMotd(Main.color("&bDimension Realms 2019 Development Server"));
		
	}
	
}
