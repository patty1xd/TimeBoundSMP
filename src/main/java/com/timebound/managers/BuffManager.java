package com.timebound.managers;

import com.timebound.TimeBoundPlugin;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.UUID;

public class BuffManager {
    private final TimeBoundPlugin plugin;
    private static final UUID HEALTH_UUID = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");
    private static final String HEALTH_NAME = "timebound.health";

    public BuffManager(TimeBoundPlugin plugin) { this.plugin = plugin; }

    public void startChecker() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) updateBuffs(p);
        }, 100L, 100L);
    }

    public void updateBuffs(Player player) {
        long mins = plugin.getTimeManager().getTimeMinutes(player.getUniqueId());
        removeAllManagedEffects(player);
        applyEffects(player, mins);
        applyHealthModifier(player, mins);
    }

    private void removeAllManagedEffects(Player player) {
        player.removePotionEffect(PotionEffectType.HASTE);
        player.removePotionEffect(PotionEffectType.REGENERATION);
        player.removePotionEffect(PotionEffectType.SPEED);
        player.removePotionEffect(PotionEffectType.SATURATION);
        player.removePotionEffect(PotionEffectType.STRENGTH);
    }

    private void applyEffects(Player player, long mins) {
        int inf = Integer.MAX_VALUE;
        if (mins >= 240) player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, inf, 0, true, false, true));
        if (mins >= 360) player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, inf, 0, true, false, true));
        if (mins >= 420) player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, inf, 0, true, false, true));
        if (mins >= 480) player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, inf, 0, true, false, true));
        if (mins >= 600) player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, inf, 0, true, false, true));
    }

    private void applyHealthModifier(Player player, long mins) {
        var attr = player.getAttribute(Attribute.MAX_HEALTH);
        if (attr == null) return;
        attr.getModifiers().stream().filter(m -> m.getName().equals(HEALTH_NAME)).forEach(attr::removeModifier);
        double bonus = 0;
        if (mins >= 300) bonus += 2.0;
        if (mins >= 420) bonus += 1.0;
        if (mins >= 480) bonus += 1.0;
        if (bonus > 0) {
            attr.addModifier(new AttributeModifier(HEALTH_UUID, HEALTH_NAME, bonus, AttributeModifier.Operation.ADD_NUMBER));
        }
        if (player.getHealth() > attr.getValue()) player.setHealth(attr.getValue());
    }

    public void removeAllBuffs(Player player) {
        removeAllManagedEffects(player);
        var attr = player.getAttribute(Attribute.MAX_HEALTH);
        if (attr != null) attr.getModifiers().stream().filter(m -> m.getName().equals(HEALTH_NAME)).forEach(attr::removeModifier);
    }
}
