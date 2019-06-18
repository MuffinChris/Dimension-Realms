package com.core.java.essentials.commands;

import com.core.java.Constants;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.core.java.essentials.Main;

import java.text.DecimalFormat;

public class InfoCommand implements CommandExecutor {

	private Main main = Main.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length == 0) {
				DecimalFormat dF = new DecimalFormat("#.##");
				DecimalFormat df = new DecimalFormat("#");
				int SPAD = Integer.valueOf(Main.getValue(p, "SPAD"));
				double ad = SPAD * Constants.ADPerSP * 100;
				ad = Double.valueOf(dF.format(ad));
				Main.msg(p, "");
				Main.msg(p, "&8» &e&lLEVEL: &f" + main.getLevel(p) + " &8/&f 100");
				Main.msg(p, "&8» &e&lEXP: &f" + df.format(main.getExp(p)) + " &8/&f " + df.format(main.getExpMax(p)));
				Main.msg(p, "&8» &b&lMANA: &f" + main.getManaMap().get(p.getUniqueId()) + " &8(&b" + (main.getManaRegenMap().get(p.getUniqueId())) + "/s&8)");
				Main.msg(p, "&8» &4&lAD: &f+" + ad + "%" + "  &c&lHP: &f" + p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
				Main.msg(p, "");
			} else if (args.length == 1) {
				if (Bukkit.getPlayer(args[0]) instanceof Player) {
					Player t = Bukkit.getPlayer(args[0]);
					DecimalFormat dF = new DecimalFormat("#.##");
					DecimalFormat df = new DecimalFormat("#");
					int SPAD = Integer.valueOf(Main.getValue(t, "SPAD"));
					double ad = SPAD * Constants.ADPerSP * 100;
					ad = Double.valueOf(dF.format(ad));
					Main.msg(p, "");
					Main.msg(p, "&8» &e&lLEVEL: &f" + main.getLevel(t) + " &8/&f 100");
					Main.msg(p, "&8» &e&lEXP: &f" + df.format(main.getExp(t)) + " &8/&f " + df.format(main.getExpMax(t)));
					Main.msg(p, "&8» &b&lMANA: &f" + main.getManaMap().get(t.getUniqueId()) + " &8(&b" + (main.getManaRegenMap().get(t.getUniqueId())) + "/s&8)");
					Main.msg(p, "&8» &4&lAD: &f+" + ad + "%" + "  &c&lHP: &f" + t.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
					Main.msg(p, "");
				} else {
					Main.msg(p, "&cInvalid Player");
				}
			} else {
				Main.msg(p, "Usage: /info <player>");
			}
		} else {
			if (args.length == 1) {
				if (Bukkit.getPlayer(args[0]) instanceof Player) {
					Player t = Bukkit.getPlayer(args[0]);
					DecimalFormat dF = new DecimalFormat("#.##");
					DecimalFormat df = new DecimalFormat("#");
					int SPAD = Integer.valueOf(Main.getValue(t, "SPAD"));
					double ad = SPAD * Constants.ADPerSP * 100;
					ad = Double.valueOf(dF.format(ad));
					Main.so("");
					Main.so("&8» &e&lLEVEL: &f" + main.getLevel(t) + " &8/&f 100");
					Main.so("&8» &e&lEXP: &f" + df.format(main.getExp(t)) + " &8/&f " + df.format(main.getExpMax(t)));
					Main.so("&8» &b&lMANA: &f" + main.getManaMap().get(t.getUniqueId()) + " &8(&b" + (main.getManaRegenMap().get(t.getUniqueId())) + "/s&8)");
					Main.so("&8» &4&lAD: &f+" + ad + "%" + "  &c&lHP: &f" + t.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
					Main.so("");
				} else {
					Main.so("&cInvalid Player");
				}
			} else {
				Main.so("Usage: /info <player>");
			}
		}
		return false;
	}
	
}
