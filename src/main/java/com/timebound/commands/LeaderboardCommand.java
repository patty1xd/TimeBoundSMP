package com.timebound.commands;

import com.timebound.TimeBoundPlugin;
import com.timebound.managers.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.util.*;

public class LeaderboardCommand implements CommandExecutor {
    private final TimeBoundPlugin plugin;
    public LeaderboardCommand(TimeBoundPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        List<Map.Entry<UUID, Long>> sorted = new ArrayList<>(plugin.getTimeManager().getAllTimes().entrySet());
        sorted.sort((a, b) -> Long.compare(b.getValue(), a.getValue()));
        sender.sendMessage("§6§l⏳ TIME LEADERBOARD");
        sender.sendMessage("§7─────────────────────");
        String[] medals = {"§6#1","§7#2","§c#3","§f#4","§f#5"};
        for (int i = 0; i < Math.min(10, sorted.size()); i++) {
            String name = Bukkit.getOfflinePlayer(sorted.get(i).getKey()).getName();
            if (name == null) continue;
            String prefix = i < medals.length ? medals[i] : "§7#" + (i+1);
            String bounty = plugin.getBountyManager().hasBounty(sorted.get(i).getKey()) ? " §c☠" : "";
            sender.sendMessage(prefix + " §f" + name + bounty + " §8- §e" + ScoreboardManager.formatTime(sorted.get(i).getValue()));
        }
        return true;
    }
}
