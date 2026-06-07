package com.pingoria.pingorialootchest.tasks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.World;

import java.util.List;

public class FallingChestTask extends BukkitRunnable {
    private final JavaPlugin plugin;
    private final ArmorStand armorStand;
    private final Location startLocation;
    private final List<ItemStack> loot;
    private final World world;
    private double currentY;
    private final double fallSpeed = 0.15;
    private boolean hasLanded = false;
    private int particleCounter = 0;

    public FallingChestTask(JavaPlugin plugin, ArmorStand armorStand, Location startLocation, List<ItemStack> loot, World world) {
        this.plugin = plugin;
        this.armorStand = armorStand;
        this.startLocation = startLocation.clone();
        this.loot = loot;
        this.world = world;
        this.currentY = startLocation.getY();
    }

    @Override
    public void run() {
        if (armorStand.isDead() || hasLanded) {
            cancel();
            return;
        }

        // Apply falling motion
        if (!hasLanded) {
            currentY -= fallSpeed;
            
            // Update position
            Location newLoc = new Location(world, startLocation.getX(), currentY, startLocation.getZ());
            armorStand.teleport(newLoc);

            // Add particles for visual effect
            particleCounter++;
            if (particleCounter % 2 == 0) {
                world.spawnParticle(Particle.DUST, newLoc.add(0, 1, 0), 5, 0.3, 0.5, 0.3);
                newLoc.subtract(0, 1, 0);
            }

            // Check collision with block
            Block blockBelow = world.getBlockAt(newLoc.clone().subtract(0, 1, 0));
            
            if (blockBelow.getType() != Material.AIR && blockBelow.getType() != Material.CAVE_AIR) {
                // Check if it's a TNT explosion or just landing on ground
                landChest(newLoc);
            }

            // Rotate armor stand for visual effect
            armorStand.setRotation(armorStand.getLocation().getYaw() + 5, armorStand.getLocation().getPitch());
        }
    }

    private void landChest(Location landingLoc) {
        hasLanded = true;

        // Play landing sound and effects
        world.playSound(landingLoc, Sound.BLOCK_CHEST_OPEN, 1.0f, 0.8f);
        world.spawnParticle(Particle.DUST, landingLoc, 15, 0.5, 0.5, 0.5);
        world.spawnParticle(Particle.CLOUD, landingLoc, 10, 0.3, 0.3, 0.3);

        // Replace with real chest
        Block chestBlock = world.getBlockAt(landingLoc);
        chestBlock.setType(Material.CHEST);

        // Fill chest with loot
        if (!loot.isEmpty()) {
            org.bukkit.block.Chest chest = (org.bukkit.block.Chest) chestBlock.getState();
            for (int i = 0; i < loot.size() && i < 27; i++) {
                chest.getBlockInventory().addItem(loot.get(i));
            }
            chest.update();
        }

        // Remove armor stand
        armorStand.remove();

        cancel();
    }
}
