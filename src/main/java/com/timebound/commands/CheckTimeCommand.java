package com.timebound.commands;

import com.timebound.TimeBoundPlugin;
import com.timebound.managers.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CheckTimeCommand implements CommandExecutor {
    private final TimeBoundPlugin plugin;
    public CheckTimeCommand(TimeBoundPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) { sender.sendMessage("§cUsage: /checktime <player>"); return true; }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (!target.hasPlayedBefore()) { sender.sendMessage("§cPlayer not found."); return true; }
        long mins = plugin.getTimeManager().getTimeMinutes(target.getUniqueId());
        String bounty = plugin.getBountyManager().getBountyLabel(target.getUniqueId());
        sender.sendMessage("§8[§6⏳§8] §e" + target.getName() + "§7's time: §f" + ScoreboardManager.formatTime(mins));
        if (bounty != null) sender.sendMessage("§8[§6⏳§8] §7Bounty: " + bounty);
        return true;
    }
}
