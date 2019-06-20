package com.core.java.essentials.commands;

import com.core.java.essentials.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
                if (args.length == 0) {
                    Main.msg(p, "&fUsage: /reset <sp/all/prof> <player>");
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("sp") || args[0].equalsIgnoreCase("skillpoints")) {
                        main.resetSP(p);
                        main.hashmapUpdate(p);
                        Main.msg(p,"&8» &e&lYou have reset your skillpoints.");
                    } else if (args[0].equalsIgnoreCase("prof") || args[0].equalsIgnoreCase("profs") || args[0].equalsIgnoreCase("professions")) {
                        if (main.getProf().containsKey(p.getUniqueId())) {
                            main.getProf().get(p.getUniqueId()).resetProfs();
                            main.hashmapUpdate(p);
                            Main.msg(p, "&8» &e&lYou have reset your professions.");
                        } else {
                            Main.msg(p, "&cPlease wait a moment to use this command.");
                        }
                    } else if (args[0].equalsIgnoreCase("all")) {
                        main.resetPlayer(p);
                        main.hashmapUpdate(p);
                        Main.msg(p,"&8» &e&lYou have FULLY reset yourself.");
                    } else {
                        Main.msg(p, "&cInvalid input.");
                    }
                } else if (args.length == 2) {
                    if (p.hasPermission("core.admin")) {
                    if (Bukkit.getPlayer(args[1]) instanceof Player) {
                        Player t = Bukkit.getPlayer(args[1]);
                        if (args[0].equalsIgnoreCase("sp") || args[0].equalsIgnoreCase("skillpoints")) {
                            main.resetSP(t);
                            main.hashmapUpdate(t);
                            Main.msg(p,"&8» &e&lYou have reset " + t.getName() + "'s skillpoints.");
                        }
                        if (args[0].equalsIgnoreCase("all")) {
                            main.resetPlayer(t);
                            main.hashmapUpdate(t);
                            Main.msg(p,"&8» &e&lYou have FULLY reset " + t.getName() + ".");
                        }
                        if (args[0].equalsIgnoreCase("prof") || args[0].equalsIgnoreCase("profs") || args[0].equalsIgnoreCase("professions")) {
                            if (main.getProf().containsKey(t.getUniqueId())) {
                                main.getProf().get(t.getUniqueId()).resetProfs();
                                main.hashmapUpdate(t);
                                Main.msg(p,"&8» &e&lYou have reset " + t.getName() + "'s professions.");
                            } else {
                                Main.msg(p, "&cTarget's profession object is not instantiated.");
                            }
                        }
                    } else {
                        Main.msg(p, "&cInvalid player.");
                    }
                    } else {
                        Main.msg(p, main.noperm);
                    }
                } else {
                    Main.so("Usage: /reset <sp/all> <player>");
                }
        } else {
            if (args.length == 0) {
                Main.so( "&fUsage: /reset <sp/all> <player>");
            } else if (args.length == 1) {
                Main.so( "&fUsage: /reset <sp/all> <player>");
            } else if (args.length == 2) {
                    if (Bukkit.getPlayer(args[1]) instanceof Player) {
                        Player t = Bukkit.getPlayer(args[1]);
                        if (args[0].equalsIgnoreCase("sp") || args[0].equalsIgnoreCase("skillpoints")) {
                            main.resetSP(t);
                            main.hashmapUpdate(t);
                            Main.so("&8» &e&lYou have reset " + t.getName() + "'s skillpoints.");
                        }
                        if (args[0].equalsIgnoreCase("all")) {
                            main.resetPlayer(t);
                            main.hashmapUpdate(t);
                            Main.so("&8» &e&lYou have FULLY reset " + t.getName() + ".");
                        }
                        if (args[0].equalsIgnoreCase("prof") || args[0].equalsIgnoreCase("profs") || args[0].equalsIgnoreCase("professions")) {
                            if (main.getProf().containsKey(t.getUniqueId())) {
                                main.getProf().get(t.getUniqueId()).resetProfs();
                                main.hashmapUpdate(t);
                                Main.so("&8» &e&lYou have reset " + t.getName() + "'s professions.");
                            } else {
                                Main.so( "&cTarget's profession object is not instantiated.");
                            }
                        }
                    } else {
                        Main.so( "&cInvalid player.");
                    }
            } else {
                Main.so("Usage: /reset <sp/all> <player>");
            }
        }
        return false;
    }
}
