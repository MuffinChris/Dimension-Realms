package com.core.java.essentials;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.core.java.rpgbase.bossbars.BS;

public class playerinfoManager {

	private Map<UUID, playerinfo> pinfs = new HashMap<>();
	
	public void setPinf(Player p, playerinfo pi) {
		pinfs.put(p.getUniqueId(), pi);
	}
	
	public playerinfo getPinf(Player p) {
		return pinfs.get(p.getUniqueId());
	}
	
	public Map<UUID, playerinfo> getPinfs() {
		return pinfs;
	}
	
}
