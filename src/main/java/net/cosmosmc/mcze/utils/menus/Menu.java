package net.cosmosmc.mcze.utils.menus;

import lombok.Getter;
import net.cosmosmc.mcze.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
public abstract class Menu implements InventoryHolder {

    private int size;
    protected Inventory inventory;

    public Menu(String title, int size) {
        inventory = Bukkit.createInventory(null, size, Utils.color(title));
        this.size = size;
    }

    public void show(Player player) {
        player.openInventory(inventory);
    }

    public abstract void click(Player player, ItemStack itemStack);

    protected String getFriendlyName(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta == null || !itemMeta.hasDisplayName()) {
            return null;
        }

        return ChatColor.stripColor(itemMeta.getDisplayName());
    }

}