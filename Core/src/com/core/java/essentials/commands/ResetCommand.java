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
                    Main.msg(p, "&fUsage: /reset <sp/all> <player>");
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("sp") || args[0].equalsIgnoreCase("skillpoints")) {
                        main.resetSP(p);
                        main.hashmapUpdate(p);
                        Main.msg(p,"&8» &e&lYou have reset your skillpoints.");
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
