package com.java.rpg;

import com.java.rpg.classes.RPGConstants;

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

    public void setLevel(int l) {
        level = l;
        calcMaxExp();
    }

    public void setExp(double e) {
        exp = e;
        levelup();
    }

}
