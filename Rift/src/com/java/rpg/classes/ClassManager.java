package com.java.rpg.classes;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.java.Main;
import com.java.rpg.classes.skills.Pyromancer.*;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public class ClassManager implements Listener {

    private Main main = Main.getInstance();

    private List<UUID> fall;

    private Map<UUID, Integer> fallMap;

    public List<UUID> getFall() {
        return fall;
    }

    public Map<UUID, Integer> getFallMap() {
        return fallMap;
    }

    public ClassManager() {
        fall = new ArrayList<>();
        fallMap = new HashMap<>();
        createClasses();
    }

    @EventHandler
    public void onClick (PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (main.getPC().get(e.getPlayer().getUniqueId()) != null) {
                if (main.getPC().get(e.getPlayer().getUniqueId()).getStatuses() != null) {
                    List<String> statuses = main.getPC().get(e.getPlayer().getUniqueId()).getStatuses();
                    List<Integer> indexesToRemove = new ArrayList<>();
                    int index = 0;
                    for (String status : statuses) {
                        if (status.contains("Warmup")) {
                            indexesToRemove.add(index);
                            index++;
                        }
                    }
                    for (int ind : indexesToRemove) {
                        statuses.remove(ind);
                    }
                }
            }
        }
    }

    public static boolean isArmor(String s) {
        return (s.contains("HELMET") || s.contains("CHESTPLATE") || s.contains("LEGGINGS") || s.contains("BOOTS") || s.contains("ELYTRA"));
    }

    public static ItemStack fixItem(ItemStack i) {
        if (i != null && i.getType() != null && isArmor(i.getType().toString())) {
            net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(i);
            if (nmsStack.getTag() == null || !(nmsStack.getTag().getList("AttributeModifiers", 0) instanceof NBTTagList)) {
                NBTTagCompound itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
                NBTTagList modifiers = new NBTTagList();
                NBTTagCompound itemC = new NBTTagCompound();

                itemC.set("AttributeName", new NBTTagString("generic.armor"));
                itemC.set("Name", new NBTTagString("generic.armor"));
                itemC.set("Amount", new NBTTagDouble(0));
                itemC.set("Operation", new NBTTagInt(0));
                itemC.set("UUIDLeast", new NBTTagInt(894654));
                itemC.set("UUIDMost", new NBTTagInt(2872));

                String item = i.toString().toLowerCase();


                if (item.contains("helmet") || item.contains("cap")) {
                    itemC.set("Slot", new NBTTagString("head"));
                }
                if (item.contains("chestplate") || item.contains("tunic") || item.contains("elytra")) {
                    itemC.set("Slot", new NBTTagString("chest"));
                }
                if (item.contains("leggings") || item.contains("pants")) {
                    itemC.set("Slot", new NBTTagString("legs"));
                }
                if (item.contains("boots")) {
                    itemC.set("Slot", new NBTTagString("feet"));
                }

                modifiers.add(itemC);
                itemTagC.set("AttributeModifiers", modifiers);
                nmsStack.setTag(itemTagC);
                ItemStack nItem = CraftItemStack.asBukkitCopy(nmsStack);

                ItemMeta meta = nItem.getItemMeta();
                nItem.setItemMeta(meta);
                return nItem;
            }
        }
        return i;
    }

    public void updateArmor(Player p) {
        for (int i = 0; i < p.getInventory().getContents().length; i++) {
            if (p.getInventory().getItem(i) instanceof ItemStack) {
                ItemStack it = p.getInventory().getItem(i);
                if (it != null && it.getType() != null && isArmor(it.getType().toString())) {
                    p.getInventory().setItem(i, fixItem(it));
                }
            }
        }
    }

    @EventHandler
    public void newArmor (PlayerArmorChangeEvent e) {
        updateArmor(e.getPlayer());
    }

    @EventHandler
    public void onJoin (PlayerJoinEvent e) {
        main.getPC().put(e.getPlayer().getUniqueId(), new RPGPlayer(e.getPlayer()));
    }

    @EventHandler
    public void onLeave (PlayerQuitEvent e) {
        if (main.getPC().containsKey(e.getPlayer().getUniqueId())) {
            main.getPC().get(e.getPlayer().getUniqueId()).pushFiles();
            main.getPC().get(e.getPlayer().getUniqueId()).scrub();
            new BukkitRunnable() {
                public void run() {
                    main.getPC().remove(e.getPlayer().getUniqueId());
                }
            }.runTaskLater(Main.getInstance(), 10L);
        }
    }

    /*
    @EventHandler (priority = EventPriority.HIGHEST)
    public void armorMR (EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (main.getPC().get(p) instanceof RPGPlayer && main.getPC().get(p).getPClass() instanceof PlayerClass) {
                double armor = main.getPC().get(p.getUniqueId()).getPClass().getCalcArmor(main.getPC().get(p.getUniqueId()).getLevel());
                double mr = main.getPC().get(p.getUniqueId()).getPClass().getCalcMR(main.getPC().get(p.getUniqueId()).getLevel());

                if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || e.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
                    e.setDamage(e.getDamage() * 1 - (armor / RPGConstants.defenseDiv));
                }

                if (e.getCause() == EntityDamageEvent.DamageCause.CUSTOM) {
                    e.setDamage(e.getDamage() * 1 - (mr / RPGConstants.defenseDiv));
                }
            }
        }
    }*/

    public static Map<String, PlayerClass> classes = new HashMap<>();

    public static void createClasses() {
        List<Skill> skillsNone = new ArrayList<>();
        classes.put("None", new PlayerClass("None", "&eNone", RPGConstants.defaultHP, 5.0, 100, 2, 3, 0.1, "SWORD", 10, 30, 32, 0.2, 0.2, skillsNone));


        List<Skill> skillsPyro = new ArrayList<>();
        skillsPyro.add(new Fireball());
        skillsPyro.add(new WorldOnFire());
        skillsPyro.add(new InfernoVault());
        skillsPyro.add(new MeteorShower());
        skillsPyro.add(new FlameTornado());
        skillsPyro.add(new Pyroclasm());
        classes.put("Pyromancer", new PlayerClass("Pyromancer", "&6Pyromancer", 800.0, 2.0, 400, 10, 5, 0.1, "HOE", 15, 20, 22, 0.31, 0.22, skillsPyro));
    }

    public PlayerClass getPClassFromString(String s) {
        for (String cl : classes.keySet()) {
            if (cl.equalsIgnoreCase(s)) {
                return classes.get(cl);
            }
        }
        return null;
    }

    public void cleanToggle(Player p, Skill s) {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        RPGPlayer rp = main.getPC().get(p.getUniqueId());
        rp.getToggles().remove(s.getName());
        rp.getBoard().endToggle(s);
        List<Map<Integer, String>> tasksToRemove = new ArrayList<>();
        for (Map<Integer, String> map : rp.getToggleTasks()) {
            if (map.containsValue(s.getName())) {
                tasksToRemove.add(map);
            }
        }

        for (Map<Integer, String> map : tasksToRemove) {
            scheduler.cancelTask((int) map.keySet().toArray()[0]);
            rp.getToggleTasks().remove(map);
        }
        tasksToRemove = new ArrayList<>();
    }

    /*
    public void cleanToggles(Player p) {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        RPGPlayer rp = main.getPC().get(p.getUniqueId());
        List<String> skillsToRemove = new ArrayList<>();
        if (rp.getPClass() instanceof PlayerClass) {
            rp.getToggles().clear();
            List<Map<Integer, String>> tasksToRemove = new ArrayList<>();
            for (Map<Integer, String> map : rp.getToggleTasks()) {
                tasksToRemove.add(map);
            }
            for (Map<Integer, String> map : tasksToRemove) {
                scheduler.cancelTask((int) map.keySet().toArray()[0]);
                rp.getToggleTasks().remove(map);
            }
        }
        skillsToRemove = new ArrayList<>();
    }*/

    public void passives(Player p) {
        RPGPlayer rp = main.getPC().get(p.getUniqueId());
        if (rp instanceof RPGPlayer && rp.getPClass() instanceof PlayerClass) {
            List<Skill> skillsToAdd = new ArrayList<>();
            for (Skill s : rp.getPClass().getSkills()) {
                if (s.getType().contains("PASSIVE") && s.getLevel() <= rp.getLevel()) {
                    if (!rp.getPassives().contains(s.getName())) {
                        skillsToAdd.add(s);
                    }
                }
            }
            for (Skill s : skillsToAdd) {
                rp.getPassives().add(s.getName());
                int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
                    public void run() {
                        s.passive(p);
                    }
                }, 0L, s.getPassiveTicks());
                Map<Integer, String> taskSkill = new HashMap<>();
                taskSkill.put(task, s.getName());
                rp.getPassiveTasks().add(taskSkill);
            }
            cleanPassives(p);
            skillsToAdd = new ArrayList<>();
        } else {
            cleanPassives(p);
        }
    }

    public void cleanPassives(Player p) {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        RPGPlayer rp = main.getPC().get(p.getUniqueId());
        List<String> skillsToRemove = new ArrayList<>();
        for (String s : rp.getPassives()) {
            if (!(rp.getSkillFromName(s) instanceof Skill)) {
                skillsToRemove.add(s);
                continue;
            }
            if (rp.getPClass() == null || (!rp.getPClass().getSkills().contains(rp.getSkillFromName(s)) || rp.getSkillFromName(s).getLevel() > rp.getLevel())) {
                skillsToRemove.add(s);
            }
        }
        for (String s : skillsToRemove) {
            rp.getPassives().remove(s);
            List<Map<Integer, String>> tasksToRemove = new ArrayList<>();
            for (Map<Integer, String> map : rp.getPassiveTasks()) {
                if (map.containsValue(s)) {
                    tasksToRemove.add(map);
                }
            }

            for (Map<Integer, String> map : tasksToRemove) {
                scheduler.cancelTask((int) map.keySet().toArray()[0]);
                rp.getPassiveTasks().remove(map);
            }
            tasksToRemove = new ArrayList<>();
        }
        skillsToRemove = new ArrayList<>();
    }

}
