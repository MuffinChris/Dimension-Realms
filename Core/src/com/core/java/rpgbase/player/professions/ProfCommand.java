package com.core.java.rpgbase.player.professions;

import com.core.java.Constants;
import com.core.java.essentials.Main;
import com.core.java.rpgbase.player.Party;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProfCommand implements CommandExecutor, Listener {

    private Main main = Main.getInstance();

    @EventHandler
    public void oreMine (BlockBreakEvent e) {
        if (e.getBlock() != null && e.getBlock().getType() != null) {
            if (isOre(e.getBlock())) {
                if (main.getProf().containsKey(e.getPlayer().getUniqueId())) {
                    PlayerProf pprof = main.getProf().get(e.getPlayer().getUniqueId());
                    double mod = getBlockXP(e.getBlock());
                    double xp = mod * (1.5 * Math.pow(pprof.getLevel("Mining"), 1.8) + 40);
                    double random = Math.random();
                    xp+=random*xp;


                }
            }
        }
    }

    public double getBlockXP(Block i) {
        if (i.getType().toString().toLowerCase().contains("coal")) {
            return 0.4;
        }
        if (i.getType().toString().toLowerCase().contains("iron")) {
            return 0.7;
        }
        if (i.getType().toString().toLowerCase().contains("redstone")) {
            return 0.5;
        }
        if (i.getType().toString().toLowerCase().contains("gold")) {
            return 1.0;
        }
        if (i.getType().toString().toLowerCase().contains("lapis")) {
            return 1.2;
        }
        if (i.getType().toString().toLowerCase().contains("quartz")) {
            return 0.75;
        }
        if (i.getType().toString().toLowerCase().contains("diamond")) {
            return 3.0;
        }
        if (i.getType().toString().toLowerCase().contains("emerald")) {
            return 5.0;
        }
        return 0.0;
    }

    public boolean isOre(Block i) {
        List<Material> ores = new ArrayList<Material>();
        ores.add(Material.COAL_ORE);
        ores.add(Material.IRON_ORE);
        ores.add(Material.REDSTONE_ORE);
        ores.add(Material.GOLD_ORE);
        ores.add(Material.LAPIS_ORE);
        ores.add(Material.NETHER_QUARTZ_ORE);
        ores.add(Material.DIAMOND_ORE);
        ores.add(Material.EMERALD_ORE);
        return ores.contains(i.getType());
    }

    @EventHandler
    public void onJoin (PlayerJoinEvent e) {
        main.getProf().put(e.getPlayer().getUniqueId(), new PlayerProf(e.getPlayer()));
        main.getProf().get(e.getPlayer().getUniqueId()).pullFiles();
    }

    @EventHandler
    public void onLeave (PlayerQuitEvent e) {
        main.getProf().get(e.getPlayer().getUniqueId()).pushFiles();
        if (main.getProf().containsKey(e.getPlayer().getUniqueId())) {
            main.getProf().remove(e.getPlayer().getUniqueId());
        }
    }

    public void sendProfInv(Player r, Player p, boolean thirdparty) {
        String name = "&e&lYOUR PROFESSIONS";
        if (thirdparty) {
            name = "&e&l" + p.getName() + "'s PROFESSIONS";
        }
        Inventory playerInv = Bukkit.createInventory(null, 54, Main.color(name));
        PlayerProf pprof = main.getProf().get(p.getUniqueId());

        ArrayList<String> lore;
        ItemStack sp = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta spMeta = (SkullMeta) sp.getItemMeta();
        spMeta.setOwningPlayer(p);
        spMeta.setDisplayName(Main.color("&ePlayer: &f" + p.getName()));
        lore = new ArrayList<String>();
        lore.add("");
        for (Profession prof : pprof.getProfs()) {
            lore.add(Main.color("&8» &e" + prof.getName() + ": &f" + prof.getLevel()));
        }
        lore.add("");
        spMeta.setLore(lore);
        sp.setItemMeta(spMeta);
        playerInv.setItem(4, sp);

        int start = 9;

        for (Profession prof : pprof.getProfs()) {

            ItemStack profI = new ItemStack(prof.getMaterial());
            ItemMeta profIMeta = profI.getItemMeta();
            profIMeta.setDisplayName(Main.color("&6" + prof.getName()));
            lore = new ArrayList<String>();
            lore.add("");
            DecimalFormat dF = new DecimalFormat("#.##");
            DecimalFormat df = new DecimalFormat("#");
            lore.add(Main.color(prof.getDesc()));
            lore.add("");
            lore.add(Main.color("&8» &eLevel: &f" + prof.getLevel()));
            lore.add(Main.color("&8» &eEXP: &f" + dF.format(prof.getExp()) + "&8/&f" + dF.format(prof.getMaxExp(prof.getLevel())) + " &8(&e" + prof.getPercent() + "&8)"));
            lore.add("");
            profIMeta.setLore(lore);
            profIMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            profI.setItemMeta(profIMeta);

            playerInv.setItem(start, profI);

            ItemStack perk = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
            lore = new ArrayList<String>();
            lore.add("");
            if (prof.getLevel() >= 10) {
                perk = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                lore.add(Main.color("&aUnlocked"));
            } else {
                perk = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                lore.add(Main.color("&cLocked &8(&cLVL 10&8)"));
            }
            lore.add("");
            ItemMeta perkMeta = perk.getItemMeta();
            // make a perk be money making!s
            // make enchanter perk custom enchs

            if (prof.getName().equals("Mining")) {
                perkMeta.setDisplayName(Main.color("&eTempo"));
                // haste after mining chance
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto gain Haste II after mining ore."));
            }
            if (prof.getName().equals("Lumber")) {
                perkMeta.setDisplayName(Main.color("&eProliferate"));
                // chance to drop extra saplings
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto drop extra saplings after breaking leaves."));

            }
            if (prof.getName().equals("Enchanting")) {
                perkMeta.setDisplayName(Main.color("&eEfficiency"));
                // bottle cost reduction
                lore.add(Main.color("&fBottle conversion cost reduced by &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &8(&f/bottle&8)"));

            }
            if (prof.getName().equals("Fishing")) {
                perkMeta.setDisplayName(Main.color("&eDouble Catch"));
                // chance at double fish
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto catch double the fish."));

            }
            if (prof.getName().equals("Farming")) {
                perkMeta.setDisplayName(Main.color("&eBountiful Harvest"));
                // chance at double crop
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto harvest double the crops."));

            }
            perkMeta.setLore(lore);
            perk.setItemMeta(perkMeta);


            playerInv.setItem(start + 9, perk);

            perk = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
            lore = new ArrayList<String>();
            lore.add("");
            if (prof.getLevel() >= 20) {
                perk = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                lore.add(Main.color("&aUnlocked"));
            } else {
                perk = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                lore.add(Main.color("&cLocked &8(&cLVL 20&8)"));
            }
            lore.add("");
            perkMeta = perk.getItemMeta();
            if (prof.getName().equals("Mining")) {
                perkMeta.setDisplayName(Main.color("&eTempo"));
                // haste after mining chance
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto gain Haste II after mining ore."));
            }
            if (prof.getName().equals("Lumber")) {
                perkMeta.setDisplayName(Main.color("&eProliferate"));
                // chance to drop extra saplings
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto drop extra saplings after breaking leaves."));

            }
            if (prof.getName().equals("Enchanting")) {
                perkMeta.setDisplayName(Main.color("&eEfficiency"));
                // bottle cost reduction
                lore.add(Main.color("&fBottle conversion cost reduced by &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &8(&f/bottle&8)"));

            }
            if (prof.getName().equals("Fishing")) {
                perkMeta.setDisplayName(Main.color("&eDouble Catch"));
                // chance at double fish
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto catch double the fish."));

            }
            if (prof.getName().equals("Farming")) {
                perkMeta.setDisplayName(Main.color("&eBountiful Harvest"));
                // chance at double crop
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto harvest double the crops."));

            }
            perkMeta.setLore(lore);
            perk.setItemMeta(perkMeta);


            playerInv.setItem(start + 18, perk);

            perk = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
            lore = new ArrayList<String>();
            lore.add("");
            if (prof.getLevel() >= 30) {
                perk = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                lore.add(Main.color("&aUnlocked"));
            } else {
                perk = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                lore.add(Main.color("&cLocked &8(&cLVL 30&8)"));
            }
            lore.add("");
            perkMeta = perk.getItemMeta();
            if (prof.getName().equals("Mining")) {
                perkMeta.setDisplayName(Main.color("&eTempo"));
                // haste after mining chance
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto gain Haste II after mining ore."));
            }
            if (prof.getName().equals("Lumber")) {
                perkMeta.setDisplayName(Main.color("&eProliferate"));
                // chance to drop extra saplings
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto drop extra saplings after breaking leaves."));

            }
            if (prof.getName().equals("Enchanting")) {
                perkMeta.setDisplayName(Main.color("&eEfficiency"));
                // bottle cost reduction
                lore.add(Main.color("&fBottle conversion cost reduced by &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &8(&f/bottle&8)"));

            }
            if (prof.getName().equals("Fishing")) {
                perkMeta.setDisplayName(Main.color("&eDouble Catch"));
                // chance at double fish
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto catch double the fish."));

            }
            if (prof.getName().equals("Farming")) {
                perkMeta.setDisplayName(Main.color("&eBountiful Harvest"));
                // chance at double crop
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto harvest double the crops."));

            }
            perkMeta.setLore(lore);
            perk.setItemMeta(perkMeta);


            playerInv.setItem(start + 27, perk);


            perk = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
            lore = new ArrayList<String>();
            lore.add("");
            if (prof.getLevel() >= 40) {
                perk = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                lore.add(Main.color("&aUnlocked"));
            } else {
                perk = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                lore.add(Main.color("&cLocked &8(&cLVL 40&8)"));
            }
            lore.add("");
            perkMeta = perk.getItemMeta();
            if (prof.getName().equals("Mining")) {
                perkMeta.setDisplayName(Main.color("&eTempo"));
                // haste after mining chance
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto gain Haste II after mining ore."));
            }
            if (prof.getName().equals("Lumber")) {
                perkMeta.setDisplayName(Main.color("&eProliferate"));
                // chance to drop extra saplings
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto drop extra saplings after breaking leaves."));

            }
            if (prof.getName().equals("Enchanting")) {
                perkMeta.setDisplayName(Main.color("&eEfficiency"));
                // bottle cost reduction
                lore.add(Main.color("&fBottle conversion cost reduced by &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &8(&f/bottle&8)"));

            }
            if (prof.getName().equals("Fishing")) {
                perkMeta.setDisplayName(Main.color("&eDouble Catch"));
                // chance at double fish
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto catch double the fish."));

            }
            if (prof.getName().equals("Farming")) {
                perkMeta.setDisplayName(Main.color("&eBountiful Harvest"));
                // chance at double crop
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto harvest double the crops."));

            }
            perkMeta.setLore(lore);
            perk.setItemMeta(perkMeta);


            playerInv.setItem(start + 36, perk);


            start+=2;
        }

        ItemStack glass = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(Main.color(" "));
        glass.setItemMeta(glassMeta);
        for (int i = 0; i < 54; i++) {
            if (playerInv.getContents()[i] == null) {
                playerInv.setItem(i, glass);
            }
        }

        r.openInventory(playerInv);
    }

    @EventHandler
    public void noClick (InventoryClickEvent e) {
        if (e.getView().getTitle().contains("PROFESSIONS")) {
            e.setCancelled(true);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            main.getProf().get(p.getUniqueId()).pushFiles();
            if (args.length == 0) {
                sendProfInv(p, p, false);
            } else {
                if (Bukkit.getPlayer(args[0]) instanceof Player) {
                    Player t = Bukkit.getPlayer(args[0]);
                    sendProfInv(p, t, true);
                } else {
                    Main.msg(p, "&cInvalid Target.");
                }
            }
        } else {

        }
        return false;
    }

}
