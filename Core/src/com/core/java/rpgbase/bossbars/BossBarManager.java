package com.core.java.rpgbase.bossbars;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

public class BossBarManager {

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
	
}
