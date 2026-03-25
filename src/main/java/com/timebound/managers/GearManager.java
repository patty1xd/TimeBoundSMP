package com.timebound.managers;

import com.timebound.TimeBoundPlugin;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.*;

public class GearManager {
    private final TimeBoundPlugin plugin;
    private final Random random = new Random();
    private final List<ItemStack> armorPieces = new ArrayList<>();
    private final List<ItemStack> swords = new ArrayList<>();

    public GearManager(TimeBoundPlugin plugin) { this.plugin = plugin; buildItems(); }

    private void buildItems() {
        // CHRONO
        armorPieces.add(a(Material.NETHERITE_HELMET,"§b§lChrono Helm","§7\"Time bends around your head\"",List.of("§e• Reduced knockback","§e• Increased reach","§e• +200 durability"),Map.of(Enchantment.KNOCKBACK,2)));
        armorPieces.add(a(Material.NETHERITE_CHESTPLATE,"§b§lTemporal Chestguard","§7\"Returns part of the blow to your enemies\"",List.of("§e• 15% damage reflection","§e• +200 durability"),Map.of(Enchantment.THORNS,3)));
        armorPieces.add(a(Material.NETHERITE_LEGGINGS,"§b§lEon Leggings","§7\"Regeneration flows through your legs\"",List.of("§e• Regeneration II while worn","§e• +200 durability"),Map.of(Enchantment.PROTECTION,5)));
        armorPieces.add(a(Material.NETHERITE_BOOTS,"§b§lSwiftstep Boots","§7\"Step faster through the sands of time\"",List.of("§e• Speed II while worn","§e• +200 durability"),Map.of(Enchantment.FEATHER_FALLING,4)));
        // BLOODRAGE
        armorPieces.add(a(Material.NETHERITE_HELMET,"§c§lSkull of the Frenzied","§7\"It remembers every wound\"",List.of("§e• +1 dmg when below 50% HP","§e• +200 durability"),Map.of(Enchantment.PROTECTION,4)));
        armorPieces.add(a(Material.NETHERITE_CHESTPLATE,"§c§lBerserker's Ribcage","§7\"Pain is just fuel\"",List.of("§e• 10% lifesteal on hit","§e• +200 durability"),Map.of(Enchantment.THORNS,3)));
        armorPieces.add(a(Material.NETHERITE_LEGGINGS,"§c§lWrathwalkers","§7\"Every step fueled by anger\"",List.of("§e• +0.5 heart on player kill","§e• +200 durability"),Map.of(Enchantment.PROTECTION,4)));
        armorPieces.add(a(Material.NETHERITE_BOOTS,"§c§lBloodsoaked Treads","§7\"The ground remembers your kills\"",List.of("§e• Speed II when below 40% HP","§e• +200 durability"),Map.of(Enchantment.FEATHER_FALLING,4)));
        // PHANTOM
        armorPieces.add(a(Material.NETHERITE_HELMET,"§5§lVoidgaze Cowl","§7\"See without being seen\"",List.of("§e• Invisibility pulse on hit","§e• +200 durability"),Map.of(Enchantment.PROTECTION,4)));
        armorPieces.add(a(Material.NETHERITE_CHESTPLATE,"§5§lWraithplate","§7\"Between hits, you barely exist\"",List.of("§e• 20% dodge chance","§e• +200 durability"),Map.of(Enchantment.PROTECTION,5)));
        armorPieces.add(a(Material.NETHERITE_LEGGINGS,"§5§lAshstep Greaves","§7\"Leave no trace\"",List.of("§e• Reduced fall damage","§e• +200 durability"),Map.of(Enchantment.PROTECTION,4)));
        armorPieces.add(a(Material.NETHERITE_BOOTS,"§5§lPhantom Soles","§7\"You were never there\"",List.of("§e• Speed II","§e• Negates fall damage","§e• +200 durability"),Map.of(Enchantment.FEATHER_FALLING,10,Enchantment.DEPTH_STRIDER,3)));
        // TITAN
        armorPieces.add(a(Material.NETHERITE_HELMET,"§8§lCrestwall Helm","§7\"Mountains have fallen on this helmet\"",List.of("§e• +1 heart","§e• Knockback immunity","§e• +200 durability"),Map.of(Enchantment.PROTECTION,6)));
        armorPieces.add(a(Material.NETHERITE_CHESTPLATE,"§8§lIronwall Aegis","§7\"Nothing gets through\"",List.of("§e• 20% damage reduction","§e• Heavy thorns","§e• +200 durability"),Map.of(Enchantment.THORNS,5,Enchantment.PROTECTION,6)));
        armorPieces.add(a(Material.NETHERITE_LEGGINGS,"§8§lBedrock Cuisses","§7\"Rooted like stone\"",List.of("§e• Knockback immunity","§e• +1 heart","§e• +200 durability"),Map.of(Enchantment.PROTECTION,6)));
        armorPieces.add(a(Material.NETHERITE_BOOTS,"§8§lEarthbound Stompers","§7\"The ground fears your step\"",List.of("§e• 15% damage reduction","§e• Slowness 1 on wearer","§e• +200 durability"),Map.of(Enchantment.PROTECTION,5,Enchantment.FEATHER_FALLING,4)));
        // INFERNO
        armorPieces.add(a(Material.NETHERITE_HELMET,"§6§lEmbercrown","§7\"Forged where the world burns\"",List.of("§e• Fire immunity","§e• +0.5 heart","§e• +200 durability"),Map.of(Enchantment.FIRE_PROTECTION,10)));
        armorPieces.add(a(Material.NETHERITE_CHESTPLATE,"§6§lScorchplate","§7\"Touch it and regret it\"",List.of("§e• Ignites attacker 2s","§e• +200 durability"),Map.of(Enchantment.THORNS,3,Enchantment.FIRE_PROTECTION,8)));
        armorPieces.add(a(Material.NETHERITE_LEGGINGS,"§6§lAshwalker Greaves","§7\"Leave a trail of embers\"",List.of("§e• Ignites enemy on hit","§e• +200 durability"),Map.of(Enchantment.PROTECTION,4,Enchantment.FIRE_PROTECTION,6)));
        armorPieces.add(a(Material.NETHERITE_BOOTS,"§6§lCinderstep Boots","§7\"Every step burns a little\"",List.of("§e• Fire immunity","§e• Speed II in Nether","§e• +200 durability"),Map.of(Enchantment.FIRE_PROTECTION,10,Enchantment.FEATHER_FALLING,4)));
        // SWORDS
        swords.add(s("§b§lTimebreaker Blade","§7\"Each strike fractures a moment\"",List.of("§e• Unbreakable","§e• +3 damage","§e• +Attack Speed"),Map.of(Enchantment.SHARPNESS,6,Enchantment.SWEEPING_EDGE,3)));
        swords.add(s("§c§lCrimson Fang","§7\"It thirsts and it remembers\"",List.of("§e• Unbreakable","§e• 8% lifesteal","§e• +2 damage"),Map.of(Enchantment.SHARPNESS,5,Enchantment.FIRE_ASPECT,2)));
        swords.add(s("§5§lEclipser","§7\"Strikes from the dark land harder\"",List.of("§e• Unbreakable","§e• +2 damage","§e• Blindness on hit"),Map.of(Enchantment.SHARPNESS,5,Enchantment.KNOCKBACK,2)));
        swords.add(s("§8§lColossus Cleaver","§7\"It doesn't cut. It ends.\"",List.of("§e• Unbreakable","§e• +4 damage","§e• Slowness on hit"),Map.of(Enchantment.SHARPNESS,7,Enchantment.KNOCKBACK,3)));
        swords.add(s("§6§lEmbervein Saber","§7\"Still cooling down from the day it was made\"",List.of("§e• Unbreakable","§e• Sets enemy on fire 3s","§e• +2 damage"),Map.of(Enchantment.SHARPNESS,5,Enchantment.FIRE_ASPECT,3)));
    }

