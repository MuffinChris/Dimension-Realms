package com.core.java.rpgbase.bossbars;

import java.text.DecimalFormat;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.core.java.essentials.Main;

public class BS {

	private UUID uuid;
	private BossBar bossbar;
	//private int timer = 0;
	private Main main = Main.getInstance();
	//private boolean playerinf;
	//private Entity target;
	
	
	public BS(Player p) {
		createBossBar(p, "");
	}
	
	public void createBossBar(Player p, String s) {
		//playerinf = true;
		bossbar = Bukkit.getServer().createBossBar(s, BarColor.YELLOW, BarStyle.SOLID);
		uuid = p.getUniqueId();
		bossbar.addPlayer(p);
		update();
		bossbar.setVisible(true);
	}
	
	public String getTitle() {
		return bossbar.getTitle();
	}
	
	/*public boolean getPlayerInf() {
		return playerinf;
	}*/
	
	/*public Entity getTarget() {
		return target;
	}*/

	public void update() {
		Player p = Bukkit.getPlayer(uuid);
		DecimalFormat dF = new DecimalFormat("#.##");
		DecimalFormat df = new DecimalFormat("#");
		double max = main.getExpMax(p);
		double exp = main.getExp(p);
		double exppercent = ((1.0 * exp) / (1.0 * max));
		//int mana = main.getMana(p);
		//String hp = "&c" + df.format(p.getHealth()) /*+ "/" + df.format(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue())*/ + " HP";
		//String manastr = "&b" + mana + /*"/" + main.getManaMap().get(uuid) +*/ " MANA";
		String expstr = "&e" + dF.format(exppercent * 100.0) + "% XP";
		String lvlstr = "&6LVL: " + main.getLevel(p);

		//bossbar.setTitle(Main.color(hp + "  " + manastr + "  " + expstr + "  " + lvlstr));

		//double manaprogress = Math.max((1.0D * mana) / (1.0D * main.getManaMap().get(uuid)), 0.0);
		//bossbar.setProgress(Math.min(manaprogress, 1.0));
		bossbar.setTitle(Main.color(lvlstr + "  " + expstr));
		bossbar.setProgress(Math.min(exppercent, 1.0));
	}

	/*
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
		bossbar.setProgress(Math.min(progress, 1.0));
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
		
	}*/
	
}
