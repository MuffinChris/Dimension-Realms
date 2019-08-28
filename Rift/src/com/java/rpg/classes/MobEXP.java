package com.java.rpg.classes;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.java.Main;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class MobEXP implements Listener {

    private Main main = Main.getInstance();

    private static Map<Biome, LevelRange> biomeLevels = new HashMap<>();

    public static Map<Biome, LevelRange> getBL() {
        return biomeLevels;
    }

    public MobEXP() {
        biomeLevels.clear();
        biomeLevels.put(Biome.BADLANDS, new LevelRange(40, 50));
        biomeLevels.put(Biome.BADLANDS_PLATEAU, new LevelRange(45, 50));
        biomeLevels.put(Biome.BAMBOO_JUNGLE, new LevelRange(30, 35));
        biomeLevels.put(Biome.BAMBOO_JUNGLE_HILLS, new LevelRange(30, 35));
        biomeLevels.put(Biome.BEACH, new LevelRange(1, 10));
        biomeLevels.put(Biome.BIRCH_FOREST, new LevelRange(1, 15));
        biomeLevels.put(Biome.BIRCH_FOREST_HILLS, new LevelRange(1, 10));
        biomeLevels.put(Biome.COLD_OCEAN, new LevelRange(10, 25));
        biomeLevels.put(Biome.DARK_FOREST, new LevelRange(20, 35));
        biomeLevels.put(Biome.DARK_FOREST_HILLS, new LevelRange(20, 30));
        biomeLevels.put(Biome.DEEP_COLD_OCEAN, new LevelRange(30, 40));
        biomeLevels.put(Biome.DEEP_FROZEN_OCEAN, new LevelRange(40, 50));
        biomeLevels.put(Biome.DEEP_LUKEWARM_OCEAN, new LevelRange(20, 30));
        biomeLevels.put(Biome.DEEP_OCEAN, new LevelRange(20, 30));
        biomeLevels.put(Biome.DEEP_WARM_OCEAN, new LevelRange(20, 30));
        biomeLevels.put(Biome.DESERT, new LevelRange(15, 25));
        biomeLevels.put(Biome.DESERT_HILLS, new LevelRange(10, 20));
        biomeLevels.put(Biome.DESERT_LAKES, new LevelRange(5, 10));
        biomeLevels.put(Biome.END_BARRENS, new LevelRange(40, 50));
        biomeLevels.put(Biome.END_HIGHLANDS, new LevelRange(40, 50));
        biomeLevels.put(Biome.END_MIDLANDS, new LevelRange(40, 50));
        biomeLevels.put(Biome.ERODED_BADLANDS, new LevelRange(40, 50));
        biomeLevels.put(Biome.FLOWER_FOREST, new LevelRange(1, 15));
        biomeLevels.put(Biome.FOREST, new LevelRange(1, 10));
        biomeLevels.put(Biome.FROZEN_OCEAN, new LevelRange(30, 50));
        biomeLevels.put(Biome.FROZEN_RIVER, new LevelRange(20, 35));
        biomeLevels.put(Biome.GIANT_SPRUCE_TAIGA, new LevelRange(25, 30));
        biomeLevels.put(Biome.GIANT_SPRUCE_TAIGA_HILLS, new LevelRange(25, 30));
        biomeLevels.put(Biome.GIANT_TREE_TAIGA, new LevelRange(25, 35));
        biomeLevels.put(Biome.GIANT_TREE_TAIGA_HILLS, new LevelRange(25, 35));
        biomeLevels.put(Biome.GRAVELLY_MOUNTAINS, new LevelRange(10, 20));
        biomeLevels.put(Biome.ICE_SPIKES, new LevelRange(50, 50));
        biomeLevels.put(Biome.JUNGLE, new LevelRange(30, 40));
        biomeLevels.put(Biome.JUNGLE_EDGE, new LevelRange(25, 35));
        biomeLevels.put(Biome.JUNGLE_HILLS, new LevelRange(30, 40));
        biomeLevels.put(Biome.LUKEWARM_OCEAN, new LevelRange(20, 30));
        biomeLevels.put(Biome.MODIFIED_BADLANDS_PLATEAU, new LevelRange(45, 50));
        biomeLevels.put(Biome.MODIFIED_GRAVELLY_MOUNTAINS, new LevelRange(10, 25));
        biomeLevels.put(Biome.MODIFIED_JUNGLE, new LevelRange(30, 50));
        biomeLevels.put(Biome.MODIFIED_JUNGLE_EDGE, new LevelRange(25, 40));
        biomeLevels.put(Biome.MODIFIED_WOODED_BADLANDS_PLATEAU, new LevelRange(0, 0));
        biomeLevels.put(Biome.MOUNTAIN_EDGE, new LevelRange(10, 20));
        biomeLevels.put(Biome.MOUNTAINS, new LevelRange(15, 25));
        biomeLevels.put(Biome.MUSHROOM_FIELD_SHORE, new LevelRange(20, 30));
        biomeLevels.put(Biome.MUSHROOM_FIELDS, new LevelRange(20, 30));
        biomeLevels.put(Biome.NETHER, new LevelRange(20, 50));
        biomeLevels.put(Biome.OCEAN, new LevelRange(5, 30));
        biomeLevels.put(Biome.PLAINS, new LevelRange(1, 10));
        biomeLevels.put(Biome.RIVER, new LevelRange(1, 15));
        biomeLevels.put(Biome.SAVANNA, new LevelRange(10, 20));
        biomeLevels.put(Biome.SAVANNA_PLATEAU, new LevelRange(10, 25));
        biomeLevels.put(Biome.SHATTERED_SAVANNA, new LevelRange(20, 35));
        biomeLevels.put(Biome.SHATTERED_SAVANNA_PLATEAU, new LevelRange(20, 40));
        biomeLevels.put(Biome.SMALL_END_ISLANDS, new LevelRange(45, 50));
        biomeLevels.put(Biome.SNOWY_BEACH, new LevelRange(15, 20));
        biomeLevels.put(Biome.SNOWY_MOUNTAINS, new LevelRange(15, 25));
        biomeLevels.put(Biome.SNOWY_TAIGA, new LevelRange(15, 25));
        biomeLevels.put(Biome.SNOWY_TAIGA_HILLS, new LevelRange(15, 25));
        biomeLevels.put(Biome.SNOWY_TAIGA_MOUNTAINS, new LevelRange(20, 35));
        biomeLevels.put(Biome.SNOWY_TUNDRA, new LevelRange(10, 25));
        biomeLevels.put(Biome.STONE_SHORE, new LevelRange(5, 15));
        biomeLevels.put(Biome.SUNFLOWER_PLAINS, new LevelRange(1, 5));
        biomeLevels.put(Biome.SWAMP, new LevelRange(35, 45));
        biomeLevels.put(Biome.SWAMP_HILLS, new LevelRange(30, 40));
        biomeLevels.put(Biome.TAIGA, new LevelRange(15, 20));
        biomeLevels.put(Biome.TAIGA_HILLS, new LevelRange(10, 15));
        biomeLevels.put(Biome.TAIGA_MOUNTAINS, new LevelRange(20, 30));
        biomeLevels.put(Biome.TALL_BIRCH_FOREST, new LevelRange(5, 10));
        biomeLevels.put(Biome.TALL_BIRCH_HILLS, new LevelRange(5, 10));
        biomeLevels.put(Biome.THE_END, new LevelRange(50, 50));
        biomeLevels.put(Biome.THE_VOID, new LevelRange(50, 50));
        biomeLevels.put(Biome.WARM_OCEAN, new LevelRange(10, 20));
        biomeLevels.put(Biome.WOODED_BADLANDS_PLATEAU, new LevelRange(35, 45));
        biomeLevels.put(Biome.WOODED_HILLS, new LevelRange(5, 15));
        biomeLevels.put(Biome.WOODED_MOUNTAINS, new LevelRange(5, 20));

    }

    public void setLevel(LivingEntity ent, int level) {
        if (!ent.getName().contains("Lv.")) {
            ent.setCustomName(Main.color(ent.getName() + " &6Lv. " + level));
            ent.setCustomNameVisible(true);
        } else {
            ent.setCustomName(Main.color(ent.getName().substring(0, ent.getName().indexOf("Lv.")) + " &6Lv. " + level));
            ent.setCustomNameVisible(true);
        }
    }

    public void scaleHealth(LivingEntity ent, int level, double modifier) {
        double hp = 400 + level * 50;
        hp*=modifier;
        ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
        ent.setHealth(ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
    }

    public void setExp(LivingEntity ent, double exp) {
        if (ent.hasMetadata("EXP")) {
            ent.removeMetadata("EXP", Main.getInstance());
        }
        ent.setMetadata("EXP", new FixedMetadataValue(Main.getInstance(), exp));
    }

    @EventHandler
    public void onSpawn (EntityAddToWorldEvent e) {
        if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof ArmorStand) && !(e.getEntity() instanceof Player)) {
            new BukkitRunnable() {
                public void run() {
                    LivingEntity ent = (LivingEntity) e.getEntity();
                    int level = biomeLevels.get(ent.getLocation().getBlock().getBiome()).getRandomLevel();
                    setLevel(ent, level);
                    scaleHealth(ent, level, 1);
                    setExp(ent, 25);
                }
            }.runTaskLater(Main.getInstance(), 1);
        }
    }

    @EventHandler
    public void onKill (EntityDeathEvent e) {
        if (e.getEntity().getKiller() instanceof Player) {
            Player p = (Player) e.getEntity().getKiller();
            if (e.getEntity().hasMetadata("EXP")) {
                double exp = (double) e.getEntity().getMetadata("EXP").get(0).value();
                main.getRP(p).giveExpFromSource(p, e.getEntity().getLocation(), exp);
            }
        }
    }

}
