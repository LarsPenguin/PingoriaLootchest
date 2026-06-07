package com.pingoria.pingorialootchest.entity;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import java.util.List;
import java.util.UUID;

public class FallingLootChest {
    private final UUID entityId;
    private final Location startLocation;
    private final List<ItemStack> loot;
    private ArmorStand visualEntity;
    private double currentY;
    private boolean hasLanded;
    private final double fallSpeed = 0.1;
    private final double minHeight;

    public FallingLootChest(UUID entityId, Location startLocation, List<ItemStack> loot) {
        this.entityId = entityId;
        this.startLocation = startLocation.clone();
        this.loot = loot;
        this.currentY = startLocation.getY();
        this.minHeight = startLocation.getY();
        this.hasLanded = false;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public Location getStartLocation() {
        return startLocation.clone();
    }

    public List<ItemStack> getLoot() {
        return loot;
    }

    public ArmorStand getVisualEntity() {
        return visualEntity;
    }

    public void setVisualEntity(ArmorStand visualEntity) {
        this.visualEntity = visualEntity;
    }

    public double getCurrentY() {
        return currentY;
    }

    public void setCurrentY(double y) {
        this.currentY = y;
    }

    public boolean hasLanded() {
        return hasLanded;
    }

    public void setHasLanded(boolean landed) {
        this.hasLanded = landed;
    }

    public double getFallSpeed() {
        return fallSpeed;
    }

    public double getMinHeight() {
        return minHeight;
    }

    public void updateVisualPosition(Location newLocation) {
        if (visualEntity != null && !visualEntity.isDead()) {
            visualEntity.teleport(newLocation);
            // Add rotation for visual effect
            visualEntity.setHeadPose(new EulerAngle(
                visualEntity.getHeadPose().getX() + 0.05,
                visualEntity.getHeadPose().getY() + 0.05,
                visualEntity.getHeadPose().getZ()
            ));
        }
    }
}
