package com.core.java.essentials;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ChatFunctions implements Listener {
	
	@EventHandler
	public void joinMessage (PlayerJoinEvent e) {
		e.setJoinMessage(Main.color("   &a▶ &f" + e.getPlayer().getName()));
		e.getPlayer().setDisplayName(Main.color("&7" + e.getPlayer().getName() + " &8[&e" + Main.getInstance().getLevel(e.getPlayer()) + "&8]"));
		if (e.getPlayer().hasPermission("core.helper")) {
			e.getPlayer().setDisplayName(Main.color("&8[&aHelper&8] &f" + e.getPlayer().getName() + " &8[&e" + Main.getInstance().getLevel(e.getPlayer()) + "&8]"));
		}
		if (e.getPlayer().hasPermission("core.mod")) {
			e.getPlayer().setDisplayName(Main.color("&8[&bMod&8] &f" + e.getPlayer().getName() + " &8[&e" + Main.getInstance().getLevel(e.getPlayer()) + "&8]"));
		}
		if (e.getPlayer().hasPermission("core.admin")) {
			e.getPlayer().setDisplayName(Main.color("&8[&cAdmin&8] &f" + e.getPlayer().getName() + " &8[&e" + Main.getInstance().getLevel(e.getPlayer()) + "&8]"));
		}
		if (e.getPlayer().hasPermission("core.owner")) {
			e.getPlayer().setDisplayName(Main.color("&8[&6Owner&8] &e" + e.getPlayer().getName() + " &8[&e" + Main.getInstance().getLevel(e.getPlayer()) + "&8]"));
		}
	}
	
	@EventHandler
	public void leaveMessage (PlayerQuitEvent e) {
		e.setQuitMessage(Main.color("   &c◀ &f" + e.getPlayer().getName()));
	}
	
	@EventHandler
	public void onChat (AsyncPlayerChatEvent e) {
		if (e.getPlayer().hasPermission("core.chatcolor")) {
			e.setMessage(Main.color(e.getMessage()));
		}
		e.setFormat(Main.color("&7%s" + " &8»" + "&f %s"));
	}
	
}
