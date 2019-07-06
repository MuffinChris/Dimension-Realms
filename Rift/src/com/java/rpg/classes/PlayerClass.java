package com.java.rpg.classes;

import com.java.Main;

import java.util.List;

public class PlayerClass {

    private String name;
    private String fancyname;

    private double basehp;
    private double hpPerLevel;

    private double mana;
    private double manaPerLevel;

    private double manaRegen;
    private double manaRegenPerLevel;

    private String weapon;
    private double baseDmg;

    private String resourceName = "Mana";
    private String fancyResourceName = "&bMana";

    private List<Skill> skills;

    public PlayerClass(String name, String fancyname, double basehp, double hpPerLevel, double mana, double manaPerlevel, double manaRegen, double manaRegenPerLevel, String weapon, double baseDmg, List<Skill> skills) {
        this.name = name;
        this.fancyname = fancyname;
        this.basehp = basehp;
        this.hpPerLevel = hpPerLevel;
        this.mana = mana;
        this.manaPerLevel = manaPerLevel;
        this.manaRegen = manaRegen;
        this.manaRegenPerLevel = manaRegenPerLevel;
        this.skills = skills;
        this.weapon = weapon;
        this.baseDmg = baseDmg;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public String getName() {
        return name;
    }

    public String getFancyName() {
        return Main.color(fancyname);
    }

    public double getBaseHP() {
        return basehp;
    }

    public double getHpPerLevel() {
        return hpPerLevel;
    }

    public double getMana() {
        return mana;
    }

    public double getManaPerLevel() {
        return manaPerLevel;
    }

    public double getManaRegen() {
        return manaRegen;
    }

    public double getManaRegenPerLevel() {
        return manaRegenPerLevel;
    }

    public double getBaseDmg() {
        return baseDmg;
    }

    public String getWeapon() {
        return weapon;
    }

    public double getCalcHP(int level) {
        return hpPerLevel * level + basehp;
    }

    public double getCalcMana(int level) {
        return manaPerLevel * level + mana;
    }

    public double getCalcManaRegen(int level) {
        return manaRegenPerLevel * level + manaRegen;
    }

}
