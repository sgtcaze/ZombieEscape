package net.cosmosmc.mcze.menus;

import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.constants.KitType;
import net.cosmosmc.mcze.core.constants.Messages;
import net.cosmosmc.mcze.utils.ItemStackBuilder;
import net.cosmosmc.mcze.utils.Utils;
import net.cosmosmc.mcze.utils.menus.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ZombieKitMenu extends Menu {

    private ZombieEscape plugin;

    public ZombieKitMenu(String title, int size, ZombieEscape plugin) {
        super(title, size);
        this.plugin = plugin;
        inventory.setItem(2, new ItemStackBuilder(Material.SLIME_BALL).withName("&a&lMilkMan").withLore("&fGet &6x3 &fMilk Buckets").build());
        inventory.setItem(4, new ItemStackBuilder(Material.CARROT_STICK).withName("&a&lLeaper").withLore("&fLaunch towards your enemies").build());
        inventory.setItem(6, new ItemStackBuilder(Material.SKULL_ITEM).withData(3).withName("&a&lDecoy").withLore("&fDisguise as a human temporarily").build());
    }

    @Override
    public void click(Player player, ItemStack itemStack) {
        String itemName = getFriendlyName(itemStack);

        if (itemName == null) {
            return;
        }

        for (KitType kitType : KitType.values()) {
            if (kitType.getName().equalsIgnoreCase(itemName)) {
                plugin.getProfile(player).setZombieKit(kitType);
                Messages.UPDATED_KIT.send(player, itemName, Utils.color("&aZombie"));
                break;
            }
        }
    }

}