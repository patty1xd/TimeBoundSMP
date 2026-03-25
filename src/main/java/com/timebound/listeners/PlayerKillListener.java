package com.timebound.listeners;

import com.timebound.TimeBoundPlugin;
import com.timebound.managers.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerKillListener implements Listener {
    private final TimeBoundPlugin plugin;
    public PlayerKillListener(TimeBoundPlugin plugin) { this.plugin = plugin; }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        long victimMins = plugin.getTimeManager().getTimeMinutes(victim.getUniqueId());
        String victimTime = ScoreboardManager.formatTime(victimMins);

        if (killer == null || killer == victim) {
            Bukkit.broadcastMessage("§8[§6⏳§8] §e" + victim.getName() + " §7died with §f" + victimTime + " §7on the clock.");
            plugin.getTimeManager().removeMinutes(victim.getUniqueId(), plugin.getConfig().getLong("death-penalty", 30));
            return;
        }

        long prevKillerMins = plugin.getTimeManager().getTimeMinutes(killer.getUniqueId());

        if (victimMins <= 0) {
            killer.sendMessage("§8[§6⏳§8] §c" + victim.getName() + " §7is at 0 time. No time earned.");
            Bukkit.broadcastMessage("§8[§6⏳§8] §e" + killer.getName() + " §7slew §c" + victim.getName() + " §7(§c0 time§7)");
            plugin.getTimeManager().removeMinutes(victim.getUniqueId(), plugin.getConfig().getLong("death-penalty", 30));
            return;
        }

        if (plugin.getKillCooldownManager().isOnCooldown(killer.getUniqueId(), victim.getUniqueId())) {
            long remaining = plugin.getKillCooldownManager().getRemainingMinutes(killer.getUniqueId(), victim.getUniqueId());
            killer.sendMessage("§8[§6⏳§8] §c" + victim.getName() + " §7is on cooldown. (§e" + remaining + "m §7remaining)");
            Bukkit.broadcastMessage("§8[§6⏳§8] §e" + killer.getName() + " §7slew §e" + victim.getName() + " §7[§f" + victimTime + "§7] §8(cooldown)");
            plugin.getTimeManager().removeMinutes(victim.getUniqueId(), plugin.getConfig().getLong("death-penalty", 30));
            return;
        }

        long killReward = plugin.getConfig().getLong("kill-reward", 30);
        int bountyBonus = plugin.getBountyManager().getBountyBonus(victim.getUniqueId());
        long totalGain = killReward + bountyBonus;

        plugin.getKillCooldownManager().setCooldown(killer.getUniqueId(), victim.getUniqueId());
        plugin.getTimeManager().addMinutes(killer.getUniqueId(), totalGain);
        plugin.getTimeManager().removeMinutes(victim.getUniqueId(), plugin.getConfig().getLong("death-penalty", 30));

        long newKillerMins = plugin.getTimeManager().getTimeMinutes(killer.getUniqueId());
        plugin.getBountyManager().checkAndAnnounce(killer, prevKillerMins, newKillerMins);

        String bountyMsg = bountyBonus > 0 ? " §6(+§e" + bountyBonus + "m §6bounty§6)" : "";
        Bukkit.broadcastMessage("§8[§6⏳§8] §e" + killer.getName() + " §7slew §c" + victim.getName() + " §7[§f" + victimTime + "§7] §a+§e" + totalGain + "m" + bountyMsg);
        killer.sendMessage("§8[§6⏳§8] §aYou earned §6+" + totalGain + " mins§a!" + bountyMsg);
        victim.sendMessage("§8[§6⏳§8] §cYou lost §4-30 mins §cfrom dying to §e" + killer.getName() + "§c!");
        if (victimMins - 30 <= 0) victim.sendMessage("§8[§6⏳§8] §4⚠ You are at 0 time!");
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof EnderDragon dragon) {
            Player killer = dragon.getKiller();
            if (killer == null) return;
            if (!plugin.getTimeManager().hasDragonReward(killer.getUniqueId())) {
                plugin.getTimeManager().setDragonRewarded(killer.getUniqueId());
                plugin.getTimeManager().addMinutes(killer.getUniqueId(), 180);
                killer.sendMessage("§8[§6⏳§8] §6🐉 You slew the Ender Dragon! §a+§e3 hours§a awarded!");
                Bukkit.broadcastMessage("§8[§6⏳§8] §e" + killer.getName() + " §7slew the §5Ender Dragon§7! §6+3 hours!");
            } else {
                killer.sendMessage("§8[§6⏳§8] §7You already claimed the dragon kill reward.");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(event.getDamager() instanceof Player attacker)) return;
        plugin.getGearEffectManager().applyOffensiveEffects(attacker, victim);
        double newDamage = plugin.getGearEffectManager().applyDefensiveEffects(victim, attacker, event.getDamage());
        if (newDamage <= 0) event.setCancelled(true);
        else event.setDamage(newDamage);
        double hp = attacker.getHealth(), maxHp = attacker.getAttribute(Attribute.MAX_HEALTH).getValue();
        if (hp/maxHp < 0.5 && plugin.getGearEffectManager().hasArmorPiece(attacker, "§c§lSkull of the Frenzied"))
            event.setDamage(event.getDamage() + 2.0);
    }
}
