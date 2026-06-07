package com.pingoria.pingorialootchest.manager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;

public class LootChestManager {
    private final JavaPlugin plugin;
    private final Map<String, List<ItemStack>> lootChests = new HashMap<>();
    private final File dataFolder;

    public LootChestManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(plugin.getDataFolder(), "lootchests");
        
        if (!this.dataFolder.exists()) {
            this.dataFolder.mkdirs();
        }
        
        loadAllChests();
    }

    public void createLootChest(String name) {
        if (!lootChests.containsKey(name)) {
            lootChests.put(name, new ArrayList<>());
            saveLootChest(name);
        }
    }

    public Inventory getChestInventory(String name) {
        Inventory inv = Bukkit.createInventory(null, 54, "§6Loot Chest: " + name);
        
        if (lootChests.containsKey(name)) {
            List<ItemStack> items = lootChests.get(name);
            for (int i = 0; i < items.size() && i < 54; i++) {
                inv.setItem(i, items.get(i));
            }
        }
        
        return inv;
    }

    public void saveLootChest(String name, Inventory inventory) {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < 54; i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && item.getType() != Material.AIR) {
                items.add(item.clone());
            }
        }
        lootChests.put(name, items);
        saveLootChest(name);
    }

    private void saveLootChest(String name) {
        File file = new File(dataFolder, name + ".yml");
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            List<ItemStack> items = lootChests.getOrDefault(name, new ArrayList<>());
            oos.writeObject(items);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save loot chest: " + name);
            e.printStackTrace();
        }
    }

    private void loadAllChests() {
        if (!dataFolder.exists() || !dataFolder.isDirectory()) {
            return;
        }

        File[] files = dataFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".yml")) {
                    String name = file.getName().replace(".yml", "");
                    loadLootChest(name);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void loadLootChest(String name) {
        File file = new File(dataFolder, name + ".yml");
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<ItemStack> items = (List<ItemStack>) ois.readObject();
            lootChests.put(name, items);
        } catch (IOException | ClassNotFoundException e) {
            plugin.getLogger().warning("Could not load loot chest: " + name);
            e.printStackTrace();
        }
    }

    public List<String> getAllChestNames() {
        return new ArrayList<>(lootChests.keySet());
    }

    public List<ItemStack> getRandomLoot(String name) {
        if (!lootChests.containsKey(name)) {
            return new ArrayList<>();
        }

        List<ItemStack> allItems = lootChests.get(name);
        List<ItemStack> randomLoot = new ArrayList<>();
        
        Random random = new Random();
        int lootCount = Math.max(1, random.nextInt(Math.min(5, allItems.size())));
        
        for (int i = 0; i < lootCount && !allItems.isEmpty(); i++) {
            int index = random.nextInt(allItems.size());
            randomLoot.add(allItems.get(index).clone());
        }
        
        return randomLoot;
    }

    public boolean chestExists(String name) {
        return lootChests.containsKey(name);
    }

    public void deleteChest(String name) {
        lootChests.remove(name);
        File file = new File(dataFolder, name + ".yml");
        if (file.exists()) {
            file.delete();
        }
    }
}
