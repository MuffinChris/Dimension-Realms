package com.java.rpg.classes;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.java.Main;
import com.java.rpg.classes.skills.Fireball;
import com.java.rpg.classes.skills.MeteorShower;
import com.java.rpg.classes.skills.WorldOnFire;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassManager implements Listener {

    private Main main = Main.getInstance();

    public ClassManager() {
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
            main.getPC().remove(e.getPlayer().getUniqueId());
        }
    }

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
    }

    public static Map<String, PlayerClass> classes = new HashMap<>();

    public static void createClasses() {
        List<Skill> skills = new ArrayList<>();
        skills.add(new Fireball());
        skills.add(new WorldOnFire());
        skills.add(new MeteorShower());
        classes.put("Pyromancer", new PlayerClass("Pyromancer", "&6Pyromancer", 800.0, 5.0, 400, 5, 5, 0.1, "HOE", 10, 20, 22, 0.2, 0.1, skills));
    }

    public PlayerClass getPClassFromString(String s) {
        for (String cl : classes.keySet()) {
            if (cl.equalsIgnoreCase(s)) {
                return classes.get(cl);
            }
        }
        return null;
    }

}
