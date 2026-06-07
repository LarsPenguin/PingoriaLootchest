package com.pingoria.pingorialootchest.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ExplosionListener implements Listener {
    private final JavaPlugin plugin;

    public ExplosionListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        // This listener can be expanded for TNT-specific interactions with falling chests
        // Currently, the falling chest lands when it hits any block
    }
}
