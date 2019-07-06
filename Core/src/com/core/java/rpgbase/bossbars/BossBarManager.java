package com.core.java.rpgbase.bossbars;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.core.java.essentials.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BossBarManager implements Listener {

	private Map<UUID, BS> bossBars = new HashMap<>();
	
	public void setBossBars(Player p, BS bs) {
		bossBars.put(p.getUniqueId(), bs);
	}
	
	public BS getBS(Player p) {
		return bossBars.get(p.getUniqueId());
	}
	
	public Map<UUID, BS> getBossBars() {
		return bossBars;
	}

	public BossBarManager(Main main) {
		this.main = main;
	}

	private Main main;

	@EventHandler
	public void onJoin (PlayerJoinEvent e) {
		main.getBarManager().setBossBars(e.getPlayer(), new BS(e.getPlayer()));
	}

	@EventHandler
	public void onLeave (PlayerQuitEvent e) {
		if (main.getBarManager().getBossBars().containsKey(e.getPlayer().getUniqueId())) {
			main.getBarManager().getBossBars().remove(e.getPlayer().getUniqueId());
		}
	}
	
}
