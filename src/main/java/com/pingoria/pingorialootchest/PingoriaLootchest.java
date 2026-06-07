package com.pingoria.pingorialootchest;

import com.pingoria.pingorialootchest.commands.LootChestCommand;
import com.pingoria.pingorialootchest.listeners.ChestInteractionListener;
import com.pingoria.pingorialootchest.listeners.ExplosionListener;
import com.pingoria.pingorialootchest.manager.LootChestManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PingoriaLootchest extends JavaPlugin {

    private static PingoriaLootchest instance;
    private LootChestManager lootChestManager;

    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize manager
        lootChestManager = new LootChestManager(this);
        
        // Register commands
        getCommand("lootchest").setExecutor(new LootChestCommand(this));
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new ChestInteractionListener(this), this);
        getServer().getPluginManager().registerEvents(new ExplosionListener(this), this);
        
        getLogger().info("✓ PingoriaLootchest plugin has been enabled!");
        getLogger().info("✓ Use /lootchest help for commands");
    }

    @Override
    public void onDisable() {
        getLogger().info("✗ PingoriaLootchest plugin has been disabled!");
    }

    public static PingoriaLootchest getInstance() {
        return instance;
    }

    public LootChestManager getLootChestManager() {
        return lootChestManager;
    }
}
