package com.timebound.managers;

import com.timebound.TimeBoundPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import java.util.*;

public class ScoreboardManager {
    private final TimeBoundPlugin plugin;
    private final Map<UUID, Scoreboard> boards = new HashMap<>();

    public ScoreboardManager(TimeBoundPlugin plugin) { this.plugin = plugin; }

    public void startUpdater() {
        int interval = plugin.getConfig().getInt("scoreboard-update-interval", 20);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) updatePlayer(p);
        }, interval, interval);
    }

    public void updatePlayer(Player player) {
        Scoreboard board = boards.computeIfAbsent(player.getUniqueId(), k -> Bukkit.getScoreboardManager().getNewScoreboard());
        Objective obj = board.getObjective("timebound");
        if (obj != null) obj.unregister();
        obj = board.registerNewObjective("timebound", Criteria.DUMMY, "§6§l⏳ TIMEBOUND");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        long myMins = plugin.getTimeManager().getTimeMinutes(player.getUniqueId());
        String myBounty = plugin.getBountyManager().getBountyLabel(player.getUniqueId());
        List<Map.Entry<UUID, Long>> sorted = new ArrayList<>(plugin.getTimeManager().getAllTimes().entrySet());
        sorted.sort((a, b) -> Long.compare(b.getValue(), a.getValue()));
        int line = 15;
        setLine(obj, "§7─────────────────", line--);
        setLine(obj, "§eYour Time: §f" + formatTime(myMins), line--);
        if (myBounty != null) setLine(obj, myBounty, line--);
        setLine(obj, "§7─────────────────", line--);
        setLine(obj, "§6§lTOP PLAYERS", line--);
        int rank = 1;
        for (Map.Entry<UUID, Long> entry : sorted) {
            if (rank > 5) break;
            String name = Bukkit.getOfflinePlayer(entry.getKey()).getName();
            if (name == null) { rank++; continue; }
            String prefix = rank == 1 ? "§6#1 " : rank == 2 ? "§7#2 " : "§c#" + rank + " ";
            String bi = plugin.getBountyManager().hasBounty(entry.getKey()) ? " §c☠" : "";
            setLine(obj, prefix + name + bi + " §f" + formatTime(entry.getValue()), line--);
            rank++;
        }
        setLine(obj, "§7─────────────────", line--);
        player.setScoreboard(board);
    }

    private void setLine(Objective obj, String text, int score) { obj.getScore(text).setScore(score); }

    public static String formatTime(long totalMinutes) {
        if (totalMinutes <= 0) return "§c0m";
        long h = totalMinutes / 60, m = totalMinutes % 60;
        return h > 0 ? "§e" + h + "h " + m + "m" : "§e" + m + "m";
    }
}
