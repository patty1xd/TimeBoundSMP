package com.timebound.models;

public enum TimeTier {
    NONE(0), T1(240), T2(300), T3(360), T4(420), T5(480), T6(600), T7(660);
    private final int minMinutes;
    TimeTier(int minMinutes) { this.minMinutes = minMinutes; }
    public int getMinMinutes() { return minMinutes; }
    public static TimeTier fromMinutes(long minutes) {
        TimeTier current = NONE;
        for (TimeTier tier : values()) {
            if (minutes >= tier.minMinutes) current = tier;
        }
        return current;
    }
}
