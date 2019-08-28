package com.java.rpg.classes;

import java.util.Map;

public class StatusObject {

    private String name;
    private int duration; // This is ticked down by 1 every tick, and removed if duration becomes 0
    private int value; // This represents value. Stunned might have 0 value. Power strength increase might have 10 value for bonus.

    public StatusObject(String name, int duration, int value) {
        this.name = name;
        this.duration = duration;
        this.value = value;
    }

}