    private ItemStack a(Material mat, String name, String flavor, List<String> lore, Map<Enchantment,Integer> enc) {
        ItemStack i = new ItemStack(mat); ItemMeta m = i.getItemMeta();
        m.setDisplayName(name); List<String> l = new ArrayList<>(); l.add(flavor); l.add(""); l.addAll(lore);
        m.setLore(l); m.setUnbreakable(true); m.addEnchant(Enchantment.UNBREAKING,10,true);
        enc.forEach((e,v)->m.addEnchant(e,v,true)); i.setItemMeta(m); return i;
    }

    private ItemStack s(String name, String flavor, List<String> lore, Map<Enchantment,Integer> enc) {
        ItemStack i = new ItemStack(Material.NETHERITE_SWORD); ItemMeta m = i.getItemMeta();
        m.setDisplayName(name); List<String> l = new ArrayList<>(); l.add(flavor); l.add(""); l.addAll(lore);
        m.setLore(l); m.setUnbreakable(true); m.addEnchant(Enchantment.UNBREAKING,10,true);
        enc.forEach((e,v)->m.addEnchant(e,v,true)); i.setItemMeta(m); return i;
    }

    public void giveRandomArmorPiece(Player p) {
        ItemStack piece = armorPieces.get(random.nextInt(armorPieces.size())).clone();
        p.getInventory().addItem(piece);
        String n = piece.getItemMeta().getDisplayName();
        p.sendMessage("§8[§6⏳§8] §6🎁 Milestone reward! You received: " + n);
        plugin.getServer().broadcastMessage("§8[§6⏳§8] §e" + p.getName() + " §7unlocked a milestone and received §6" + n + "§7!");
    }

    public void giveRandomSword(Player p) {
        ItemStack sword = swords.get(random.nextInt(swords.size())).clone();
        p.getInventory().addItem(sword);
        String n = sword.getItemMeta().getDisplayName();
        p.sendMessage("§8[§6⏳§8] §6🎁 Milestone reward! You received: " + n);
        plugin.getServer().broadcastMessage("§8[§6⏳§8] §e" + p.getName() + " §7unlocked a milestone and received §6" + n + "§7!");
    }

    public boolean isCustomGear(ItemStack item) {
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return false;
        String name = item.getItemMeta().getDisplayName();
        for (ItemStack a : armorPieces) if (a.getItemMeta().getDisplayName().equals(name)) return true;
        for (ItemStack s : swords) if (s.getItemMeta().getDisplayName().equals(name)) return true;
        return false;
    }
}
