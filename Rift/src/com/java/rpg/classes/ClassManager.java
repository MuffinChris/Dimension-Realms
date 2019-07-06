package com.java.rpg.classes;

import com.java.Main;
import com.java.rpg.classes.skills.Fireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.java.rpg.classes.skills.Flameburst;
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
    public void onJoin (PlayerJoinEvent e) {
        main.getPC().put(e.getPlayer(), new RPGPlayer(e.getPlayer()));
    }

    @EventHandler
    public void onLeave (PlayerQuitEvent e) {
        if (main.getPC().containsKey(e.getPlayer())) {
            main.getPC().get(e.getPlayer()).pushFiles();
            main.getPC().get(e.getPlayer()).scrub();
            main.getPC().remove(e.getPlayer());
        }
    }

    public static Map<String, PlayerClass> classes = new HashMap<>();

    public static void createClasses() {
        List<Skill> skills = new ArrayList<>();
        skills.add(new Fireball());
        skills.add(new Flameburst());
        classes.put("Pyromancer", new PlayerClass("Pyromancer", "&6Pyromancer", 800.0, 5.0, 400, 10, 10, 0.3, "HOE", 10, skills));
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
