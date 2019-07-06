package com.java.rpg.classes;

import com.java.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class SkillCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length > 0) {
                String flavor = main.getPC().get(p).castSkill(args[0]);
                if (flavor.equalsIgnoreCase("NoMana")) {
                    Main.msg(p, "&cNot enough mana!");
                } else if (flavor.contains("Level")) {
                    int level = Integer.valueOf(flavor.replace("Level", ""));
                    Main.msg(p, "&cYou must be level &f" + level + " &cto use that skill.");
                } else if (flavor.contains("CD:")) {
                    double cd = Double.valueOf(flavor.replace("CD:", ""));
                    DecimalFormat dF = new DecimalFormat("#.##");
                    String skill = args[0].substring(0, 1).toUpperCase() + args[0].substring(1).toLowerCase();
                    Main.msg(p, "&c" + skill + " on cooldown: &f" + dF.format(cd) + "s");
                } else if (flavor.contains("Failure")) {
                    Main.msg(p, "&cInternal Skill Error. Please let Admins know what happened.");
                } else if (flavor.contains("Invalid")) {
                    Main.msg(p, "&cInvalid Skill Name!");
                }
            } else {
                Main.msg(p, "&fUsage: /skill <skill>");
            }
        } else {
            Main.so("&cNot a console cmd");
        }
        return false;
    }

}
