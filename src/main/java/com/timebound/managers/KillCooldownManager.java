package com.timebound.managers;

import com.timebound.TimeBoundPlugin;
import java.util.*;

public class KillCooldownManager {
    private final TimeBoundPlugin plugin;
    private final Map<UUID, Map<UUID, Long>> cooldowns = new HashMap<>();

    public KillCooldownManager(TimeBoundPlugin plugin) { this.plugin = plugin; }

    public boolean isOnCooldown(UUID killer, UUID victim) {
        Map<UUID, Long> map = cooldowns.get(killer);
        if (map == null) return false;
        Long time = map.get(victim);
        if (time == null) return false;
        long cooldownMs = plugin.getConfig().getLong("kill-cooldown", 120) * 60 * 1000;
        return (System.currentTimeMillis() - time) < cooldownMs;
    }

    public long getRemainingMinutes(UUID killer, UUID victim) {
        Map<UUID, Long> map = cooldowns.get(killer);
        if (map == null) return 0;
        Long time = map.get(victim);
        if (time == null) return 0;
        long cooldownMs = plugin.getConfig().getLong("kill-cooldown", 120) * 60 * 1000;
        long remaining = cooldownMs - (System.currentTimeMillis() - time);
        return Math.max(0, remaining / 60000);
    }

    public void setCooldown(UUID killer, UUID victim) {
        cooldowns.computeIfAbsent(killer, k -> new HashMap<>()).put(victim, System.currentTimeMillis());
    }
}
