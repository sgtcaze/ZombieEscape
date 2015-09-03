package net.cosmosmc.mcze.guis;

import net.cosmosmc.mcze.utils.ItemStackBuilder;
import net.cosmosmc.mcze.utils.menus.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HumanKitMenu extends Menu {

    public HumanKitMenu(String title, int size) {
        super(title, size);

        getInventory().addItem(new ItemStackBuilder(Material.SKULL_ITEM).withData(3).withName("&6&lExample Kit").build());
    }

    @Override
    public void click(Player player, ItemStack itemStack) {
        String itemName = getFriendlyName(itemStack);

        if (itemName == null) {
            return;
        }

        switch (itemName) {
            case "Example Kit":
                // TODO equip kit.
        }
    }

}