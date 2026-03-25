package com.timebound;

import com.timebound.commands.*;
import com.timebound.listeners.*;
import com.timebound.managers.*;
import org.bukkit.plugin.java.JavaPlugin;

public class TimeBoundPlugin extends JavaPlugin {

    private static TimeBoundPlugin instance;
    private TimeManager timeManager;
    private BuffManager buffManager;
    private ScoreboardManager scoreboardManager;
    private GearManager gearManager;
    private GearEffectManager gearEffectManager;
    private ChatManager chatManager;
    private BountyManager bountyManager;
    private ShopManager shopManager;
    private KillCooldownManager killCooldownManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        killCooldownManager = new KillCooldownManager(this);
        gearManager = new GearManager(this);
        gearEffectManager = new GearEffectManager(this);
        timeManager = new TimeManager(this);
        buffManager = new BuffManager(this);
        scoreboardManager = new ScoreboardManager(this);
        chatManager = new ChatManager(this);
        bountyManager = new BountyManager(this);
        shopManager = new ShopManager(this);
        getServer().getPluginManager().registerEvents(new PlayerKillListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new ArmorEquipListener(this), this);
        getServer().getPluginManager().registerEvents(new GearEffectListener(this), this);
        getServer().getPluginManager().registerEvents(new ShopListener(this), this);
        getCommand("time").setExecutor(new TimeCommand(this));
        getCommand("checktime").setExecutor(new CheckTimeCommand(this));
        getCommand("addtime").setExecutor(new AdminTimeCommand(this));
        getCommand("removetime").setExecutor(new AdminTimeCommand(this));
        getCommand("settime").setExecutor(new AdminTimeCommand(this));
        getCommand("timeleaderboard").setExecutor(new LeaderboardCommand(this));
        getCommand("timeshop").setExecutor(new ShopCommand(this));
        getCommand("seasonreset").setExecutor(new SeasonResetCommand(this));
        scoreboardManager.startUpdater();
        buffManager.startChecker();
        getLogger().info("TimeBound SMP enabled!");
    }

    @Override
    public void onDisable() {
        if (timeManager != null) timeManager.saveAll();
        getLogger().info("TimeBound SMP disabled. Data saved.");
    }

    public static TimeBoundPlugin getInstance() { return instance; }
    public TimeManager getTimeManager() { return timeManager; }
    public BuffManager getBuffManager() { return buffManager; }
    public ScoreboardManager getBoardManager() { return scoreboardManager; }
    public GearManager getGearManager() { return gearManager; }
    public GearEffectManager getGearEffectManager() { return gearEffectManager; }
    public ChatManager getChatManager() { return chatManager; }
    public BountyManager getBountyManager() { return bountyManager; }
    public ShopManager getShopManager() { return shopManager; }
    public KillCooldownManager getKillCooldownManager() { return killCooldownManager; }
}
