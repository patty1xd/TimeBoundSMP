package com.timebound.managers;

import com.timebound.TimeBoundPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.*;

public class BountyManager {
    private final TimeBoundPlugin plugin;
    private final TreeMap<Integer, Integer> thresholds = new TreeMap<>();

    public BountyManager(TimeBoundPlugin plugin) {
        this.plugin = plugin;
        thresholds.put(600, 15);
        thresholds.put(900, 30);
        thresholds.put(1200, 45);
    }

    public boolean hasBounty(UUID uuid) { return plugin.getTimeManager().getTimeMinutes(uuid) >= 600; }

    public int getBountyBonus(UUID uuid) {
        long mins = plugin.getTimeManager().getTimeMinutes(uuid);
        int bonus = 0;
        for (Map.Entry<Integer, Integer> e : thresholds.entrySet()) if (mins >= e.getKey()) bonus = e.getValue();
        return bonus;
    }

    public String getBountyLabel(UUID uuid) {
        long mins = plugin.getTimeManager().getTimeMinutes(uuid);
        if (mins >= 1200) return "§4☠ MEGA BOUNTY §c(+45 mins)";
        if (mins >= 900) return "§c⚠ HIGH BOUNTY §6(+30 mins)";
        if (mins >= 600) return "§e⚡ BOUNTY §7(+15 mins)";
        return null;
    }

    public void announceBounty(Player target) {
        String label = getBountyLabel(target.getUniqueId());
        if (label == null) return;
        Bukkit.broadcastMessage("§8[§6⏳§8] §e" + target.getName() + " §7now has a " + label + "§7!");
    }

    public void checkAndAnnounce(Player player, long prevMins, long nowMins) {
        for (int threshold : thresholds.keySet())
            if (nowMins >= threshold && prevMins < threshold)
                Bukkit.getScheduler().runTaskLater(plugin, () -> announceBounty(player), 1L);
    }
}
