package com.core.java.essentials.commands;

import com.core.java.essentials.Main;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LoreCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("core.admin")) {
                if (args.length == 0) {
                    Main.msg(p,"&fUsage: /addlore <lore>");
                }
                if (args.length >= 1) {
                        String s = "";
                        for (String l : args) {
                            s+=l + " ";
                        }
                        s = Main.color(s);
                        if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() != Material.AIR) {
                            ItemStack i = p.getInventory().getItemInMainHand();
                            List<String> lore = new ArrayList<String>();
                            if (i.hasItemMeta() && i.getItemMeta().getLore() != null && i.getItemMeta().getLore().size() > 0) {
                                lore = i.getItemMeta().getLore();
                            }
                            lore.add(s);
                            ItemMeta meta = i.getItemMeta();
                            meta.setLore(lore);
                            i.setItemMeta(meta);
                            p.getInventory().setItemInMainHand(i);
                            Main.msg(p, "&aChanged Item Lore!");
                        } else {
                            Main.msg(p, "&cYou must be holding an item!");
                        }
                }
            } else {
                Main.msg(p, Main.getInstance().noperm);
            }
        } else {
            Main.so("&cCommand cannot be used from Console.");
        }
        return false;
    }
}
