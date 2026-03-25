package com.timebound.commands;

import com.timebound.TimeBoundPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AdminTimeCommand implements CommandExecutor {
    private final TimeBoundPlugin plugin;
    public AdminTimeCommand(TimeBoundPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("timebound.admin")) { sender.sendMessage("§cNo permission."); return true; }
        if (args.length < 2) { sender.sendMessage("§cUsage: /" + label + " <player> <minutes>"); return true; }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (!target.hasPlayedBefore()) { sender.sendMessage("§cPlayer not found."); return true; }
        long amount; try { amount = Long.parseLong(args[1]); } catch (NumberFormatException e) { sender.sendMessage("§cInvalid number."); return true; }
        switch (label.toLowerCase()) {
            case "addtime" -> { plugin.getTimeManager().addMinutes(target.getUniqueId(), amount); sender.sendMessage("§aAdded §e" + amount + "m §ato §f" + target.getName()); }
            case "removetime" -> { plugin.getTimeManager().removeMinutes(target.getUniqueId(), amount); sender.sendMessage("§aRemoved §e" + amount + "m §afrom §f" + target.getName()); }
            case "settime" -> { plugin.getTimeManager().setTime(target.getUniqueId(), amount); sender.sendMessage("§aSet §f" + target.getName() + "§a's time to §e" + amount + "m"); }
        }
        return true;
    }
}
