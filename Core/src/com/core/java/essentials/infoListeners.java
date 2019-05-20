package com.core.java.essentials;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class infoListeners implements Listener {

	private Main main = Main.getInstance();
	
	@EventHandler
	public void pInfo(PlayerJoinEvent e) {
		main.getpinf().setPinf(e.getPlayer(), new playerinfo(e.getPlayer()));
	}
	
	@EventHandler
	public void pInfoLeave(PlayerQuitEvent e) {
		if (main.getpinf().getPinfs().containsKey(e.getPlayer().getUniqueId())) {
			main.getpinf().getPinfs().remove(e.getPlayer().getUniqueId());
		}
	}
	
}
