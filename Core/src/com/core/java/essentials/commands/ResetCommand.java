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
            if (p.hasPermission("core.admin")) {
                if (args.length == 0) {
                    main.resetPlayer(p);
                    main.hashmapUpdate(p);
                    Main.msg(p,"&8» &e&lYou have reset yourself.");
                } else if (args.length == 1) {
                    if (Bukkit.getPlayer(args[0]) instanceof Player) {
                        Player t = Bukkit.getPlayer(args[0]);
                        Main.msg(p,"&8» &e&lYou have reset &6" + t.getName() + "&e.");
                        main.resetPlayer(t);
                        main.hashmapUpdate(t);
                    } else {
                        Main.msg(p, "&cInvalid player.");
                    }
                } else {
                    Main.so("Usage: /reset <player>");
                }
            } else {
                Main.msg(p, main.noperm);
            }
        } else {
            if (args.length == 1) {
                if (Bukkit.getPlayer(args[0]) instanceof Player) {
                    Player t = Bukkit.getPlayer(args[0]);
                    Main.so("&8» &e&lYou have reset &6" + t.getName() + "&e.");
                    main.resetPlayer(t);
                    main.hashmapUpdate(t);
                } else {
                    Main.so("&cInvalid player.");
                }
            } else {
                Main.so("Usage: /reset <player>");
            }
        }
        return false;
    }
}
