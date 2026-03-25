package com.timebound.managers;

import com.timebound.TimeBoundPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class ChatManager {
    private final TimeBoundPlugin plugin;
    public ChatManager(TimeBoundPlugin plugin) { this.plugin = plugin; }

    public String getTierName(UUID uuid) {
        long mins = plugin.getTimeManager().getTimeMinutes(uuid);
        if (mins >= 1200) return "§4☠ Immortal";
        if (mins >= 900) return "§dWarlord";
        if (mins >= 600) return "§6Champion";
        if (mins >= 480) return "§bVeteran";
        if (mins >= 360) return "§aSeasoned";
        if (mins >= 240) return "§fSurvivor";
        return "§7Drifter";
    }

    public String formatChatMessage(Player player, String message) {
        long mins = plugin.getTimeManager().getTimeMinutes(player.getUniqueId());
        String timeStr = ScoreboardManager.formatTime(mins);
        String name = mins >= 1200 ? "§4§l☠ " + player.getName() + " §4§l☠" : player.getName();
        String tag = mins >= 1200 ? "§4" : mins >= 600 ? "§6" : mins >= 240 ? "§7" : "§8";
        return tag + "[" + timeStr + "§r" + tag + "] " + name + " §7» §f" + message;
    }

    public void updatePlayerDisplay(Player player) {
        long mins = plugin.getTimeManager().getTimeMinutes(player.getUniqueId());
        org.bukkit.scoreboard.Scoreboard board = player.getScoreboard();
        if (board == null) board = Bukkit.getScoreboardManager().getMainScoreboard();
        String teamName = "tb_" + player.getName().substring(0, Math.min(player.getName().length(), 10));
        Team team = board.getTeam(teamName);
        if (team == null) team = board.registerNewTeam(teamName);
        if (mins >= 1200) { team.setPrefix("§4☠ "); team.setSuffix(" §4☠"); }
        else if (mins >= 900) { team.setPrefix("§d"); team.setSuffix(""); }
        else if (mins >= 600) { team.setPrefix("§6"); team.setSuffix(""); }
        else { team.setPrefix("§f"); team.setSuffix(""); }
        team.addEntry(player.getName());
    }
}
