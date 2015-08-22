package net.cosmosmc.mcze.utils;

import net.cosmosmc.mcze.exceptions.InventoryException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

//We can use this for inventories and what not
public class ItemUtils {
    private ItemUtils() {
    }

    public static ItemStack createItem(Material material, String name, String... lore) {
        return createItem(material, name, 1, lore);
    }

    public static ItemStack createItem(Material material, String name, int amount, String... lore) {
        return createItem(material, name, amount, (short) 0, lore);
    }

    public static ItemStack createItem(Material material, String name, int amount, short data, String... lore) {
        ItemStack item = new ItemStack(material, amount, data);
        ItemMeta meta = item.getItemMeta();

        if (name != null) {
            meta.setDisplayName(name);
        }

        if (lore != null && lore.length > 0) {
            meta.setLore(Arrays.asList(lore));
        }

        item.setItemMeta(meta);
        return item;
    }

    public static void createInventory(String name, int size) throws InventoryException {
        if (size % 9 == 0) {
            createInventory(null, name, size);
        }
    }

    public static void createInventory(InventoryHolder owner, String name, int size) throws InventoryException {
        if (size % 9 == 0) {
            Bukkit.createInventory(owner, size, name);
        } else {
            throw new InventoryException("Inventory not valid!");
        }
    }

    public static void createInventory(String name, InventoryType type) {
        createInventory(null, name, type);
    }

    public static void createInventory(InventoryHolder owner, String name, InventoryType type) {
        Bukkit.createInventory(owner, type, name);
    }


}