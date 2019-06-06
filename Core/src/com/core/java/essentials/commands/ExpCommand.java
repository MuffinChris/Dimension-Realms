package com.core.java.essentials.commands;

import java.text.DecimalFormat;

import com.destroystokyo.paper.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.core.java.essentials.Main;

public class ExpCommand implements CommandExecutor {
	
	private Main main = Main.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (cmd.getLabel().equalsIgnoreCase("level") || cmd.getLabel().equalsIgnoreCase("lvl") || cmd.getLabel().equalsIgnoreCase("exp") || cmd.getLabel().equalsIgnoreCase("experience") || cmd.getLabel().equalsIgnoreCase("levelup")) {
				if (args.length == 0) {
					Main.msg(p, "");
					Main.msg(p, "&8» &e&lLEVEL: &f" + main.getLevel(p) + " &8/&f 100");
					Main.msg(p, "&8» &e&lEXP: &f" + main.getExp(p) + " &8/&f " + main.getExpMax(p));
					Main.msg(p, "");
				} else if (args.length == 1) {
					if (Bukkit.getPlayer(args[0]) instanceof Player) {
						Player target = (Player) Bukkit.getPlayer(args[0]);
						Main.msg(p, "");
						Main.msg(p, "&8» &e&lLEVEL: &f" + main.getLevel(target) + " &8/&f 100");
						Main.msg(p, "&8» &e&lEXP: &f" + main.getExp(target) + " &8/&f " + main.getExpMax(target));
						Main.msg(p, "");
					} else {
						Main.msg(p, "&cInvalid Player");
					}
				} else {
					Main.msg(p, "Usage: /level <player>");
				}
			}
			if (p.hasPermission("core.admin")) {
				if (cmd.getLabel().equalsIgnoreCase("setlevel")) {
					if (args.length == 2) {
						if (Bukkit.getPlayer(args[0]) instanceof Player) {
							Player target = (Player) Bukkit.getPlayer(args[0]);
							if (Integer.valueOf(args[1]) instanceof Integer) {
								if (Integer.valueOf(args[1]) <= main.getLevel(p)) {
									main.getLevelMap().replace(target.getUniqueId(), Integer.valueOf(args[1]));
									main.setIntValue(target, "Level", Integer.valueOf(args[1]));
									Main.msg(p, "&eSet &6" + target.getName() + "'s &elevel to " + Integer.valueOf(args[1]) + " &8(&cNo Stat Change&8)");
								} else {
									p.performCommand("addlevel " + target.getName() + " " + (Integer.valueOf(args[1]) - main.getLevel(target)));
								}
							} else {
								Main.msg(p, "&cInvalid Value");
							}
						} else {
							Main.msg(p, "&cInvalid Player");
						}
					} else {
						Main.msg(p, "Usage: /setlevel <player> <level>");
					}
				}
				if (cmd.getLabel().equalsIgnoreCase("addlevel")) {
					if (args.length == 2) {
						if (Bukkit.getPlayer(args[0]) instanceof Player) {
							Player target = (Player) Bukkit.getPlayer(args[0]);
							if (Integer.valueOf(args[1]) instanceof Integer) {
								int times = Integer.valueOf(args[1]);
								int newlevel = Integer.valueOf(args[1]) + main.getLevel(target);
								for (int i = 1; i <= times; i++) {
									main.getExpMap().replace(target.getUniqueId(), main.getExpMax(target));
									main.levelup(target, true);
								}
								Main.msg(p, "");
								Main.msg(p, "&7» &e&lLEVEL UP: &6" + (newlevel - times) + " &f-> &6" + newlevel);
								Main.msg(p, "&7» &e&lSP INCREASE: &f+" + (5 * times));
								Main.msg(p, "&7» &4&lAD INCREASE: &f+" + (5 * times));
								Main.msg(p, "&7» &c&lHP INCREASE: &f+" + (100 * times));
								Main.msg(p, "&7» &b&lMANA INCREASE: &f+" + (50 * times));
								Main.msg(p, "");
								Main.msg(p, "&eSet &6" + target.getName() + "'s &elevel to " + newlevel);

								p.sendTitle(new Title(Main.color("&e&lLEVEL UP!"), Main.color("&6" + (newlevel - times) + " &f-> &6" + newlevel), 5, 20, 5));
							} else {
								Main.msg(p, "&cInvalid Value");
							}
						} else {
							Main.msg(p, "&cInvalid Player");
						}
					} else {
						Main.msg(p, "Usage: /addlevel <player> <level>");
					}
				}
				if (cmd.getLabel().equalsIgnoreCase("setexp")) {
					if (args.length == 2) {
						if (Bukkit.getPlayer(args[0]) instanceof Player) {
							Player target = (Player) Bukkit.getPlayer(args[0]);
							if (Integer.valueOf(args[1]) instanceof Integer) {
								main.getExpMap().replace(target.getUniqueId(), Integer.valueOf(args[1]));
								main.setIntValue(target, "Exp", Integer.valueOf(args[1]));
								Main.msg(p, "&eSet &6" + target.getName() + "'s &eEXP to " + Integer.valueOf(args[1]));
								main.levelup(target);
							} else {
								Main.msg(p, "&cInvalid Value");
							}
						} else {
							Main.msg(p, "&cInvalid Player");
						}
					} else {
						Main.msg(p, "Usage: /setexp <player> <exp>");
					}
				}
				if (cmd.getLabel().equalsIgnoreCase("addexp")) {
					if (args.length == 2) {
						if (Bukkit.getPlayer(args[0]) instanceof Player) {
							Player target = (Player) Bukkit.getPlayer(args[0]);
							if (Integer.valueOf(args[1]) instanceof Integer) {
								//int newexp = Integer.valueOf(args[1]) + main.getExp(target);
								giveExp(target, Integer.valueOf(args[1]), 1);
								Main.msg(p, "&eAdded " + args[1] + " EXP to &6" + target.getName());
								main.levelup(target);
							} else {
								Main.msg(p, "&cInvalid Value");
							}
						} else {
							Main.msg(p, "&cInvalid Player");
						}
					} else {
						Main.msg(p, "Usage: /addexp <player> <exp>");
					}
				}
			} else {
				Main.msg(p, main.noperm);
			}
		} else {
			if (cmd.getLabel().equalsIgnoreCase("level") || cmd.getLabel().equalsIgnoreCase("lvl") || cmd.getLabel().equalsIgnoreCase("exp") || cmd.getLabel().equalsIgnoreCase("experience") || cmd.getLabel().equalsIgnoreCase("levelup")) {	
				if (args.length == 1) {
					if (Bukkit.getPlayer(args[0]) instanceof Player) {
						Player target = (Player) Bukkit.getPlayer(args[0]);
						Main.so("&8» &e&lLEVEL: &f" + main.getLevel(target) + " &8/&f 100");
						Main.so("&8» &e&lEXP: &f" + main.getExp(target) + " &8/&f " + main.getExpMax(target));
					} else {
						Main.so("&cInvalid Player");
					}
				} else {
					Main.so("Usage: /level <player>");
				}
			}
			if (cmd.getLabel().equalsIgnoreCase("setlevel")) {
				if (args.length == 2) {
					if (Bukkit.getPlayer(args[0]) instanceof Player) {
						Player target = (Player) Bukkit.getPlayer(args[0]);
						if (Integer.valueOf(args[1]) instanceof Integer) {
							main.getLevelMap().replace(target.getUniqueId(), Integer.valueOf(args[1]));
							main.setIntValue(target, "Level", Integer.valueOf(args[1]));
							Main.so( "&eSet &6" + target.getName() + "'s &elevel to " + Integer.valueOf(args[1]));
							main.levelup(target);
						} else {
							Main.so( "&cInvalid Value");
						}
					} else {
						Main.so( "&cInvalid Player");
					}
				} else {
					Main.so( "Usage: /setlevel <player> <level>");
				}
			}
			if (cmd.getLabel().equalsIgnoreCase("addlevel")) {
				if (args.length == 2) {
					if (Bukkit.getPlayer(args[0]) instanceof Player) {
						Player target = (Player) Bukkit.getPlayer(args[0]);
						if (Integer.valueOf(args[1]) instanceof Integer) {
							int newlevel = Integer.valueOf(args[1]) + main.getLevel(target);
							main.setIntValue(target, "Level", newlevel);
							main.getLevelMap().replace(target.getUniqueId(), newlevel);
							Main.so( "&eSet &6" + target.getName() + "'s &elevel to " + newlevel);
							main.levelup(target);
						} else {
							Main.so( "&cInvalid Value");
						}
					} else {
						Main.so( "&cInvalid Player");
					}
				} else {
					Main.so( "Usage: /addlevel <player> <level>");
				}
			}
			if (cmd.getLabel().equalsIgnoreCase("setexp")) {
				if (args.length == 2) {
					if (Bukkit.getPlayer(args[0]) instanceof Player) {
						Player target = (Player) Bukkit.getPlayer(args[0]);
						if (Integer.valueOf(args[1]) instanceof Integer) {
							main.getExpMap().replace(target.getUniqueId(), Integer.valueOf(args[1]));
							main.setIntValue(target, "Exp", Integer.valueOf(args[1]));
							Main.so( "&eSet &6" + target.getName() + "'s &eEXP to " + Integer.valueOf(args[1]));
							main.levelup(target);
						} else {
							Main.so( "&cInvalid Value");
						}
					} else {
						Main.so( "&cInvalid Player");
					}
				} else {
					Main.so( "Usage: /setexp <player> <exp>");
				}
			}
			if (cmd.getLabel().equalsIgnoreCase("addexp")) {
				if (args.length == 2) {
					if (Bukkit.getPlayer(args[0]) instanceof Player) {
						Player target = (Player) Bukkit.getPlayer(args[0]);
						if (Integer.valueOf(args[1]) instanceof Integer) {
							int newexp = Integer.valueOf(args[1]) + main.getExp(target);
							main.setIntValue(target, "Exp", newexp);
							main.getLevelMap().replace(target.getUniqueId(), newexp);
							Main.so( "&eSet &6" + target.getName() + "'s &eEXP to " + newexp);
							main.levelup(target);
						} else {
							Main.so( "&cInvalid Value");
						}
					} else {
						Main.so( "&cInvalid Player");
					}
				} else {
					Main.so( "Usage: /addexp <player> <exp>");
				}
			}
		}
		return false;
	}
	private Main plugin = Main.getInstance();
	public void giveExp (Player p, int exp, double percent) {
		DecimalFormat df = new DecimalFormat("#.##");
		Main.msg(p, "&7[+" + exp + " &7(" + df.format(percent * 100) + "%) XP]");
		plugin.getExpMap().replace(p.getUniqueId(), exp + plugin.getExp(p));
		plugin.setIntValue(p, "Exp", exp + plugin.getExp(p));
		plugin.levelup(p);
	}
		
}
