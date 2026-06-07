package com.pingoria.pingorialootchest.commands;

import com.pingoria.pingorialootchest.PingoriaLootchest;
import com.pingoria.pingorialootchest.manager.LootChestManager;
import com.pingoria.pingorialootchest.tasks.FallingChestTask;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LootChestCommand implements CommandExecutor {
    private final PingoriaLootchest plugin;

    public LootChestCommand(PingoriaLootchest plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can execute this command!");
            return true;
        }

        if (!player.hasPermission("pingorialootchest.admin")) {
            player.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        if (args.length == 0) {
            showHelp(player);
            return true;
        }

        LootChestManager manager = plugin.getLootChestManager();

        switch (args[0].toLowerCase()) {
            case "spawn" -> handleSpawn(player, args, manager);
            case "create" -> handleCreate(player, args, manager);
            case "edit" -> handleEdit(player, args, manager);
            case "list" -> handleList(player, manager);
            case "delete" -> handleDelete(player, args, manager);
            case "help" -> showHelp(player);
            default -> player.sendMessage("§cUnknown subcommand! Use /lootchest help");
        }

        return true;
    }

    private void handleSpawn(Player player, String[] args, LootChestManager manager) {
        if (args.length < 2) {
            player.sendMessage("§cUsage: /lootchest spawn <name>");
            player.sendMessage("§7Available chests: " + (manager.getAllChestNames().isEmpty() ? "§cNone" : String.join(", ", manager.getAllChestNames())));
            return;
        }

        String chestName = args[1];
        
        if (!manager.chestExists(chestName)) {
            player.sendMessage("§cLoot chest not found: §e" + chestName);
            player.sendMessage("§7Use /lootchest list to see available chests");
            return;
        }

        List<ItemStack> loot = manager.getRandomLoot(chestName);

        if (loot.isEmpty()) {
            player.sendMessage("§cThe loot chest §e" + chestName + " §cis empty!");
            return;
        }

        // Create falling chest 50 blocks above
        Location spawnLoc = player.getLocation().add(0, 50, 0);
        spawnLoc.setY(spawnLoc.getY());

        // Create visual armor stand
        ArmorStand armorStand = player.getWorld().spawn(spawnLoc, ArmorStand.class, as -> {
            as.setVisible(false);
            as.setArms(false);
            as.setBasePlate(false);
            as.setGravity(false);
            as.getEquipment().setHelmet(new ItemStack(Material.CHEST));
        });

        // Create falling chest entity
        FallingChestTask task = new FallingChestTask(plugin, armorStand, spawnLoc, loot, player.getWorld());
        task.runTaskTimer(plugin, 0, 1);

        player.sendMessage("§a✓ Loot chest §e" + chestName + " §aspawned 50 blocks above you!");
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
    }

    private void handleCreate(Player player, String[] args, LootChestManager manager) {
        if (args.length < 2) {
            player.sendMessage("§cUsage: /lootchest create <name>");
            return;
        }

        String name = args[1];
        
        if (manager.chestExists(name)) {
            player.sendMessage("§cA loot chest with that name already exists!");
            return;
        }

        manager.createLootChest(name);
        
        player.openInventory(manager.getChestInventory(name));
        player.sendMessage("§a✓ Loot chest created: §e" + name);
        player.sendMessage("§7Add items to the inventory and close it to save!");
    }

    private void handleEdit(Player player, String[] args, LootChestManager manager) {
        if (args.length < 2) {
            player.sendMessage("§cUsage: /lootchest edit <name>");
            return;
        }

        String name = args[1];

        if (!manager.chestExists(name)) {
            player.sendMessage("§cLoot chest not found: §e" + name);
            return;
        }

        player.openInventory(manager.getChestInventory(name));
        player.sendMessage("§a✓ Editing loot chest: §e" + name);
    }

    private void handleList(Player player, LootChestManager manager) {
        List<String> chests = manager.getAllChestNames();
        
        if (chests.isEmpty()) {
            player.sendMessage("§cNo loot chests exist yet!");
            return;
        }

        player.sendMessage("§6========== Loot Chests ==========");
        for (String chest : chests) {
            player.sendMessage("§a• §e" + chest);
        }
        player.sendMessage("§6==================================");
    }

    private void handleDelete(Player player, String[] args, LootChestManager manager) {
        if (args.length < 2) {
            player.sendMessage("§cUsage: /lootchest delete <name>");
            return;
        }

        String name = args[1];

        if (!manager.chestExists(name)) {
            player.sendMessage("§cLoot chest not found: §e" + name);
            return;
        }

        manager.deleteChest(name);
        player.sendMessage("§a✓ Loot chest deleted: §e" + name);
    }

    private void showHelp(Player player) {
        player.sendMessage("§6========== PingoriaLootchest Help ==========");
        player.sendMessage("§e/lootchest spawn <name> §7- Spawn a specific loot chest");
        player.sendMessage("§e/lootchest create <name> §7- Create a new loot chest");
        player.sendMessage("§e/lootchest edit <name> §7- Edit a loot chest");
        player.sendMessage("§e/lootchest list §7- List all loot chests");
        player.sendMessage("§e/lootchest delete <name> §7- Delete a loot chest");
        player.sendMessage("§6============================================");
    }
}
