package com.core.java.essentials;

import java.util.UUID;

import org.bukkit.entity.Player;

public class playerinfo {

	private UUID uuid;
	private Player reply;
	
	
	
	public playerinfo(Player p) {
		uuid = p.getUniqueId();
		reply = null;
	}
	
	public Player getReply() {
		return reply;
	}
	
	public void setReply(Player p) {
		reply = p;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
}
