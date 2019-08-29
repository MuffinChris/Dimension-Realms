package com.java.rpg.classes;

public class StatusValue {

    private int value;
    private String source;
    private int timestamp;
    private int duration;
    private boolean durationless;

    public StatusValue(String source, int value, int duration, int timestamp, boolean durationless) {
        this.source = source;
        this.value = value;
        this.duration = duration;
        this.timestamp = timestamp;
        this.durationless = durationless;
    }

    public boolean getDurationless() {
        return durationless;
    }

    public int getValue() {
        return value;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getSource() {
        return source;
    }

    public int getDuration() {
        return duration;
    }

    public void scrub() {
        source = null;
    }

}
