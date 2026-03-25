package com.timebound.managers;

import com.timebound.TimeBoundPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class TimeManager {
    private final TimeBoundPlugin plugin;
    private final Map<UUID, Long> playerTime = new HashMap<>();
    private final Map<UUID, Set<Integer>> claimedMilestones = new HashMap<>();
    private final Set<UUID> dragonKillRewarded = new HashSet<>();
    private File dataFile;
    private FileConfiguration dataConfig;

    public static final int[] ARMOR_MILESTONES = {540, 780, 1020, 1200};
    public static final int SWORD_MILESTONE = 900;

    public TimeManager(TimeBoundPlugin plugin) {
        this.plugin = plugin;
        loadData();
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::saveAll, 6000L, 6000L);
    }

    public long getTimeMinutes(UUID uuid) { return playerTime.getOrDefault(uuid, 0L); }

    public void setTime(UUID uuid, long minutes) {
        long prev = playerTime.getOrDefault(uuid, 0L);
        playerTime.put(uuid, Math.max(0, minutes));
        long now = playerTime.get(uuid);
        Player p = Bukkit.getPlayer(uuid);
        if (p != null) {
            plugin.getBuffManager().updateBuffs(p);
            plugin.getBoardManager().updatePlayer(p);
            plugin.getChatManager().updatePlayerDisplay(p);
            checkMilestones(p, prev, now);
        }
    }

    public void addMinutes(UUID uuid, long minutes) { setTime(uuid, getTimeMinutes(uuid) + minutes); }
    public void removeMinutes(UUID uuid, long minutes) { setTime(uuid, getTimeMinutes(uuid) - minutes); }

    public void initPlayer(UUID uuid) {
        if (!playerTime.containsKey(uuid))
            playerTime.put(uuid, (long) plugin.getConfig().getInt("starting-time", 180));
    }

    public boolean hasDragonReward(UUID uuid) { return dragonKillRewarded.contains(uuid); }
    public void setDragonRewarded(UUID uuid) { dragonKillRewarded.add(uuid); }
    public boolean hasClaimed(UUID uuid, int milestone) {
        return claimedMilestones.computeIfAbsent(uuid, k -> new HashSet<>()).contains(milestone);
    }
    private void setClaimed(UUID uuid, int milestone) {
        claimedMilestones.computeIfAbsent(uuid, k -> new HashSet<>()).add(milestone);
    }

    private void checkMilestones(Player p, long prev, long now) {
        for (int m : ARMOR_MILESTONES) {
            if (now >= m && prev < m && !hasClaimed(p.getUniqueId(), m)) {
                setClaimed(p.getUniqueId(), m);
                plugin.getGearManager().giveRandomArmorPiece(p);
            }
        }
        if (now >= SWORD_MILESTONE && prev < SWORD_MILESTONE && !hasClaimed(p.getUniqueId(), SWORD_MILESTONE)) {
            setClaimed(p.getUniqueId(), SWORD_MILESTONE);
            plugin.getGearManager().giveRandomSword(p);
        }
    }

    public Map<UUID, Long> getAllTimes() { return Collections.unmodifiableMap(playerTime); }

    public void resetAll() {
        playerTime.clear(); claimedMilestones.clear(); dragonKillRewarded.clear(); saveAll();
    }

    public void saveAll() {
        dataConfig.set("times", null); dataConfig.set("milestones", null); dataConfig.set("dragon", null);
        for (Map.Entry<UUID, Long> e : playerTime.entrySet()) dataConfig.set("times." + e.getKey(), e.getValue());
        for (Map.Entry<UUID, Set<Integer>> e : claimedMilestones.entrySet())
            dataConfig.set("milestones." + e.getKey(), new ArrayList<>(e.getValue()));
        List<String> dl = new ArrayList<>();
        for (UUID u : dragonKillRewarded) dl.add(u.toString());
        dataConfig.set("dragon", dl);
        try { dataConfig.save(dataFile); } catch (IOException ex) { ex.printStackTrace(); }
    }

    private void loadData() {
        dataFile = new File(plugin.getDataFolder(), "timedata.yml");
        if (!dataFile.exists()) { plugin.getDataFolder().mkdirs(); try { dataFile.createNewFile(); } catch (IOException e) { e.printStackTrace(); } }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        if (dataConfig.isConfigurationSection("times"))
            for (String k : dataConfig.getConfigurationSection("times").getKeys(false))
                playerTime.put(UUID.fromString(k), dataConfig.getLong("times." + k));
        if (dataConfig.isConfigurationSection("milestones"))
            for (String k : dataConfig.getConfigurationSection("milestones").getKeys(false))
                claimedMilestones.put(UUID.fromString(k), new HashSet<>(dataConfig.getIntegerList("milestones." + k)));
        for (String s : dataConfig.getStringList("dragon")) dragonKillRewarded.add(UUID.fromString(s));
    }
}
