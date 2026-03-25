package com.timebound.commands;

import com.timebound.TimeBoundPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCommand implements CommandExecutor {
    private final TimeBoundPlugin plugin;
    public ShopCommand(TimeBoundPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) { sender.sendMessage("§cPlayers only."); return true; }
        p.openInventory(plugin.getShopManager().buildShopInventory());
        return true;
    }
}
