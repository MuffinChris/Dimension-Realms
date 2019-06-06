package com.core.java.rpgbase.player;

import com.core.java.essentials.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissionAttachment;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Permissions implements CommandExecutor, Listener {

    private Main main = Main.getInstance();

    public Map<UUID, PermissionAttachment> perms;
    public List<String> memberperms;
    public List<String> vipperms;
    public List<String> vipplusperms;
    public List<String> helperperms;
    public List<String> builderperms;
    public List<String> modperms;
    public List<String> adminperms;
    public List<String> ownerperms;

    public void setupPerms() {
        perms = new HashMap<>();
        vipperms = new ArrayList<String>();
        vipperms.add("core.chatcolor");
    }

    public void updatePerms() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
        } else {

        }
        return false;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        File pFile = new File("plugins/Core/data/" + e.getPlayer().getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            if (!pData.isSet("Group")) {
                pData.set("Group", "Member");
                PermissionAttachment at = e.getPlayer().addAttachment(main);
                perms.put(e.getPlayer().getUniqueId(), at);
                pData.save(pFile);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (perms.containsKey(e.getPlayer().getUniqueId())) {
            perms.remove(e.getPlayer().getUniqueId());
        }
    }

}
