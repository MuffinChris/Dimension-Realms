package com.core.java.essentials.commands;

import com.core.java.essentials.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GodCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("core.admin")) {
                if (args.length == 0) {
                    boolean god = p.isInvulnerable();
                    if (god) {
                        Main.msg(p, "&aYou've left godmode.");
                        main.getGodMode().replace(p.getUniqueId(), false);
                        p.setInvulnerable(false);
                    } else {
                        Main.msg(p, "&aYou've entered godmode.");
                        main.getGodMode().replace(p.getUniqueId(), true);
                        p.setInvulnerable(true);
                    }
                } else {
                    if (Bukkit.getPlayer(args[0]) instanceof Player) {
                        Player t = Bukkit.getPlayer(args[0]);
                        boolean god = t.isInvulnerable();
                        if (god) {
                            Main.msg(p, "&a" + t.getName() + " has left godmode.");
                            main.getGodMode().replace(t.getUniqueId(), false);
                            t.setInvulnerable(false);
                        } else {
                            Main.msg(p, "&a" + t.getName() + " has entered godmode.");
                            main.getGodMode().replace(t.getUniqueId(), true);
                            t.setInvulnerable(true);
                        }
                    } else {
                        Main.msg(p, "&cInvalid player.");
                    }
                }
            } else {
                Main.msg(p, Main.getInstance().noperm);
            }
        } else {
            if (args.length != 1) {
                Main.so("&fUsage: /god <player>");
                return false;
            }
            if (Bukkit.getPlayer(args[0]) instanceof Player) {
                Player t = Bukkit.getPlayer(args[0]);
                boolean god = t.isInvulnerable();
                if (god) {
                    Main.so( "&a" + t.getName() + " has left godmode.");
                    main.getGodMode().replace(t.getUniqueId(), false);
                    t.setInvulnerable(false);
                } else {
                    Main.so( "&a" + t.getName() + " has entered godmode.");
                    main.getGodMode().replace(t.getUniqueId(), true);
                    t.setInvulnerable(true);
                }
            } else {
                Main.so( "&cInvalid player.");
            }
        }
        return false;
    }

}
