package com.core.java.essentials;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ChatFunctions implements Listener {
	
	@EventHandler
	public void joinMessage (PlayerJoinEvent e) {
		e.setJoinMessage(Main.color("   &a\u25B6 &f" + e.getPlayer().getName()));
		updateName(e.getPlayer());
	}
	
	public void updateName(Player p) {
		p.setDisplayName(Main.color("&7" + p.getName() + " &8[&e" + Main.getInstance().getLevel(p) + "&8]"));
		if (p.hasPermission("core.helper")) {
			p.setDisplayName(Main.color("&8[&aHelper&8] &f" + p.getName() + " &8[&e" + Main.getInstance().getLevel(p) + "&8]"));
		}
		if (p.hasPermission("core.mod")) {
			p.setDisplayName(Main.color("&8[&bMod&8] &f" + p.getName() + " &8[&e" + Main.getInstance().getLevel(p) + "&8]"));
		}
		if (p.hasPermission("core.admin")) {
			p.setDisplayName(Main.color("&8[&cAdmin&8] &f" + p.getName() + " &8[&e" + Main.getInstance().getLevel(p) + "&8]"));
		}
		if (p.hasPermission("core.owner")) {
			p.setDisplayName(Main.color("&8[&6Owner&8] &e" + p.getName() + " &8[&e" + Main.getInstance().getLevel(p) + "&8]"));
		}
	}
	
	@EventHandler
	public void leaveMessage (PlayerQuitEvent e) {
		e.setQuitMessage(Main.color("   &c\u25C0&f " + e.getPlayer().getName()));
	}
	
	@EventHandler
	public void onChat (AsyncPlayerChatEvent e) {
		if (e.getPlayer().hasPermission("core.chatcolor")) {
			e.setMessage(Main.color(e.getMessage()));
		}
		updateName(e.getPlayer());
		e.setFormat(Main.color("&7%s" + " &8\u00BB" + "&f %s"));
	}
	
}
