package com.core.java;

import com.core.java.rpgbase.player.professions.Profession;

import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static int SPPerLevel = 5;
    public static int ManaPerLevel = 50;
    public static int ADPerLevel = 5;
    public static int HPPerLevel = 100;
    public static int maxLevel = 100;

    public static int HealthScale = 20;

    public static double BaseHP = 2000;
    public static int BaseMana = 5000;
    public static int BaseManaRegen = 20;

    public static double ADPerSP = 0.05;
    public static double HPPerSP = 0.05;
    public static double ManaPerSP = 0.1;
    public static int ManaRegenPerSP = 5;

    public static double FreeAttackDamage = 75.0;

    public static double PartyXPMult = 1.75;

    public static int maxLevelProf = 50;

    public static List<Profession> baseProfs = makeProfList();

    public static List<Profession> makeProfList() {
        List<Profession> list = new ArrayList<Profession>();
        list.add(new Profession("Mining", maxLevelProf, 0, 0, "&7Mine ores and traverse caverns."));
        list.add(new Profession("Lumber", maxLevelProf, 0, 0, "&7Chop down trees."));
        list.add(new Profession("Farming", maxLevelProf, 0, 0, "&7Plant and harvest crops."));
        list.add(new Profession("Fishing", maxLevelProf, 0, 0, "&7Reel in fish."));
        list.add(new Profession("Enchanting", maxLevelProf, 0, 0, "&7Enchant items."));
        return list;
    }

}
