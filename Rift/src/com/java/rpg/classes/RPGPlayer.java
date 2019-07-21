package com.java.rpg.classes;

import com.java.Main;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import com.java.rpg.Leveleable;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RPGPlayer extends Leveleable {

    private Main main = Main.getInstance();

    private Player player;
    private PlayerClass pclass;

    private double currentMana;

    private Skillboard board;

    public void setMana(double m) {
        currentMana = m;
    }

    public String getPrettyCMana() {
        DecimalFormat df = new DecimalFormat("#");
        return df.format(currentMana);
    }

    List<String> statuses;
    private Map<String, Long> cooldowns;

    public RPGPlayer(Player p) {
        super (0, 0);
        player = p;
        currentMana = 0;
        statuses = new ArrayList<>();
        cooldowns = new HashMap<>();
        pullFiles();
        board = new Skillboard(p);
    }

    public Skillboard getBoard() {
        return board;
    }

    public Map<String, Long> getCooldowns() {
        return cooldowns;
    }

    public PlayerClass getPClass() {
        return pclass;
    }

    public Player getPlayer() {
        return player;
    }

    public double getCMana() {
        return currentMana;
    }

    public void pushFiles() {
        File pFile = new File("plugins/Rift/data/classes/" + player.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            String name = "None";
            if (pclass instanceof PlayerClass) {
                name = pclass.getName();
                pData.set("Current Class", name);
            } else {
                pData.set("Current Class", "None");
            }
            pData.set(name + "Level", getLevel());
            pData.set(name + "Exp", getExp());
            pData.set(name + "CMana", currentMana);
            String cd = "";
            for (String skill : cooldowns.keySet()) {
                cd+=skill + ":" + cooldowns.get(skill) + ",";
            }
            if (cd.contains(",")) {
                cd = cd.substring(0, cd.length() - 1);
            }
            pData.set("Cooldowns", cd);
            pData.save(pFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pullFiles() {
        File pFile = new File("plugins/Rift/data/classes/" + player.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        if (pFile.exists()) {
            String name = "None";
            pclass = main.getCM().getPClassFromString(pData.getString("Current Class"));
            if (pclass instanceof PlayerClass) {
                name = pclass.getName();
            }
            setLevel(pData.getInt(name + "Level"));
            setExp(pData.getDouble(name + "Exp"));
            currentMana = (pData.getInt(name + "CMana"));
            if(pData.contains("Cooldowns")) {
                String cdstr = pData.getString("Cooldowns");
                if (cdstr.length() > 0) {
                    String[] cds = cdstr.split(",");
                    for (String s : cds) {
                        String[] cdobj = s.split(":");
                        cooldowns.put(cdobj[0], Long.valueOf(cdobj[1]));
                    }
                }
            }
            updateStats();
        } else {
            pushFiles();
            pullFiles();
        }
    }

    public boolean changeClass(PlayerClass pc) {
        double hp = player.getHealth() / player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        pushFiles();
        if (pclass instanceof PlayerClass && pc.getName().equalsIgnoreCase(pclass.getName())) {
            return false;
        }
        pclass = pc;
        File pFile = new File("plugins/Rift/data/classes/" + player.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            if (pclass instanceof PlayerClass) {
                pData.set("Current Class", pclass.getName());
            } else {
                pData.set("Current Class", "None");
            }
            pData.save(pFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        pullFiles();
        currentMana = 0;
        player.setHealth(hp * pclass.getCalcHP(getLevel()));
        updateStats();
        return true;
    }

    public void updateStats() {
        player.setHealthScale(20);
        if (pclass instanceof PlayerClass) {
            double hp = getPClass().getCalcHP(getLevel());
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
            player.setHealth(Math.min(player.getHealth(), hp));
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(pclass.getBaseDmg());
        } else {
            double hp = RPGConstants.defaultHP;
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
            player.setHealth(Math.min(player.getHealth(), hp));
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(RPGConstants.baseDmg);
        }
    }

    public String castSkill(String name) {
        if (pclass instanceof PlayerClass) {
            for (Skill s : pclass.getSkills()) {
                if (name.equalsIgnoreCase(s.getName())) {
                    if (s.getLevel() <= getLevel()) {
                        if (s.getManaCost() <= currentMana) {
                            String cd = getCooldown(s);
                            final BukkitScheduler scheduler = Bukkit.getScheduler();
                            if (cd.equalsIgnoreCase("Warmup")) {
                                if (!statuses.contains("Warmup" + s.getName())) {
                                    getStatuses().add("Warmup" + s.getName());
                                } else {
                                    return "AlreadyCasting";
                                }
                                List<Integer> indexesToRemove = new ArrayList<>();
                                int index = 0;
                                for (String status : statuses) {
                                    if (status.contains("Warmup")) {
                                        if (!status.contains(s.getName())) {
                                            indexesToRemove.add(index);
                                        }
                                        index++;
                                    }
                                }
                                for (int ind : indexesToRemove) {
                                    statuses.remove(ind);
                                }

                                final int task = scheduler.scheduleSyncDelayedTask(main, new Runnable() {
                                    public void run() {
                                        if (cooldowns.containsKey(s.getName())) {
                                            cooldowns.replace(s.getName(), System.currentTimeMillis());
                                        } else {
                                            cooldowns.put(s.getName(), System.currentTimeMillis());
                                        }
                                        currentMana -= s.getManaCost();
                                        s.cast(player);
                                        statuses.remove("Warmup" + s.getName());
                                    }
                                }, s.getWarmup());

                                scheduler.scheduleSyncRepeatingTask(main, new Runnable(){
                                    public void run() {
                                        if (!getStatuses().contains("Warmup" + s.getName())) {
                                            scheduler.cancelTask(task);
                                        }
                                    }
                                }, 0, 1);
                                return "Warmup:" + s.getWarmup();
                            }
                            if (cd.equalsIgnoreCase("Casted")) {
                                List<Integer> indexesToRemove = new ArrayList<>();
                                int index = 0;
                                for (String status : statuses) {
                                    if (status.contains("Warmup")) {
                                        if (!status.contains(s.getName())) {
                                            indexesToRemove.add(index);
                                        }
                                        index++;
                                    }
                                }
                                for (int ind : indexesToRemove) {
                                    statuses.remove(ind);
                                }
                                if (cooldowns.containsKey(s.getName())) {
                                    cooldowns.replace(s.getName(), System.currentTimeMillis());
                                } else {
                                    cooldowns.put(s.getName(), System.currentTimeMillis());
                                }
                                currentMana -= s.getManaCost();
                                s.cast(player);
                                if (indexesToRemove != null && indexesToRemove.size() > 0) {
                                    return "Interrupted";
                                }
                                return s.getFlavor();
                            }
                            if (cd.equalsIgnoreCase("Invalid")) {
                                return "Failure";
                            }
                            if (cd.contains("CD:")) {
                                return cd;
                            }
                        }
                        return "NoMana";
                    }
                    return "Level" + s.getLevel();
                }
            }
        }
        return "Invalid";
    }

    public String getCooldown(Skill s) {
        if (pclass instanceof PlayerClass) {
            for (Skill sk : pclass.getSkills()) {
                if (sk.equals(s)) {
                    if (cooldowns.containsKey(s.getName())) {
                        long timeLeft = System.currentTimeMillis() - cooldowns.get(s.getName());
                        if (TimeUnit.MILLISECONDS.toSeconds(timeLeft) * 20 >= s.getCooldown()) {
                            if (s.getWarmup() != 0) {
                                return "Warmup";
                            }
                            return "Casted";
                        } else {
                            DecimalFormat dF = new DecimalFormat("#.#");
                            String cd = dF.format((s.getCooldown() / 20.0) - (timeLeft/1000.0));
                            if (!cd.contains(".")) {
                                cd+=".0";
                            }
                            return "CD:" + cd;
                        }
                    } else {
                        if (s.getWarmup() != 0) {
                            return "Warmup";
                        }
                        return "Casted";
                    }
                }
            }
        }
        return "Invalid";
    }

    public Skill getSkillFromName(String name) {
        if (pclass instanceof PlayerClass) {
            for (Skill s : pclass.getSkills()) {
                if (name.equalsIgnoreCase(s.getName())) {
                    return s;
                }
            }
        }
        return null;
    }

    public void refreshCooldowns() {
        if (pclass instanceof PlayerClass) {
            for (int i = cooldowns.keySet().size() - 1; i >= 0; i--) {
                String name = cooldowns.keySet().toArray()[i].toString();
                long timeLeft = System.currentTimeMillis() - cooldowns.get(name);
                if (getSkillFromName(name) instanceof Skill) {
                    if (TimeUnit.MILLISECONDS.toSeconds(timeLeft) * 20 >= getSkillFromName(name).getCooldown()) {
                        cooldowns.remove(name);
                    }
                }
            }
        }
    }

    public List<String> getStatuses() {
        return statuses;
    }

    public void scrub() {
        board = null;
        cooldowns = new HashMap<>();
        statuses = new ArrayList<>();
        player = null;
        pclass = null;
    }
}
