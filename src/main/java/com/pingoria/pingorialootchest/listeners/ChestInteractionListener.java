package com.pingoria.pingorialootchest.listeners;

import com.pingoria.pingorialootchest.PingoriaLootchest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ChestInteractionListener implements Listener {
    private final PingoriaLootchest plugin;

    public ChestInteractionListener(PingoriaLootchest plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }

        Inventory inventory = event.getInventory();
        String title = inventory.getHolder() != null ? "" : inventory.getTitle();

        // Check if this is a loot chest editor
        if (title.contains("Loot Chest:")) {
            String chestName = title.replace("§6Loot Chest: ", "");
            plugin.getLootChestManager().saveLootChest(chestName, inventory);
            player.sendMessage("§a✓ Loot chest saved: §e" + chestName);
        }
    }
}
