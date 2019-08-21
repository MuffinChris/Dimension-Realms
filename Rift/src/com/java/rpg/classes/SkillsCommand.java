package com.java.rpg.classes;

import com.java.Main;
import com.java.rpg.party.Party;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class SkillsCommand implements CommandExecutor, Listener {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            sendSkillsInv(p);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
        }
        return false;
    }

    public void sendSkillsInv(Player p) {
        RPGPlayer rp = main.getRP(p);
        Inventory playerInv = Bukkit.createInventory(null, 27, Main.color("&e&l" + rp.getPClass().getName() + " &e&lSkills"));
        ArrayList<String> lore;
        int i = 10;
        for (Skill s : rp.getPClass().getSkills()) {
            ItemStack sp = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            ItemMeta spMeta = sp.getItemMeta();
            spMeta.setDisplayName(Main.color("&e&l" + s.getName()));
            lore = new ArrayList<>();
            if (s.getLevel() <= rp.getLevel()) {
                lore.add(Main.color("&aUNLOCKED"));
                lore.add(Main.color(""));
                lore.add(Main.color("&eSkill Level: &f" + main.getSkillLevel(p, s.getName())));
                lore.add(Main.color(""));
            } else {
                sp.setType(Material.GRAY_STAINED_GLASS_PANE);
                lore.add(Main.color("&cLOCKED &8(&cLVL " + s.getLevel() + "&8)"));
                lore.add(Main.color(""));
            }
            lore.add(Main.color("&e" + s.getDescription()));
            spMeta.setLore(lore);
            sp.setItemMeta(spMeta);
            playerInv.setItem(i, sp);
            i++;
        }
        p.openInventory(playerInv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().contains("Skills")) {
            e.setCancelled(true);
        }
    }

}
