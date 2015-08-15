package net.cosmosmc.mcze.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

//We can use this for inventories and what not
public class ItemUtils {

    public static ItemStack createItem(Material material, String name, String... lore) {
        return createItem(material, name, 1, lore);
    }

    public static ItemStack createItem(Material material, String name, int amount, String... lore) {
        return createItem(material, name, amount, (short) 0, lore);
    }

    //Feel free to modify this class if needed
    public static ItemStack createItem(Material material, String name, int amount, short data, String... lore) {
        ItemStack item = new ItemStack(material, amount, data);
        ItemMeta meta = item.getItemMeta();

        if(name != null) {
            meta.setDisplayName(name);
        }
        if(lore != null && lore.length > 0) {
            meta.setLore(Arrays.asList(lore));
        }

        item.setItemMeta(meta);
        return item;
    }

}
