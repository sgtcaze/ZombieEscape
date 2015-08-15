package net.cosmosmc.mcze.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

//We can use this for inventories and what not
public class ItemUtils {

    //Feel free to modify this class if needed
    public static ItemStack createItem(Material material, String name, String[] lore, int amount, short data) {
        ItemStack item = new ItemStack(material, amount, data);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);
        return item;
    }

}
