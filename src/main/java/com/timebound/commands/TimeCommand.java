package com.timebound.commands;

import com.timebound.TimeBoundPlugin;
import com.timebound.managers.ScoreboardManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.UUID;

public class TimeCommand implements CommandExecutor {
    private final TimeBoundPlugin plugin;
    public TimeCommand(TimeBoundPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) { sender.sendMessage("§cPlayers only."); return true; }
        long mins = plugin.getTimeManager().getTimeMinutes(p.getUniqueId());
        String tier = plugin.getChatManager().getTierName(p.getUniqueId());
        String bounty = plugin.getBountyManager().getBountyLabel(p.getUniqueId());
        p.sendMessage("§8[§6⏳§8] §7Your time: §f" + ScoreboardManager.formatTime(mins));
        p.sendMessage("§8[§6⏳§8] §7Tier: " + tier);
        if (bounty != null) p.sendMessage("§8[§6⏳§8] §7Bounty: " + bounty);
        return true;
    }
}
