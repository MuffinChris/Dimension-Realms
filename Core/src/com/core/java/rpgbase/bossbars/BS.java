package com.core.java.rpgbase.bossbars;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.core.java.essentials.Main;

public class BS {

	private UUID uuid;
	private Player p;
	private BossBar bossbar;
	private int timer = 0;
	private Main main = Main.getInstance();
	private boolean playerinf;
	private Entity target;
	
	
	public BS(Player p) {
		this.p = p;
		createBossBar(p, "");
	}
	
	public void createBossBar(Player p, String s) {
		playerinf = true;
		bossbar = Bukkit.getServer().createBossBar(s, BarColor.RED, BarStyle.SOLID);
		uuid = p.getUniqueId();
	}
	
	public String getTitle() {
		return bossbar.getTitle();
	}
	
	public boolean getPlayerInf() {
		return playerinf;
	}
	
	public Entity getTarget() {
		return target;
	}
	
	public void setInfo(Entity e, String s, BarColor bc, BarStyle bs, double progress, boolean b, boolean pinf) {
		playerinf = pinf;
		target = e;
		if (playerinf) {
			timer++;
		}
		if (timer > 128) {
			timer = 0;
		}
		bossbar.setTitle(s);
		bossbar.setColor(bc);
		bossbar.setStyle(bs);
		bossbar.setProgress(progress);
		bossbar.setVisible(b);
		if (b) {
			bossbar.addPlayer(p);
		} else {
			bossbar.removePlayer(p);
		}
		
		if (playerinf) {
			int savedtimer = timer;
			
			new BukkitRunnable() {
				@Override
				public void run() {
					if (bossbar.getPlayers().contains(p) && timer == savedtimer) {
						bossbar.setTitle("");
						bossbar.removePlayer(p);
					}
				}
			}.runTaskLater(main, 100L);
		}
		
	}
	
}
