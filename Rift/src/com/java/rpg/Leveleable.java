package com.java.rpg;

import com.destroystokyo.paper.Title;
import com.java.Main;
import com.java.holograms.Hologram;
import com.java.rpg.classes.PlayerClass;
import com.java.rpg.classes.RPGConstants;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.text.DecimalFormat;

public class Leveleable {

    private int level;
    private int maxlevel;
    private double exp;
    private double maxexp;

    // Equation
    private double expMod;
    private double expOff;
    private double expPow;

    private Main main = Main.getInstance();

    public Leveleable(int level, int maxlevel) {
        this.level = level;
        this.maxlevel = maxlevel;
        expMod = RPGConstants.expMod;
        expOff = RPGConstants.expOff;
        expPow = RPGConstants.expPow;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxLevel() {
        return maxlevel;
    }

    public double getExp() {
        return exp;
    }

    public double getMaxExp() {
        return maxexp;
    }

    public void calcMaxExp() {
        maxexp = Math.pow(level, expPow) * expMod + expOff;
    }

    public double getPercent() {
        return exp / maxexp;
    }

    public String getPrettyPercent() {
        DecimalFormat dF = new DecimalFormat("#.##");
        return dF.format((100.0D * exp) / maxexp);
    }

    public String getPrettyExp() {
        DecimalFormat df = new DecimalFormat("#");
        return df.format(exp);
    }

    public String getPrettyMaxExp() {
        DecimalFormat df = new DecimalFormat("#");
        return df.format(maxexp);
    }

    public boolean levelup() {
        if (exp >= maxexp && level < maxlevel) {
            level++;
            exp = exp - maxexp;
            calcMaxExp();
            return true;
        }
        return false;
    }

    public void levelupRewards(Player p, PlayerClass playerclass) {
        Main.msg(p, "");
        Main.msg(p, "&e&lLEVEL UP &7- &6" + playerclass.getName() + " &7(&f" + (level - 1) + " &7-> &f" + level + "&7)");
        Main.msg(p, "");
        Main.msg(p, "&e&lSTAT INCREASES:");
        Main.msg(p, "&8» &f+" + playerclass.getHpPerLevel() + " &cHP");
        Main.msg(p, "&8» &f+" + playerclass.getManaPerLevel() + " &bM &8| " + "&f+" + playerclass.getManaRegenPerLevel() + " &bM/s");
        Main.msg(p, "&8» &f+" + playerclass.getArmorPerLevel() + " &cA &8| " + "&f+" + playerclass.getMagicResistPerLevel() + " &bMR");
        if (level % 2 == 0) {
            Main.msg(p, "&8» &f+1 &bSKILL POINT");
        }
        Main.msg(p, "");
        p.sendTitle(new Title(Main.color("&e&lLEVEL UP!"), Main.color("&6" + (level - 1) + " &f-> &6" + level), 5, 40, 5));
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
        main.getRP(p).pushFiles();
    }

    public void setLevel(int l) {
        level = l;
        calcMaxExp();
    }

    public void setExp(double e) {
        exp = e;
        levelup();
    }

    public void giveExpFromSource(Player p, Location t, double xp) {
        exp+=xp;
        DecimalFormat dF = new DecimalFormat("#");
        Main.msg(p, "   &7[+" + dF.format(xp) + "&7 XP]");
        Hologram magic = new Hologram(p, t, "&7[+" + dF.format(xp) + " XP]", Hologram.HologramType.DAMAGE);
        magic.rise();
        if (levelup()) {
            levelupRewards(p, Main.getInstance().getRP(p).getPClass());
        }
    }

}
