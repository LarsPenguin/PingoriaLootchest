package com.pingoria.pingorialootchest;

import org.bukkit.plugin.java.JavaPlugin;

public class PingoriaLootchest extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("PingoriaLootchest plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("PingoriaLootchest plugin has been disabled!");
    }
}
