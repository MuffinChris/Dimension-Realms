package com.core.java.rpgbase.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

public class PlayerList {

	Map<Player, Double> players;
	
	public PlayerList() {
		players = new HashMap<Player, Double>();
	}
	
	public double getDamage(Player p) {
		if (players.containsKey(p)) {
			return players.get(p);
		}
		return 0.0;
	}
	
	public List<Player> getPlayers() {
		List<Player> ps = new ArrayList<Player>();
		for (Player p : players.keySet()) {
			ps.add(p);
		}
		return ps;
	}

	public void addDamage(Player p, double d) {
		if (players.containsKey(p)) {
			players.replace(p, d + players.get(p));
		} else {
			players.put(p, d);
		}
	}
	
	public Map<Player, Double> getList() {
		return players;
	}
	
}
