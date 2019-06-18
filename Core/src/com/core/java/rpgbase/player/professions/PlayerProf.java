package com.core.java.rpgbase.player.professions;

import com.core.java.Constants;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerProf {

    private UUID uuid;
    List<Profession> profs;

    public UUID getUUID() {
        return uuid;
    }

    public List<Profession> getProfs() {
        return profs;
    }

    public PlayerProf(Player p) {
        uuid = p.getUniqueId();
    }

    public void pullFiles() {
        File pFile = new File("plugins/Core/data/profs/" + uuid + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        if (profs != null) {
            for (Profession p : profs) {
                p.setLevel(pData.getInt(p.getName() + "LVL"));
                p.setExp(pData.getDouble(p.getName() + "EXP"));
            }
        } else {
            profs = Constants.baseProfs;
            pullFiles();
        }
    }

    public void pushFiles() {
        File pFile = new File("plugins/Core/data/profs/" + uuid + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            if (profs != null) {
                for (Profession p : profs) {
                    pData.set(p.getName() + "LVL", p.getLevel());
                    pData.set(p.getName() + "EXP", p.getExp());
                    pData.save(pFile);
                }
            } else {
                profs = Constants.baseProfs;
                pushFiles();
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
