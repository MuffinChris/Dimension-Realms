package com.core.java.rpgbase.player;

import com.core.java.essentials.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Party {

    private Player leader;
    private List<Player> players;
    private boolean pvp;
    private boolean share;

    public Party(Player p) {
        leader = p;
        players = new ArrayList<Player>();
        pvp = false;
        share = true;
    }

    public void addPlayer(Player p) {
        players.add(p);
    }

    public void removePlayer(Player p) {
        players.remove(p);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPvp(boolean p) {
        pvp = p;
    }

    public void setShare(boolean p) {
        share = p;
    }

    public boolean getPvp() {
        return pvp;
    }

    public boolean getShare() {
        return share;
    }

    public void disband() {
        sendMessage(leader, "&e&l" + leader.getName() + " &fhas disbanded the party!");
        leader = null;
        players = null;
    }

    public void sendMessage(Player pl, String s) {
        for (Player p : players) {
            if (!pl.getName().equals(p.getName())) {
                p.sendMessage(Main.color(s));
            }
        }
    }

    public Player getLeader() {
        return leader;
    }

}
