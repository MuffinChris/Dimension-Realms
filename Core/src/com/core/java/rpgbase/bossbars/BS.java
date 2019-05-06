package com.core.java.rpgbase.bossbars;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.core.java.essentials.Main;

public class BS {

	private UUID uuid;
	private Player p;
	private BossBar bossbar;
	private int timer = 0;
	private Main main = Main.getInstance();
	
	
	public BS(Player p) {
		this.p = p;
		createBossBar(p, "");
	}
	
	public void createBossBar(Player p, String s) {
		bossbar = Bukkit.getServer().createBossBar(s, BarColor.RED, BarStyle.SOLID);
		uuid = p.getUniqueId();
	}
	
	public void setInfo(String s, BarColor bc, BarStyle bs, double progress, boolean b) {
		timer++;
		if (timer > 64) {
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
		
		int savedtimer = timer;
		
		new BukkitRunnable() {
			@Override
			public void run() {
				if (bossbar.getPlayers().contains(p) && timer == savedtimer) {
					bossbar.removePlayer(p);
				}
			}
		}.runTaskLater(main, 100L);
		
	}
	
}