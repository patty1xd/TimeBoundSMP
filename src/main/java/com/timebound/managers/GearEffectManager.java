package com.timebound.managers;

import com.timebound.TimeBoundPlugin;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.Random;

public class GearEffectManager {
    private final TimeBoundPlugin plugin;
    private final Random random = new Random();
    public GearEffectManager(TimeBoundPlugin plugin) { this.plugin = plugin; }

    public double applyDefensiveEffects(Player victim, Player attacker, double damage) {
        double d = damage;
        for (ItemStack piece : victim.getInventory().getArmorContents()) {
            if (piece == null || !piece.hasItemMeta() || !piece.getItemMeta().hasDisplayName()) continue;
            switch (piece.getItemMeta().getDisplayName()) {
                case "§b§lTemporal Chestguard" -> { if (attacker != null) attacker.damage(damage * 0.15); }
                case "§5§lWraithplate" -> { if (random.nextDouble() < 0.20) { victim.sendMessage("§5§lDODGE!"); return 0; } }
                case "§8§lIronwall Aegis" -> d *= 0.80;
                case "§8§lEarthbound Stompers" -> d *= 0.85;
                case "§5§lVoidgaze Cowl" -> victim.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 40, 0, false, false));
                case "§6§lScorchplate" -> { if (attacker != null) attacker.setFireTicks(40); }
            }
        }
        return d;
    }

    public void applyOffensiveEffects(Player attacker, Player victim) {
        ItemStack hand = attacker.getInventory().getItemInMainHand();
        if (hand != null && hand.hasItemMeta() && hand.getItemMeta().hasDisplayName()) {
            switch (hand.getItemMeta().getDisplayName()) {
                case "§5§lEclipser" -> victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 0));
                case "§8§lColossus Cleaver" -> victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 0));
                case "§6§lEmbervein Saber" -> victim.setFireTicks(60);
                case "§c§lCrimson Fang" -> attacker.setHealth(Math.min(attacker.getAttribute(Attribute.MAX_HEALTH).getValue(), attacker.getHealth() + 0.8));
            }
        }
        for (ItemStack piece : attacker.getInventory().getArmorContents()) {
            if (piece == null || !piece.hasItemMeta() || !piece.getItemMeta().hasDisplayName()) continue;
            switch (piece.getItemMeta().getDisplayName()) {
                case "§6§lAshwalker Greaves" -> victim.setFireTicks(20);
                case "§c§lBerserker's Ribcage" -> attacker.setHealth(Math.min(attacker.getAttribute(Attribute.MAX_HEALTH).getValue(), attacker.getHealth() + 1.0));
            }
        }
    }

    public void applyPassiveEffects(Player player) {
        double hp = player.getHealth();
        double maxHp = player.getAttribute(Attribute.MAX_HEALTH).getValue();
        for (ItemStack piece : player.getInventory().getArmorContents()) {
            if (piece == null || !piece.hasItemMeta() || !piece.getItemMeta().hasDisplayName()) continue;
            switch (piece.getItemMeta().getDisplayName()) {
                case "§b§lEon Leggings" -> player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, 1, true, false));
                case "§b§lSwiftstep Boots" -> player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 1, true, false));
                case "§5§lPhantom Soles" -> player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 1, true, false));
                case "§c§lBloodsoaked Treads" -> { if (hp/maxHp < 0.4) player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 1, true, false)); }
                case "§6§lCinderstep Boots" -> { if (player.getWorld().getEnvironment() == org.bukkit.World.Environment.NETHER) player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 1, true, false)); }
                case "§8§lEarthbound Stompers" -> player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 0, true, false));
            }
        }
    }

    public boolean hasArmorPiece(Player player, String displayName) {
        for (ItemStack piece : player.getInventory().getArmorContents()) {
            if (piece == null || !piece.hasItemMeta()) continue;
            if (piece.getItemMeta().hasDisplayName() && piece.getItemMeta().getDisplayName().equals(displayName)) return true;
        }
        return false;
    }
}
