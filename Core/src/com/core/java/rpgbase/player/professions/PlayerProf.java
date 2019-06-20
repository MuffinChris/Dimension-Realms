package com.core.java.rpgbase.player.professions;

import com.core.java.Constants;
import com.core.java.essentials.Main;
import com.destroystokyo.paper.Title;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerProf {

    private UUID uuid;
    List<Profession> profs;

    public void resetProfsList() {
        profs = new ArrayList<Profession>();
        for (Profession prof : Constants.baseProfs) {
            profs.add(new Profession(prof.getName(), prof.getMaxLevel(), prof.getLevel(), prof.getExp(), prof.getDesc()));
        }
    }

    public void resetProfs() {
        resetProfsList();
        pushFiles();
    }

    public UUID getUUID() {
        return uuid;
    }

    public List<Profession> getProfs() {
        return profs;
    }

    public PlayerProf(Player p) {
        uuid = p.getUniqueId();
        pullFiles();
    }

    public void pullFiles() {
        File pFile = new File("plugins/Core/data/profs/" + uuid + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        if (pFile.exists()) {
            if (profs == null) {
                resetProfsList();
            }
            for (Profession p : profs) {
                p.setLevel(pData.getInt(p.getName() + "LVL"));
                p.setExp(pData.getDouble(p.getName() + "EXP"));
            }
        } else {
            resetProfsList();
            pushFiles();
        }
    }

    public void giveExp(Player p, double exp, String profstr, boolean silent) {
        int index = 0;
        for (Profession prof : profs) {
            if (prof.getName().equalsIgnoreCase(profstr)) {
                profs.get(index).giveExp(exp, p, silent);
                pushFiles();
                return;
            }
            index++;
        }
    }

    public void setExp(Player p, double exp, String profstr) {
        int index = 0;
        for (Profession prof : profs) {
            if (prof.getName().equalsIgnoreCase(profstr)) {
                profs.get(index).setExp(exp, p);
                pushFiles();
                return;
            }
            index++;
        }
    }

    public void giveLevel(Player p, int lvl, String profstr) {
        int index = 0;
        for (Profession prof : profs) {
            if (prof.getName().equalsIgnoreCase(profstr)) {
                int newlevel = lvl + profs.get(index).getLevel();
                int prevlevel = profs.get(index).getLevel();
                Main.msg(p, "");
                Main.msg(p, "&7» &e&lLEVEL UP &8(&e" + profs.get(index).getName() + "&8)&e: &6" + (prevlevel) + " &f-> &6" + newlevel);
                Main.msg(p, "");
                p.sendTitle(new Title(Main.color("&e&lLEVEL UP &8(&e" + profs.get(index).getName() + "&8)&e!"), Main.color("&6" + (prevlevel) + " &f-> &6" + newlevel), 5, 40, 5));
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                profs.get(index).setLevel(Math.min(lvl + profs.get(index).getLevel(), Constants.maxLevelProf));
                pushFiles();
                return;
            }
            index++;
        }
    }

    public void levelup(Player p) {
        for (Profession pr :profs) {
            pr.levelup(p);
        }
        pushFiles();
    }

    public void setLevel(Player p, int lvl, String profstr) {
        int index = 0;
        for (Profession prof : profs) {
            if (prof.getName().equalsIgnoreCase(profstr)) {
                Main.msg(p, "");
                Main.msg(p, "&7» &e&lLEVEL UP &8(&e" + profs.get(index).getName() + "&8)&e: &6" + (profs.get(index).getLevel()) + " &f-> &6" + lvl);
                Main.msg(p, "");
                p.sendTitle(new Title(Main.color("&e&lLEVEL UP &8(&e" + profs.get(index).getName() + "&8)&e!"), Main.color("&6" + (profs.get(index).getLevel()) + " &f-> &6" + lvl), 5, 40, 5));
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                profs.get(index).setLevel(lvl);
                pushFiles();
                return;
            }
            index++;
        }
    }

    public void pushFiles() {
        File pFile = new File("plugins/Core/data/profs/" + uuid + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            if (pFile.exists()) {
                if (profs == null) {
                    pullFiles();
                }
                for (Profession p : profs) {
                    pData.set(p.getName() + "LVL", p.getLevel());
                    pData.set(p.getName() + "EXP", p.getExp());
                    pData.save(pFile);
                }
            } else {
                if (profs == null) {
                    resetProfsList();
                }
                for (Profession p : profs) {
                    pData.set(p.getName() + "LVL", p.getLevel());
                    pData.set(p.getName() + "EXP", p.getExp());
                    pData.save(pFile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getLevel(String s) {
        for (Profession p : profs) {
            if (p.getName().equalsIgnoreCase(s)) {
                return p.getLevel();
            }
        }
        return 0;
    }

    public double getExp(String s) {
        for (Profession p : profs) {
            if (p.getName().equalsIgnoreCase(s)) {
                return p.getExp();
            }
        }
        return 0.0;
    }

}
