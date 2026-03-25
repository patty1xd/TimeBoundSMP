package com.timebound.listeners;

import com.timebound.TimeBoundPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinListener implements Listener {
    private final TimeBoundPlugin plugin;
    public PlayerJoinListener(TimeBoundPlugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        plugin.getTimeManager().initPlayer(player.getUniqueId());
        plugin.getBuffManager().updateBuffs(player);
        plugin.getBoardManager().updatePlayer(player);
        plugin.getChatManager().updatePlayerDisplay(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) { plugin.getTimeManager().saveAll(); }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        String formatted = plugin.getChatManager().formatChatMessage(event.getPlayer(), event.getMessage());
        event.getPlayer().getServer().broadcastMessage(formatted);
    }
}
