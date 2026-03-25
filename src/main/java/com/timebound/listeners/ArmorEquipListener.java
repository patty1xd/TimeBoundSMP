package com.timebound.listeners;

import com.timebound.TimeBoundPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ArmorEquipListener implements Listener {
    private final TimeBoundPlugin plugin;
    public ArmorEquipListener(TimeBoundPlugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getGearEffectManager().applyPassiveEffects(player), 1L);
    }
}
