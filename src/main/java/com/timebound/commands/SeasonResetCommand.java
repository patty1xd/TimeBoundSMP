package com.timebound.commands;

import com.timebound.TimeBoundPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SeasonResetCommand implements CommandExecutor {
    private final TimeBoundPlugin plugin;
    public SeasonResetCommand(TimeBoundPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("timebound.admin")) { sender.sendMessage("§cNo permission."); return true; }
        if (args.length < 1 || !args[0].equalsIgnoreCase("confirm")) { sender.sendMessage("§c⚠ Type §f/seasonreset confirm §cto wipe all times!"); return true; }
        plugin.getTimeManager().resetAll();
        Bukkit.broadcastMessage("§8[§6⏳§8] §4§l⚠ SEASON RESET! §7All times wiped. New season begins!");
        return true;
    }
}
