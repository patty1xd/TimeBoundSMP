package com.timebound.listeners;

import com.timebound.TimeBoundPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ShopListener implements Listener {
    private final TimeBoundPlugin plugin;
    public ShopListener(TimeBoundPlugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void onShopClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("§6§l⏳ Time Shop")) return;
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;
        if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;
        String name = event.getCurrentItem().getItemMeta().getDisplayName();
        long mins = plugin.getTimeManager().getTimeMinutes(player.getUniqueId());
        switch (name) {
            case "§f§lStarter Kit" -> { if (mins < 15) { player.sendMessage("§cNeed §e15 mins§c."); return; } plugin.getTimeManager().removeMinutes(player.getUniqueId(), 15); plugin.getShopManager().giveStarterKit(player); player.sendMessage("§aPurchased §fStarter Kit§a! §c-15 mins."); player.closeInventory(); }
            case "§b§lAdvanced Kit" -> { if (mins < 45) { player.sendMessage("§cNeed §e45 mins§c."); return; } plugin.getTimeManager().removeMinutes(player.getUniqueId(), 45); plugin.getShopManager().giveAdvancedKit(player); player.sendMessage("§aPurchased §bAdvanced Kit§a! §c-45 mins."); player.closeInventory(); }
            case "§4§lWarlord Kit" -> { if (mins < 60) { player.sendMessage("§cNeed §e60 mins§c."); return; } plugin.getTimeManager().removeMinutes(player.getUniqueId(), 60); plugin.getShopManager().giveWarlordKit(player); player.sendMessage("§aPurchased §4Warlord Kit§a! §c-60 mins."); player.closeInventory(); }
        }
    }
}
