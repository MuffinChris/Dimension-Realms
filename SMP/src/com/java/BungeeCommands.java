package com.java;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.output.ByteArrayOutputStream;
import org.bukkit.entity.Player;

import java.io.DataOutputStream;
import java.io.IOException;

public class BungeeCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Bukkit.getMessenger().registerOutgoingPluginChannel(Main.getInstance(), "BungeeCord");

            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);

            try {
                out.writeUTF("Connect");
                out.writeUTF("hub");
            } catch (IOException ex) {

            }
            p.sendPluginMessage(Main.getInstance(), "BungeeCord", b.toByteArray());
        }
        return false;
    }

}
