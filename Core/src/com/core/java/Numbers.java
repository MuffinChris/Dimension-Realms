package com.core.java;

public class Numbers {

    public double getMobProjectileDmg(int level, double dmg) {
        return (220 + 0.00025 * dmg * Math.pow(level+35, 3));
    }

    public double getMobHp(int level) {
        return (60 + 0.001 * Math.pow((level + 15), 3));
    }

    public double getMobDmg(int level, double ad) {
        return 200 + 0.0003 * Math.pow(level+40,3) * ad;
    }

}
