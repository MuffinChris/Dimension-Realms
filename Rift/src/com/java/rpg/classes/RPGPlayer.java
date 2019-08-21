package com.java.rpg.classes;

import com.java.Main;
import com.java.rpg.party.Party;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.io.File;
import com.java.rpg.Leveleable;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RPGPlayer extends Leveleable {

    private Main main = Main.getInstance();

    private int pstrength;

    private Player player;
    private PlayerClass pclass;

    private double currentMana;

    private Skillboard board;

    public void setMana(double m) {
        currentMana = Math.min(m, pclass.getCalcMana(getLevel()));
    }

    public void setManaOverflow(double m) {
        currentMana = m;
    }

    public String getPrettyCMana() {
        DecimalFormat df = new DecimalFormat("#");
        return df.format(currentMana);
    }

    Map<String, Integer> skillLevels;
    List<String> statuses;
    List<String> passives;
    List<String> toggles;
    List<Map<Integer, String>> toggleTasks;
    List<Map<Integer, String>> passiveTasks;
    private Map<String, Long> cooldowns;

    private LivingEntity target;

    public RPGPlayer(Player p) {
        super (0, 0);
        player = p;
        currentMana = 0;
        statuses = new ArrayList<>();
        passives = new ArrayList<>();
        cooldowns = new HashMap<>();
        passiveTasks = new ArrayList<>();
        toggleTasks = new ArrayList<>();
        toggles = new ArrayList<>();
        skillLevels = new HashMap<>();
        target = null;
        pullFiles();
        board = new Skillboard(p);
        pstrength = 100;
    }

    public Map<String, Integer> getSkillLevels() {
        return skillLevels;
    }

    public void setPStrength(int d) {
        pstrength = d;
    }

    public int getPStrength() {
        return pstrength;
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
            pData.set("Username", player.getName());
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
            String output = "";
            for (String s : skillLevels.keySet()) {
                output+=s + "-" + skillLevels.get(s) + ",";
            }
            if (output.contains(",")) {
                output = output.substring(0, output.length() - 1);
            }
            boolean none = false;
            if (pData.contains(name + "Skills") && pData.get(name + "Skills").equals("") && !pclass.getName().equals("None")) {
                none = true;
            }
            if (pData.contains(name + "Skills") && !none) {
                pData.set(name + "Skills", output);
            } else {
                output = "";
                for (Skill s : pclass.getSkills()) {
                    output+=s.getName() + "-0,";
                }
                if (output.contains(",")) {
                    output = output.substring(0, output.length() - 1);
                }
                pData.set(name + "Skills", output);
            }
            String cd = "";
            for (String skill : cooldowns.keySet()) {
                cd+=skill + ":" + cooldowns.get(skill) + ",";
            }
            if (cd.contains(",")) {
                cd = cd.substring(0, cd.length() - 1);
            }
            pData.set("Cooldowns", cd);
            pData.save(pFile);
            updateStats();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pullFiles() {
        File pFile = new File("plugins/Rift/data/classes/" + player.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        if (pFile.exists()) {
            pData.set("Username", player.getName());
            String name = "None";
            pclass = main.getCM().getPClassFromString(pData.getString("Current Class"));
            if (pclass instanceof PlayerClass) {
                name = pclass.getName();
            }
            setLevel(pData.getInt(name + "Level"));
            setExp(pData.getDouble(name + "Exp"));
            currentMana = (pData.getInt(name + "CMana"));
            if(pData.contains(name + "Skills")) {
                if (pData.get(name + "Skills").equals("") && !pclass.getName().equals("None")) {
                    pushFiles();
                }
                String skstr = pData.getString(name + "Skills");
                if (skstr.length() > 0) {
                    String[] sks = skstr.split(",");
                    for (String s : sks) {
                        String[] skobj = s.split("-");
                        skillLevels.put(skobj[0], Integer.valueOf(skobj[1]));
                    }
                }
            } else {
                pushFiles();
            }
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

    public void noneClass() {
        cooldowns.clear();
        double hp = player.getHealth() / player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        pushFiles();
        pclass = main.getCM().getPClassFromString("None");
        File pFile = new File("plugins/Rift/data/classes/" + player.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            pData.set("Current Class", "None");
            pData.save(pFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        pullFiles();
        currentMana = 0;
        player.setHealth(hp * RPGConstants.defaultHP);
        updateStats();
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
            double hpprev = player.getHealth() / player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
            double hp = getPClass().getCalcHP(getLevel());
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
            player.setHealth(Math.min(hp, hp * hpprev));
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(pclass.getBaseDmg());
        }
    }

    public ArrayList<LivingEntity> getNearbyTargets(Player p, int range) {
        ArrayList<LivingEntity> list = new ArrayList<>();
        for (LivingEntity ent : p.getLocation().getNearbyLivingEntities(range)) {
            if (ent instanceof ArmorStand) {
                continue;
            }
            if (ent instanceof Player) {
                Player pl = (Player) ent;
                if (main.getPM().getParty(pl) instanceof Party && !main.getPM().getParty(pl).getPvp()) {
                    if (main.getPM().getParty(pl).getPlayers().contains(p)) {
                        continue;
                    }
                }
                if (pl.equals(p)) {
                    continue;
                }
            }
            list.add(ent);
        }
        return list;
    }

    public LivingEntity getNearestEntityInSight(Player player, int range) {
        ArrayList<LivingEntity> entities = getNearbyTargets(player, range);
            for (LivingEntity ent : entities) {
                Vector toEntity = ent.getLocation().toVector().subtract(player.getEyeLocation().toVector());
                Vector toEntity2 = ent.getEyeLocation().toVector().subtract(player.getEyeLocation().toVector());
                Vector direction = player.getEyeLocation().getDirection();
                double dot = toEntity.normalize().dot(direction);
                double dot2 = toEntity2.normalize().dot(direction);
                if (dot > 0.98 || dot2 > 0.98) {
                    if (player.hasLineOfSight(ent)) {
                        return ent;
                    }
                }
            }
        //}
        return null;
    }

    public String castSkill(String name) {
        if (pclass instanceof PlayerClass) {
            for (Skill s : pclass.getSkills()) {
                if (name.equalsIgnoreCase(s.getName())) {
                    if (s.getLevel() <= getLevel()) {
                        if (s.getManaCost() <= currentMana || (s.getType().contains("TOGGLE") && getToggles().contains(s.getName()))) {
                            String cd = getCooldown(s);
                            final BukkitScheduler scheduler = Bukkit.getScheduler();
                            if (cd.equalsIgnoreCase("Warmup")) {
                                if (!statuses.contains("Warmup" + s.getName())) {
                                    getStatuses().add("Warmup" + s.getName());
                                } else {
                                    return "AlreadyCasting";
                                }

                                if (s.getType().contains("TARGET")) {
                                    if (getNearestEntityInSight(player, 32) instanceof LivingEntity) {
                                        target = getNearestEntityInSight(player, 32);
                                        if (target.getLocation().distance(player.getLocation()) > s.getTargetRange()) {
                                            target = null;
                                            getStatuses().remove("Warmup" + s.getName());
                                            return "OutOfRangeTarget";
                                        }
                                        s.targetParticles(player, target);
                                    } else {
                                        getStatuses().remove("Warmup" + s.getName());
                                        target = null;
                                        return "BadTarget";
                                    }
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
                                        if (currentMana >= s.getManaCost()) {
                                            currentMana -= s.getManaCost();
                                            if (s.getType().contains("TARGET")) {
                                                s.target(player, target);
                                            } else {
                                                s.cast(player);
                                            }
                                        } else {
                                            Main.msg(player, "&cOut of mana to cast " + s.getName() + "!");
                                        }
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
                            if (cd.equalsIgnoreCase("Invalid")) {
                                return "Failure";
                            }
                            if (cd.contains("CD:")) {
                                return cd;
                            }
                            if (s.getType().contains("TOGGLE")) {
                                if (getToggles().contains(s.getName())) {
                                    s.toggleEnd(player);
                                } else {
                                    getToggles().add(s.getName());
                                    Map<Integer, String> map = new HashMap<>();
                                    map.put(s.toggleInit(player), s.getName());
                                    getToggleTasks().add(map);
                                    currentMana -= s.getManaCost();
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
                                }
                                return "CastedSkill";
                            }
                            if (s.getType().contains("TARGET")) {
                                if (getNearestEntityInSight(player, 32) instanceof LivingEntity) {
                                    target = getNearestEntityInSight(player, 32);
                                    if (target.getLocation().distance(player.getLocation()) > s.getTargetRange()) {
                                        target = null;
                                        getStatuses().remove("Warmup" + s.getName());
                                        return "OutOfRangeTarget";
                                    }
                                    s.targetParticles(player, target);
                                } else {
                                    getStatuses().remove("Warmup" + s.getName());
                                    target = null;
                                    return "BadTarget";
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
                                if (cooldowns.containsKey(s.getName())) {
                                    cooldowns.replace(s.getName(), System.currentTimeMillis());
                                } else {
                                    cooldowns.put(s.getName(), System.currentTimeMillis());
                                }
                                currentMana -= s.getManaCost();
                                s.target(player, target);
                                if (indexesToRemove != null && indexesToRemove.size() > 0) {
                                    return "Interrupted";
                                }
                                return "CastedSkill";
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

                                if (!s.getType().contains("CAST")) {
                                    return "CannotCast";
                                }

                                return "CastedSkill";
                                //return s.getFlavor();
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

    public List<String> getToggles() {
        return toggles;
    }

    public List<Map<Integer, String>> getToggleTasks() {
        return toggleTasks;
    }

    public List<String> getPassives() {
        return passives;
    }

    public List<Map<Integer, String>> getPassiveTasks() {
        return passiveTasks;
    }

    public void scrub() {
        if (main.getCM().getFall().contains(player.getUniqueId())) {
            main.getCM().getFall().remove(player.getUniqueId());
        }
        if (main.getCM().getFallMap().containsKey(player.getUniqueId())) {
            Bukkit.getScheduler().cancelTask(main.getCM().getFallMap().get(player.getUniqueId()));
            main.getCM().getFallMap().remove(player.getUniqueId());
        }
        BukkitScheduler scheduler = Bukkit.getScheduler();
        List<String> skillsToRemove = new ArrayList<>();
        for (String s : getToggles()) {
            skillsToRemove.add(s);
        }
        for (String s : skillsToRemove) {
            getSkillFromName(s).toggleEnd(player);
        }
        skillsToRemove = new ArrayList<>();

        for (String s : getPassives()) {
            skillsToRemove.add(s);
        }
        for (String s : skillsToRemove) {
            getPassives().remove(s);
            List<Map<Integer, String>> tasksToRemove = new ArrayList<>();
            for (Map<Integer, String> map : getPassiveTasks()) {
                if (map.containsValue(s)) {
                    tasksToRemove.add(map);
                }
            }

            for (Map<Integer, String> map : tasksToRemove) {
                scheduler.cancelTask((int) map.keySet().toArray()[0]);
                getPassiveTasks().remove(map);
            }
            tasksToRemove = new ArrayList<>();
        }
        skillsToRemove = new ArrayList<>();
        board = null;
        cooldowns = new HashMap<>();
        statuses = new ArrayList<>();
        passives = new ArrayList<>();
        passiveTasks = new ArrayList<>();
        toggleTasks = new ArrayList<>();
        toggles = new ArrayList<>();
        player = null;
        pclass = null;
    }
}
