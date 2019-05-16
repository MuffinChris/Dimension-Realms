package com.core.java.rpgbase.entities;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Entity;

public class PlayerListManager {

	private Map<Entity, PlayerList> plist = new HashMap<>();
	
	public void setPlayerList(Entity e, PlayerList l) {
		if (plist.containsKey(e)) {
			plist.replace(e, l);
		} else {
			plist.put(e, l);
		}
	}
	
	public PlayerList getPList(Entity e) {
		return plist.get(e);
	}
	
	public Map<Entity, PlayerList> getPList() {
		return plist;
	}
	
}