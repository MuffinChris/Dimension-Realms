package com.core.java.essentials.commands;

import java.io.File;
import java.text.DecimalFormat;

import com.core.java.Constants;
import com.destroystokyo.paper.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.core.java.essentials.Main;

public class ExpCommand implements CommandExecutor {
	
	private Main main = Main.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			DecimalFormat df = new DecimalFormat("#");
			if (cmd.getLabel().equalsIgnoreCase("level") || cmd.getLabel().equalsIgnoreCase("lvl") || cmd.getLabel().equalsIgnoreCase("exp") || cmd.getLabel().equalsIgnoreCase("experience") || cmd.getLabel().equalsIgnoreCase("levelup")) {
				if (args.length == 0) {
					Main.msg(p, "");
					Main.msg(p, "&8» &e&lLEVEL: &f" + main.getLevel(p) + " &8/&f 100");
					Main.msg(p, "&8» &e&lEXP: &f" + df.format(main.getExp(p)) + " &8/&f " + df.format(main.getExpMax(p)));
					Main.msg(p, "");
				} else if (args.length == 1) {
					if (Bukkit.getPlayer(args[0]) instanceof Player) {
						Player target = (Player) Bukkit.getPlayer(args[0]);
						Main.msg(p, "");
						Main.msg(p, "&8» &e&lLEVEL: &f" + main.getLevel(target) + " &8/&f 100");
						Main.msg(p, "&8» &e&lEXP: &f" + df.format(main.getExp(target)) + " &8/&f " + df.format(main.getExpMax(target)));
						Main.msg(p, "");
					} else {
						Main.msg(p, "&cInvalid Player");
					}
				} else {
					Main.msg(p, "Usage: /level <player>");
				}
				return false;
			}
			if (p.hasPermission("core.admin")) {
				if (cmd.getLabel().equalsIgnoreCase("setlevel")) {
					if (args.length == 2) {
						if (Bukkit.getPlayer(args[0]) instanceof Player) {
							Player target = (Player) Bukkit.getPlayer(args[0]);
							if (Integer.valueOf(args[1]) instanceof Integer) {
								int val = Integer.valueOf(args[1]);
								val = Math.min(100, val);
								if (val <= main.getLevel(target)) {
									main.getLevelMap().replace(target.getUniqueId(), val);
									main.setIntValue(target, "Level", val);
									Main.msg(p, "&eSet &6" + target.getName() + "'s &elevel to " + val + " &8(&cNo Stat Change&8)");
								} else {
									p.performCommand("addlevel " + target.getName() + " " + (val - main.getLevel(target)));
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
								if (times + main.getLevel(target) > 100) {
									times = 100 - main.getLevel(target);
								}
								int newlevel = times + main.getLevel(target);
								main.setIntValue( target, "Level", newlevel);
								main.getLevelMap().replace(target.getUniqueId(), newlevel);
								Main.msg(target, "");
								Main.msg(target, "&7» &e&lLEVEL UP: &6" + (newlevel - times) + " &f-> &6" + newlevel);
								Main.msg(target, "&7» &e&lSP INCREASE: &f+" + (Constants.SPPerLevel * times));
								Main.msg(target, "&7» &4&lAD INCREASE: &f+" + (Constants.ADPerLevel * times));
								Main.msg(target, "&7» &c&lHP INCREASE: &f+" + (Constants.HPPerLevel * times));
								Main.msg(target, "&7» &b&lMANA INCREASE: &f+" + (Constants.ManaPerLevel * times));
								Main.msg(target, "");
								Main.msg(p, "&eSet &6" + target.getName() + "'s &elevel to " + newlevel);
								main.setupStats(target);
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
							if (Double.valueOf(args[1]) instanceof Double) {
								main.getExpMap().replace(target.getUniqueId(), Double.valueOf(args[1]));
								main.setDoubleValue(target, "Exp", Double.valueOf(args[1]));
								Main.msg(p, "&eSet &6" + target.getName() + "'s &eEXP to " + df.format(Double.valueOf(args[1])));
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
							if (Double.valueOf(args[1]) instanceof Double) {
								//int newexp = Integer.valueOf(args[1]) + main.getExp(target);
								giveExp(target, Double.valueOf(args[1]));
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
					Player target = (Player) Bukkit.getPlayer(args[0]);
					if (Integer.valueOf(args[1]) instanceof Integer) {
						if (Integer.valueOf(args[1]) <= main.getLevel(target)) {
							main.getLevelMap().replace(target.getUniqueId(), Integer.valueOf(args[1]));
							main.setIntValue(target, "Level", Integer.valueOf(args[1]));
							Main.so( "&eSet &6" + target.getName() + "'s &elevel to " + Integer.valueOf(args[1]) + " &8(&cNo Stat Change&8)");
						} else {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "addlevel " + target.getName() + " " + (Integer.valueOf(args[1]) - main.getLevel(target)));
						}
					} else {
						Main.so( "&cInvalid Value");
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
							int times = Integer.valueOf(args[1]);
							if (times + main.getLevel(target) > 100) {
								times = 100 - main.getLevel(target);
							}
							int newlevel = times + main.getLevel(target);
							/*for (int i = 1; i <= times; i++) {
								main.getExpMap().replace(target.getUniqueId(), main.getExpMax(target));
								main.levelup(target, true);
							}*/
							main.setIntValue( target, "Level", newlevel);
							main.getLevelMap().replace(target.getUniqueId(), newlevel);
							Main.msg(target, "");
							Main.msg(target, "&7» &e&lLEVEL UP: &6" + (newlevel - times) + " &f-> &6" + newlevel);
							Main.msg(target, "&7» &e&lSP INCREASE: &f+" + (Constants.SPPerLevel * times));
							Main.msg(target, "&7» &4&lAD INCREASE: &f+" + (Constants.ADPerLevel * times));
							Main.msg(target, "&7» &c&lHP INCREASE: &f+" + (Constants.HPPerLevel * times));
							Main.msg(target, "&7» &b&lMANA INCREASE: &f+" + (Constants.ManaPerLevel * times));
							Main.msg(target, "");
							Main.so( "&eSet &6" + target.getName() + "'s &elevel to " + newlevel);
							main.setupStats(target);
							target.sendTitle(new Title(Main.color("&e&lLEVEL UP!"), Main.color("&6" + (newlevel - times) + " &f-> &6" + newlevel), 5, 20, 5));
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
						if (Double.valueOf(args[1]) instanceof Double) {
							main.getExpMap().replace(target.getUniqueId(), Double.valueOf(args[1]));
							main.setDoubleValue(target, "Exp", Double.valueOf(args[1]));
							Main.so( "&eSet &6" + target.getName() + "'s &eEXP to " + Double.valueOf(args[1]));
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
						if (Double.valueOf(args[1]) instanceof Double) {
							//int newexp = Integer.valueOf(args[1]) + main.getExp(target);
							giveExp(target, Double.valueOf(args[1]));
							Main.so( "&eAdded " + args[1] + " EXP to &6" + target.getName());
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
	public void giveExp (Player p, double exp) {
		DecimalFormat df = new DecimalFormat("#");
		if (Double.valueOf(exp) instanceof Double) {
			Main.msg(p, "&7[+" + df.format(exp) + " XP]");
			plugin.getExpMap().replace(p.getUniqueId(), exp + plugin.getExp(p));
			plugin.setDoubleValue(p, "Exp", exp + plugin.getExp(p));
			plugin.levelup(p);
		}
	}
		
}
