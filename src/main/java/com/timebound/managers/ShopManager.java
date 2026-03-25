package com.timebound.managers;

import com.timebound.TimeBoundPlugin;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.*;

public class ShopManager {
    private final TimeBoundPlugin plugin;
    public ShopManager(TimeBoundPlugin plugin) { this.plugin = plugin; }

    public Inventory buildShopInventory() {
        Inventory inv = plugin.getServer().createInventory(null, 27, "§6§l⏳ Time Shop");
        inv.setItem(11, buildShopItem(Material.IRON_SWORD, "§f§lStarter Kit", Arrays.asList("§7Iron armor, sword, shield", "§7Bow, 64 arrows, 32 steak", "", "§6Cost: §e15 minutes", "", "§aClick to purchase!")));
        inv.setItem(13, buildShopItem(Material.DIAMOND_SWORD, "§b§lAdvanced Kit", Arrays.asList("§7Diamond armor, sword, shield", "§710 golden apples, 32 steak", "§716 ender pearls", "", "§6Cost: §e45 minutes", "", "§aClick to purchase!")));
        inv.setItem(15, buildShopItem(Material.NETHERITE_SWORD, "§4§lWarlord Kit", Arrays.asList("§7Netherite armor, sword, shield", "§716 golden apples, 3 totems", "§716 ender pearls, 32 steak", "", "§6Cost: §e60 minutes", "", "§aClick to purchase!")));
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta m = filler.getItemMeta(); m.setDisplayName(" "); filler.setItemMeta(m);
        for (int i = 0; i < 27; i++) if (inv.getItem(i) == null) inv.setItem(i, filler);
        return inv;
    }

    private ItemStack buildShopItem(Material mat, String name, List<String> lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name); meta.setLore(lore); item.setItemMeta(meta); return item;
    }

    public void giveStarterKit(Player p) {
        giveEnchanted(p, Material.IRON_HELMET, Map.of(Enchantment.PROTECTION, 2));
        giveEnchanted(p, Material.IRON_CHESTPLATE, Map.of(Enchantment.PROTECTION, 2));
        giveEnchanted(p, Material.IRON_LEGGINGS, Map.of(Enchantment.PROTECTION, 2));
        giveEnchanted(p, Material.IRON_BOOTS, Map.of(Enchantment.PROTECTION, 2));
        giveEnchanted(p, Material.IRON_SWORD, Map.of(Enchantment.SHARPNESS, 2));
        p.getInventory().addItem(new ItemStack(Material.SHIELD), new ItemStack(Material.BOW), new ItemStack(Material.ARROW, 64), new ItemStack(Material.COOKED_BEEF, 32));
    }

    public void giveAdvancedKit(Player p) {
        giveEnchanted(p, Material.DIAMOND_HELMET, Map.of(Enchantment.PROTECTION, 3));
        giveEnchanted(p, Material.DIAMOND_CHESTPLATE, Map.of(Enchantment.PROTECTION, 3));
        giveEnchanted(p, Material.DIAMOND_LEGGINGS, Map.of(Enchantment.PROTECTION, 3));
        giveEnchanted(p, Material.DIAMOND_BOOTS, Map.of(Enchantment.PROTECTION, 3, Enchantment.FEATHER_FALLING, 3));
        giveEnchanted(p, Material.DIAMOND_SWORD, Map.of(Enchantment.SHARPNESS, 3));
        p.getInventory().addItem(new ItemStack(Material.SHIELD), new ItemStack(Material.GOLDEN_APPLE, 10), new ItemStack(Material.COOKED_BEEF, 32), new ItemStack(Material.ENDER_PEARL, 16));
    }

    public void giveWarlordKit(Player p) {
        giveEnchanted(p, Material.NETHERITE_HELMET, Map.of(Enchantment.PROTECTION, 4));
        giveEnchanted(p, Material.NETHERITE_CHESTPLATE, Map.of(Enchantment.PROTECTION, 4));
        giveEnchanted(p, Material.NETHERITE_LEGGINGS, Map.of(Enchantment.PROTECTION, 4));
        giveEnchanted(p, Material.NETHERITE_BOOTS, Map.of(Enchantment.PROTECTION, 4, Enchantment.FEATHER_FALLING, 4));
        giveEnchanted(p, Material.NETHERITE_SWORD, Map.of(Enchantment.SHARPNESS, 5));
        p.getInventory().addItem(new ItemStack(Material.SHIELD), new ItemStack(Material.GOLDEN_APPLE, 16), new ItemStack(Material.TOTEM_OF_UNDYING, 3), new ItemStack(Material.ENDER_PEARL, 16), new ItemStack(Material.COOKED_BEEF, 32));
    }

    private void giveEnchanted(Player p, Material mat, Map<Enchantment, Integer> enchants) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        for (var e : enchants.entrySet()) meta.addEnchant(e.getKey(), e.getValue(), true);
        item.setItemMeta(meta); p.getInventory().addItem(item);
    }
}
