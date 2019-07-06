package com.core.java.rpgbase.player.professions;

import com.core.java.Constants;
import com.core.java.essentials.Main;
import com.core.java.rpgbase.player.Party;
import com.destroystokyo.paper.Title;
import com.sun.scenario.effect.Crop;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftItem;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.block.data.Ageable;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProfCommand implements CommandExecutor, Listener {

    private Main main = Main.getInstance();

    // Add enchant anvil

    @EventHandler
    public void farmInteractEvent (BlockBreakEvent e) {
        //if (e.getAction() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            /*if (e.getClickedBlock() != null) {
                if (main.getProf().containsKey(e.getPlayer().getUniqueId())) {*/
                    //PlayerProf pprof = main.getProf().get(e.getPlayer().getUniqueId());
                    /*if ((e.getClickedBlock().getType() == Material.DIRT || e.getClickedBlock().getType() == Material.GRASS_BLOCK) && e.getItem().getType() != null && e.getItem().getType().toString().toUpperCase().contains("HOE")) {
                        new BukkitRunnable() {
                            public void run() {
                                if (e.getClickedBlock().getType() == Material.FARMLAND) {
                                    double xp = (0.3 * Math.pow(pprof.getLevel("Farming"), 1.8) + 5);
                                    double random = Math.random() * 0.1;
                                    xp+=random*xp;
                                    pprof.giveExp(e.getPlayer(), xp, "Farming", false);
                                    if (e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
                                        e.getClickedBlock().setMetadata("BlockOwner", new FixedMetadataValue(main, e.getPlayer().getUniqueId()));
                                    }
                                }
                            }
                        }.runTaskLater(main, 1L);
                    }
                    if (e.getClickedBlock().getType() == Material.FARMLAND) {
                        if (e.getItem() != null && isPlant(e.getItem())) {
                            if (e.getClickedBlock().hasMetadata("BlockOwner")) {
                                for (MetadataValue mv : e.getClickedBlock().getMetadata("BlockOwner")) {
                                    if (mv.value() == e.getPlayer().getUniqueId()) {
                                        return;
                                    }
                                }
                            }
                            double xp = (0.5 * Math.pow(pprof.getLevel("Farming"), 1.8) + 5);
                            double random = Math.random() * 0.3;
                            xp+=random*xp;
                            pprof.giveExp(e.getPlayer(), xp, "Farming", false);
                        }
                    }*/
                //}
            //}
        //} else
        //if (e.getAction() != null && e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (e.getBlock() != null && e.getBlock().getType() != null && !e.isCancelled()) {
                if (isPlantBlock(e.getBlock()) > 0) {
                    if (main.getProf().containsKey(e.getPlayer().getUniqueId())) {
                        PlayerProf pprof = main.getProf().get(e.getPlayer().getUniqueId());
                        double mod = isPlantBlock(e.getBlock());
                        double mod2 = getGrowthState(e.getBlock(), e.getPlayer());
                        if (mod2 < 0.8 && mod2 >= 0) {
                            return;
                        }
                        if (mod2 < 0) {
                            mod2 = 1.0;
                            if (e.getBlock().hasMetadata("BlockOwner")) {
                                for (MetadataValue mv : e.getBlock().getMetadata("BlockOwner")) {
                                    if (mv.value() == e.getPlayer().getUniqueId()) {
                                        e.getBlock().removeMetadata("BlockOwner", main);
                                        return;
                                    }
                                }
                            }
                        }
                        double xp = mod2 * mod * (2.0 * Math.pow(pprof.getLevel("Farming"), 1.8) + 35);
                        double random = Math.random() * 0.5;
                        xp += random * xp;
                        pprof.giveExp(e.getPlayer(), xp, "Farming", false);
                        if (pprof.getLevel("Farming") >= 10) {
                            giveHarvest(e.getPlayer(), e.getBlock(), pprof);
                        }
                    }
                }
            }
        //}
    }

    public void giveHarvest(Player p, Block b, PlayerProf pprof) {
        if (b.getType() == Material.CHORUS_PLANT || b.getType() == Material.SUGAR_CANE || b.getType() == Material.BAMBOO) {
            ItemStack plant = new ItemStack(getPlantBlock(b));
            int amount = 0;
            for (int i = 0; i < getBrokenHeight(b, p); i++) {
                double random = Math.random();
                if (random <= ((1.0D * pprof.getLevel("Farming")) / (1.0D * Constants.maxLevelProf))) {
                    amount++;
                }
            }
            if (amount > 64) {
                plant.setAmount(64);
            } else {
                plant.setAmount(amount);
            }
            if (amount > 0) {
                p.getInventory().addItem(plant);
                Main.msg(p, "&7[+" + amount + " Harvest&7]");
            }
        } else {
            double random = Math.random();
            if (random <= ((1.0D * pprof.getLevel("Farming")) / (1.0D * Constants.maxLevelProf))) {
                p.getInventory().addItem(new ItemStack(getPlantBlock(b)));
                Main.msg(p.getPlayer(), "&7[+1 Harvest&7]");
            }
        }
    }

    //BEETROOTNOWORK

    public boolean isPlant(ItemStack i) {
        List<Material> mats = new ArrayList<Material>();
        mats.add(Material.WHEAT_SEEDS);
        mats.add(Material.PUMPKIN_SEEDS);
        mats.add(Material.MELON_SEEDS);
        mats.add(Material.BEETROOT_SEEDS);
        mats.add(Material.CARROT);
        mats.add(Material.POTATO);
        for (Material m : mats) {
            if (i.getType() == m) {
                return true;
            }
        }
        return false;
    }

    public int getHeight(Block b) {
        boolean go = true;
        int total = 1;
        int x = 1;
        while (go) {
            if (b.getLocation().add(0, x, 0).getBlock() != null) {
                Block comp = b.getLocation().add(0, x, 0).getBlock();
                if (comp.getType() == b.getType()) {
                    total++;
                    x++;
                } else {
                    go = false;
                }
            } else {
                go = false;
            }
        }
        go = true;
        x = -1;
        while (go) {
            if (b.getLocation().add(0, x, 0).getBlock() != null) {
                Block comp = b.getLocation().add(0, x, 0).getBlock();
                if (comp.getType() == b.getType()) {
                    total++;
                    x--;
                } else {
                    go = false;
                }
            } else {
                go = false;
            }
        }
        return total;
    }

    public int getBrokenHeight(Block b, Player p) {
        boolean go = true;
        int total = 1;
        if (b.hasMetadata("BlockOwner")) {
            for (MetadataValue mv : b.getMetadata("BlockOwner")) {
                if (mv.value() == p.getUniqueId()) {
                    b.removeMetadata("BlockOwner", main);
                    total--;
                }
            }
        }
        int x = 1;
        while (go) {
            if (b.getLocation().add(0, x, 0).getBlock() != null) {
                Block comp = b.getLocation().add(0, x, 0).getBlock();
                if (comp.getType() == b.getType()) {
                    if (comp.hasMetadata("BlockOwner")) {
                        for (MetadataValue mv : comp.getMetadata("BlockOwner")) {
                            if (mv.value() == p.getUniqueId()) {
                                b.removeMetadata("BlockOwner", main);
                                total--;
                            }
                        }
                    }
                    total++;
                    x++;
                } else {
                    go = false;
                }
            } else {
                go = false;
            }
        }
        return total;
    }

    public double getGrowthState(Block b, Player p) {
        if (b.getBlockData() instanceof Ageable) {
            Ageable age = (Ageable) b.getBlockData();
            if (b.getType() == Material.BAMBOO) {
                return getBrokenHeight(b, p);
            }
            if( b.getType() == Material.SUGAR_CANE) {
                return getBrokenHeight(b, p);
            }
            if( b.getType() == Material.CHORUS_PLANT) {
                return getBrokenHeight(b, p);
            }
            if( b.getType() == Material.CACTUS) {
                return getBrokenHeight(b, p);
            }
            return age.getAge() / age.getMaximumAge();
        }
        return -1;
    }

    public Material getPlantBlock(Block b) {
        if (b.getType() == Material.WHEAT) {
            return Material.WHEAT;
        }
        if (b.getType() == Material.BEETROOTS) {
            return Material.BEETROOT;
        }
        if (b.getType() == Material.PUMPKIN) {
            return Material.PUMPKIN;
        }
        if (b.getType() == Material.CARVED_PUMPKIN) {
            return Material.CARVED_PUMPKIN;
        }
        if (b.getType() == Material.MELON) {
            return Material.MELON;
        }
        if (b.getType() == Material.CARROTS) {
            return Material.CARROT;
        }
        if (b.getType() == Material.POTATOES) {
            return Material.POTATO;
        }
        if (b.getType() == Material.BAMBOO) {
            return Material.BAMBOO;
        }
        if (b.getType() == Material.SUGAR_CANE) {
            return Material.SUGAR_CANE;
        }
        if (b.getType() == Material.NETHER_WART) {
            return Material.NETHER_WART;
        }
        if (b.getType() == Material.CHORUS_PLANT) {
            return Material.CHORUS_FRUIT;
        }
        if (b.getType() == Material.CACTUS) {
            return Material.CACTUS;
        }
        if (b.getType() == Material.COCOA) {
            return Material.COCOA;
        }
        return Material.WHEAT;
    }


    public double isPlantBlock(Block b) {
        if (b.getType() == Material.WHEAT) {
            return 1.0;
        }
        if (b.getType() == Material.BEETROOTS) {
            return 0.75;
        }
        if (b.getType() == Material.PUMPKIN) {
            return 2.0;
        }
        if (b.getType() == Material.CARVED_PUMPKIN) {
            return 2.2;
        }
        if (b.getType() == Material.MELON) {
            return 2.0;
        }
        if (b.getType() == Material.CARROTS) {
            return 1.0;
        }
        if (b.getType() == Material.POTATOES) {
            return 1.0;
        }
        if (b.getType() == Material.BAMBOO) {
            return 0.25;
        }
        if (b.getType() == Material.SUGAR_CANE) {
            return 0.3;
        }
        if (b.getType() == Material.NETHER_WART) {
            return 1.5;
        }
        if (b.getType() == Material.CHORUS_PLANT) {
            return 0.8;
        }
        if (b.getType() == Material.CACTUS) {
            return 0.5;
        }
        if (b.getType() == Material.COCOA) {
            return 2.0;
        }
        return 0.0;
    }

    @EventHandler
    public void onPlace (BlockPlaceEvent e) {
        if (!e.isCancelled() && e.getPlayer().getGameMode().equals(GameMode.SURVIVAL) && e.getBlockPlaced() != null && (isOre(e.getBlockPlaced()) || isLog(e.getBlockPlaced()) || isPlantBlock(e.getBlockPlaced()) != 0.0 )) {
            e.getBlock().setMetadata("BlockOwner", new FixedMetadataValue(main, e.getPlayer().getUniqueId()));
        }
    }

    @EventHandler
    public void logBreak (BlockBreakEvent e) {
        if (e.getBlock() != null && e.getBlock().getType() != null && !e.isCancelled()) {
            if (isLog(e.getBlock())) {
                if (e.getBlock().hasMetadata("BlockOwner")) {
                    for (MetadataValue mv : e.getBlock().getMetadata("BlockOwner")) {
                        if (mv.value() == e.getPlayer().getUniqueId()) {
                            e.getBlock().removeMetadata("BlockOwner", main);
                            return;
                        }
                    }
                }
                if (main.getProf().containsKey(e.getPlayer().getUniqueId())) {
                    PlayerProf pprof = main.getProf().get(e.getPlayer().getUniqueId());
                    double mod = getLogXP(e.getBlock());
                    double xp = mod * (0.5 * Math.pow(pprof.getLevel("Lumber"), 1.8) + 20);
                    double random = Math.random() * 0.25;
                    xp+=random*xp;
                    pprof.giveExp(e.getPlayer(), xp, "Lumber", false);
                }
            }
            if (isLeaves(e.getBlock())) {
                if (main.getProf().containsKey(e.getPlayer().getUniqueId())) {
                    PlayerProf pprof = main.getProf().get(e.getPlayer().getUniqueId());
                    if (pprof.getLevel("Lumber") >= 10) {
                        double random = Math.random();
                        if (random <= ((1.0D * pprof.getLevel("Lumber")) / (1.0D * Constants.maxLevelProf))) {
                            e.getPlayer().getInventory().addItem(new ItemStack(getSapling(e.getBlock())));
                            Main.msg(e.getPlayer(), "&7[+1 Sapling&7]");
                        }
                    }
                }
            }
        }
    }

    public Material getSapling(Block b) {
        if (b.getType() == Material.ACACIA_LEAVES) {
            return Material.ACACIA_SAPLING;
        }
        if (b.getType() == Material.BIRCH_LEAVES) {
            return Material.BIRCH_SAPLING;
        }
        if (b.getType() == Material.DARK_OAK_LEAVES) {
            return Material.DARK_OAK_SAPLING;
        }
        if (b.getType() == Material.JUNGLE_LEAVES) {
            return Material.JUNGLE_SAPLING;
        }
        if (b.getType() == Material.SPRUCE_LEAVES) {
            return Material.SPRUCE_SAPLING;
        }
        return Material.OAK_SAPLING;
    }

    public boolean isLeaves(Block b) {
        return (b.getType().toString().toUpperCase().contains("LEAVES"));
    }

    public boolean isLog(Block b) {
        return (b.getType().toString().toUpperCase().contains("LOG") || b.getType().toString().toUpperCase().contains("WOOD"));
    }

    public double getLogXP(Block b) {
        return 1.0;
    }

    @EventHandler
    public void itemEnchant (EnchantItemEvent e) {
        if (main.getProf().containsKey(e.getEnchanter().getUniqueId()) && !e.isCancelled()) {
            PlayerProf pprof = main.getProf().get(e.getEnchanter().getUniqueId());
            double mod = e.getExpLevelCost();
            mod = mod/15.0;
            double xp = mod * (10.0 * Math.pow(pprof.getLevel("Enchanting"), 1.8) + 100);
            double random = Math.random() * 0.5;
            xp+=random*xp;
            pprof.giveExp(e.getEnchanter(), xp, "Enchanting", false);
        }
    }


    @EventHandler
    public void oreMine (BlockBreakEvent e) {
        if (e.getBlock() != null && e.getBlock().getType() != null && !e.isCancelled()) {
            if (isOre(e.getBlock())) {
                if (e.getBlock().hasMetadata("BlockOwner")) {
                    for (MetadataValue mv : e.getBlock().getMetadata("BlockOwner")) {
                        if (mv.value() == e.getPlayer().getUniqueId()) {
                            e.getBlock().removeMetadata("BlockOwner", main);
                            return;
                        }
                    }
                }
                if (main.getProf().containsKey(e.getPlayer().getUniqueId())) {
                    PlayerProf pprof = main.getProf().get(e.getPlayer().getUniqueId());
                    double mod = getBlockXP(e.getBlock());
                    double xp = mod * (0.7 * Math.pow(pprof.getLevel("Mining"), 1.8) + 40);
                    double random = Math.random() * 0.5;
                    xp+=random*xp;
                    pprof.giveExp(e.getPlayer(), xp, "Mining", false);
                    if (pprof.getLevel("Mining") >= 10) {
                        random = Math.random();
                        if (random <= ((1.0D * pprof.getLevel("Mining")) / (1.0D * Constants.maxLevelProf))) {
                            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 40, 2));
                        }
                    }
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
    public void fish (PlayerFishEvent e) {
        if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH && !e.isCancelled()) {
            if (main.getProf().containsKey(e.getPlayer().getUniqueId())) {
                PlayerProf pprof = main.getProf().get(e.getPlayer().getUniqueId());
                double mod = getFishXP(e.getCaught());
                if (mod > 0) {
                    double xp = mod * (1.1 * Math.pow(pprof.getLevel("Fishing"), 1.8) + 50);
                    double random = Math.random() * 0.75;
                    xp+=random*xp;
                    pprof.giveExp(e.getPlayer(), xp, "Fishing", false);
                    if (pprof.getLevel("Fishing") >= 10) {
                        random = Math.random();
                        if (random <= ((1.0D * pprof.getLevel("Fishing")) / (1.0D * Constants.maxLevelProf))) {
                            e.getPlayer().getInventory().addItem(new ItemStack(getFishItem(e.getCaught())));
                            Main.msg(e.getPlayer(), "&7[+1 Fish&7]");
                        }
                    }
                }
            }
        }
    }

    public double getFishXP(Entity e) {
        if (e instanceof CraftItem) {
            CraftItem ci = (CraftItem) e;
            ItemStack i = ci.getItemStack();
            if (i.getType() == Material.COD) {
                return 1.0;
            }
            if (i.getType() == Material.SALMON) {
                return 1.25;
            }
            if (i.getType() == Material.TROPICAL_FISH) {
                return 10;
            }
            if (i.getType() == Material.PUFFERFISH) {
                return 5;
            }
        }
        return 1.0;
    }

    public Material getFishItem(Entity e) {
        if (e instanceof CraftItem) {
            CraftItem ci = (CraftItem) e;
            ItemStack i = ci.getItemStack();
            if (i.getType() == Material.COD) {
                return Material.COD;
            }
            if (i.getType() == Material.SALMON) {
                return Material.SALMON;
            }
            if (i.getType() == Material.TROPICAL_FISH) {
                return Material.TROPICAL_FISH;
            }
            if (i.getType() == Material.PUFFERFISH) {
                return Material.PUFFERFISH;
            }
        }
        return null;
    }

    @EventHandler
    public void onJoin (PlayerJoinEvent e) {
        main.getProf().put(e.getPlayer().getUniqueId(), new PlayerProf(e.getPlayer()));
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
            lore.add(Main.color("&8» &e" + prof.getName() + ": &f" + prof.getLevel() + "&8/&f" + Constants.maxLevelProf + " &8(&e" + prof.getPercent() + "&8)"));
        }
        lore.add("");
        spMeta.setLore(lore);
        sp.setItemMeta(spMeta);
        playerInv.setItem(4, sp);
        if (!thirdparty) {
            ItemStack info = new ItemStack(Material.BOOK);
            ItemMeta infoMeta = info.getItemMeta();
            lore = new ArrayList<String>();
            infoMeta.setDisplayName(Main.color("&eProfessions"));
            lore.add("");
            lore.add(Main.color("&8» &fLevel up professions by completing Vanilla tasks."));
            lore.add(Main.color("&8» &eFor Example: &fMine Ores to level up Mining."));
            lore.add("");
            lore.add(Main.color("&8» &fEvery few levels, you will gain a new perk."));
            lore.add("");
            infoMeta.setLore(lore);
            info.setItemMeta(infoMeta);
            playerInv.setItem(3, info);

            ItemStack reset = new ItemStack(Material.BARRIER);
            ItemMeta resetMeta = reset.getItemMeta();
            resetMeta.setDisplayName(Main.color("&cReset Professions"));
            lore = new ArrayList<>();
            lore.add(Main.color(""));
            lore.add(Main.color("&fReset all profession levels and exp."));
            lore.add(Main.color("&4WARNING: &fThere is no going back!"));
            lore.add(Main.color(""));
            resetMeta.setLore(lore);
            reset.setItemMeta(resetMeta);
            playerInv.setItem(5, reset);
        }

        int start = 11;

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
            lore.add(Main.color("&8» &eEXP: &f" + df.format(prof.getExp()) + "&8/&f" + df.format(prof.getMaxExp(prof.getLevel())) + " &8(&e" + prof.getPercent() + "&8)"));
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
                
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto gain Haste II after mining ore."));
            }
            if (prof.getName().equals("Lumber")) {
                perkMeta.setDisplayName(Main.color("&eProliferate"));
                
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto drop extra saplings after breaking leaves."));

            }
            if (prof.getName().equals("Enchanting")) {
                perkMeta.setDisplayName(Main.color("&eEfficiency"));
                
                double percent = (prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F);
                percent = Math.min(percent, 80);
                lore.add(Main.color("&fBottle conversion cost reduced by &a" + dF.format(percent) + "% &8(&f/bottle&8)"));

            }
            if (prof.getName().equals("Fishing")) {
                perkMeta.setDisplayName(Main.color("&eDouble Catch"));
                
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto catch double the fish."));

            }
            if (prof.getName().equals("Farming")) {
                perkMeta.setDisplayName(Main.color("&eBountiful Harvest"));
                
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
                perkMeta.setDisplayName(Main.color("&eGreed"));
                
                lore.add(Main.color("&fChance of &a" + prof.getProgress() + " &fto collect bonus materials from mining ore."));
            }
            if (prof.getName().equals("Lumber")) {
                perkMeta.setDisplayName(Main.color("&eForage"));
                
                lore.add(Main.color("&fChance of &a" + prof.getProgress() + " &fto drop fruits when breaking leaves."));

            }
            if (prof.getName().equals("Enchanting")) {
                perkMeta.setDisplayName(Main.color("&eAdept Enchanting"));
                
                lore.add(Main.color("&fGain access to the upgraded enchanting terminal."));

            }
            if (prof.getName().equals("Fishing")) {
                perkMeta.setDisplayName(Main.color("&eLure"));
                
                lore.add(Main.color("&fEvery 50 fish caught with a rod, gain one level of Lure."));

            }
            if (prof.getName().equals("Farming")) {
                perkMeta.setDisplayName(Main.color("&eEnhanced Tillage"));
                // RECALL THAT THERE ARE 3 TYPES OF DIRT THAT NEED TO BE CONSIDERED, COARSE WILL BE STRANGE!
                lore.add(Main.color("&fWhile crouched, tilling soil will create a 3x3 block area of farmland."));

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
                perkMeta.setDisplayName(Main.color("&eExcavation"));
                lore.add(Main.color("&fCrouching and mining with a pickaxe will break a 3x3 area of blocks."));
            }
            if (prof.getName().equals("Lumber")) {
                perkMeta.setDisplayName(Main.color("&eTree Felling"));
                lore.add(Main.color("&fCrouching and breaking a log will break all logs in the tree."));

            }
            if (prof.getName().equals("Enchanting")) {
                perkMeta.setDisplayName(Main.color("&eLevel Return"));
                
                lore.add(Main.color("&fChance of &a" + prof.getProgress() + " &fto return half of the levels spent when enchanting."));

            }
            if (prof.getName().equals("Fishing")) {
                perkMeta.setDisplayName(Main.color("&eDouble Catch"));
                
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto catch double the fish."));

            }
            if (prof.getName().equals("Farming")) {
                perkMeta.setDisplayName(Main.color("&eBountiful Harvest"));
                
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
                
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto gain Haste II after mining ore."));
            }
            if (prof.getName().equals("Lumber")) {
                perkMeta.setDisplayName(Main.color("&eProliferate"));
                
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto drop extra saplings after breaking leaves."));

            }
            if (prof.getName().equals("Enchanting")) {
                perkMeta.setDisplayName(Main.color("&eEfficiency"));
                
                lore.add(Main.color("&fBottle conversion cost reduced by &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &8(&f/bottle&8)"));

            }
            if (prof.getName().equals("Fishing")) {
                perkMeta.setDisplayName(Main.color("&eDouble Catch"));
                
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto catch double the fish."));

            }
            if (prof.getName().equals("Farming")) {
                perkMeta.setDisplayName(Main.color("&eBountiful Harvest"));
                
                lore.add(Main.color("&fChance of &a" + dF.format((prof.getLevel() * 100.0F) / (prof.getMaxLevel() * 1.0F)) + "% &fto harvest double the crops."));

            }
            perkMeta.setLore(lore);
            perk.setItemMeta(perkMeta);


            playerInv.setItem(start + 36, perk);


            start++;
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
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.BARRIER) {
                if (e.getWhoClicked() instanceof Player) {
                    Player p = (Player) e.getWhoClicked();
                    if (main.getProf().containsKey(p.getUniqueId())) {
                        main.getProf().get(p.getUniqueId()).resetProfs();
                        main.hashmapUpdate(p);
                        Main.msg(p, "&8» &e&lYou have reset your professions.");
                    } else {
                        Main.msg(p, "&cPlease wait a moment to use this command.");
                    }
                    sendProfInv(p, p, false);
                }
            }
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
                if (args.length == 1) {
                    if (Bukkit.getPlayer(args[0]) instanceof Player) {
                        Player t = Bukkit.getPlayer(args[0]);
                        sendProfInv(p, t, true);
                    } else {
                        Main.msg(p, "&cInvalid Target.");
                    }
                    return false;
                }
                if (args.length == 4) {
                    if (args[0].equalsIgnoreCase("addexp") || args[0].equalsIgnoreCase("giveexp")) {
                        if (p.hasPermission("core.admin")) {
                            if (Bukkit.getPlayer(args[1]) instanceof Player) {
                                Player t = Bukkit.getPlayer(args[1]);
                                if (Double.valueOf(args[2]) instanceof Double && Double.valueOf(args[2]) >= 0) {
                                    double exp = Double.valueOf(args[2]);
                                    PlayerProf pprof = main.getProf().get(t.getUniqueId());
                                    pprof.giveExp(t, exp, args[3], true);
                                    Main.msg(p, "&eGave &6" + t.getName() + " &e" + exp + " &6" + args[3] + " &eEXP.");
                                    pprof.levelup(t);
                                } else {
                                    Main.msg(p, "&cInvalid EXP Argument");
                                }
                            } else {
                                Main.msg(p, "&cInvalid Target.");
                            }
                        } else {
                            Main.msg(p, main.noperm);
                        }
                    }
                    if (args[0].equalsIgnoreCase("setexp")) {
                        if (p.hasPermission("core.admin")) {
                            if (Bukkit.getPlayer(args[1]) instanceof Player) {
                                Player t = Bukkit.getPlayer(args[1]);
                                if (Double.valueOf(args[2]) instanceof Double && Double.valueOf(args[2]) >= 0) {
                                    double exp = Double.valueOf(args[2]);
                                    PlayerProf pprof = main.getProf().get(t.getUniqueId());
                                    pprof.setExp(t, exp, args[3]);
                                    Main.msg(p, "&eSet &6" + t.getName() + "'s &eEXP to " + exp + " for &6" + args[3] + "&e.");
                                    pprof.levelup(t);
                                } else {
                                    Main.msg(p, "&cInvalid EXP Argument");
                                }
                            } else {
                                Main.msg(p, "&cInvalid Target.");
                            }
                        } else {
                            Main.msg(p, main.noperm);
                        }
                    }
                    if (args[0].equalsIgnoreCase("addlevel") || args[0].equalsIgnoreCase("givelevel")) {
                        if (p.hasPermission("core.admin")) {
                            if (Bukkit.getPlayer(args[1]) instanceof Player) {
                                Player t = Bukkit.getPlayer(args[1]);
                                if (Integer.valueOf(args[2]) instanceof Integer && Integer.valueOf(args[2]) >= 0) {
                                    int lvl = Integer.valueOf(args[2]);
                                    PlayerProf pprof = main.getProf().get(t.getUniqueId());
                                    pprof.giveLevel(t, lvl, args[3]);
                                    Main.msg(p, "&eGave &6" + t.getName() + " &e" + lvl + " &6" + args[3] + " &elevels.");
                                    pprof.levelup(t);
                                } else {
                                    Main.msg(p, "&cInvalid EXP Argument");
                                }
                            } else {
                                Main.msg(p, "&cInvalid Target.");
                            }
                        } else {
                            Main.msg(p, main.noperm);
                        }
                    }
                    if (args[0].equalsIgnoreCase("setlevel")) {
                        if (p.hasPermission("core.admin")) {
                            if (Bukkit.getPlayer(args[1]) instanceof Player) {
                                Player t = Bukkit.getPlayer(args[1]);
                                if (Integer.valueOf(args[2]) instanceof Integer && Integer.valueOf(args[2]) >= 0) {
                                    int lvl = Integer.valueOf(args[2]);
                                    PlayerProf pprof = main.getProf().get(t.getUniqueId());
                                    pprof.setLevel(t, lvl, args[3]);
                                    Main.msg(p, "&eSet &6" + t.getName() + "'s &eLVL to " + lvl + " for &6" + args[3] + "&e.");
                                    pprof.levelup(t);
                                } else {
                                    Main.msg(p, "&cInvalid EXP Argument");
                                }
                            } else {
                                Main.msg(p, "&cInvalid Target.");
                            }
                        } else {
                            Main.msg(p, main.noperm);
                        }
                    }
                } else {
                    if (p.hasPermission("core.admin")) {
                        Main.msg(p, "&fUsage: /prof <addexp/addlevel/setexp/setlevel> <player> <amount> <prof>");
                    } else {
                        Main.msg(p, main.noperm);
                    }
                }
            }
        } else {

        }
        return false;
    }

}
