package com.core.java.essentials;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.event.server.ServiceUnregisterEvent;

public class ChatFunctions implements Listener {

	private Main main = Main.getInstance();

	@EventHandler
	public void joinMessage (PlayerJoinEvent e) {
		Chat c = main.getChat();
		updateName(e.getPlayer());
		String prefix = c.getPlayerPrefix(e.getPlayer());
		if (prefix.length() > 2) {
			prefix+=" ";
		}
		e.setJoinMessage(Main.color("   &a\u25B6 &f" + prefix + e.getPlayer().getName() + " " + c.getPlayerSuffix(e.getPlayer())));
		Main.so(Main.color("   &a\u25B6 &f" + prefix + e.getPlayer().getName() + " " + c.getPlayerSuffix(e.getPlayer())));
	}
	
	public static void updateName(Player p) {
		Chat c = Main.getInstance().getChat();
		//c.setPlayerPrefix(p, Main.color("&7"));
		c.setPlayerSuffix(p, Main.color("&8[&e" + Main.getInstance().getLevel(p) + "&8]"));
		/*if (p.hasPermission("core.helper")) {
			c.setPlayerPrefix(p, Main.color("&8[&aHelper&8]&f"));
		}
		if (p.hasPermission("core.mod")) {
			c.setPlayerPrefix(p, Main.color("&8[&bMod&8]&f"));
		}
		if (p.hasPermission("core.admin")) {
			c.setPlayerPrefix(p, Main.color("&8[&cAdmin&8]&f"));
		}
		if (p.hasPermission("core.owner")) {
			c.setPlayerPrefix(p, Main.color("&8[&6Owner&8]&e"));
		}*/
	}
	
	@EventHandler
	public void leaveMessage (PlayerQuitEvent e) {
		Chat c = main.getChat();
		String prefix = c.getPlayerPrefix(e.getPlayer());
		if (prefix.length() > 2) {
			prefix+=" ";
		}
		e.setQuitMessage(Main.color("   &c\u25C0 &f" + prefix + e.getPlayer().getName() + " " + c.getPlayerSuffix(e.getPlayer())));
	}
	
	@EventHandler
	public void onChat (AsyncPlayerChatEvent e) {
		Chat chat = main.getChat();
		if (e.getPlayer().hasPermission("core.chatcolor")) {
			e.setMessage(Main.color(e.getMessage()));
		}
		updateName(e.getPlayer());
		String prefix = chat.getPlayerPrefix(e.getPlayer());
		if (prefix.length() > 2) {
			prefix+=" ";
		}
		String format = e.getFormat();
		format = Main.color(prefix + "%s " + chat.getPlayerSuffix(e.getPlayer()) + " &8\u00BB" + "&f %s");
		e.setFormat(format);
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (e.getMessage().contains(p.getName()) || e.getMessage().contains(p.getDisplayName())) {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
			}
		}
	}

}
