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

public class HumanKitMenu extends Menu {

    private ZombieEscape plugin;

    public HumanKitMenu(String title, int size, ZombieEscape plugin) {
        super(title, size);
        this.plugin = plugin;
        inventory.setItem(2, new ItemStackBuilder(Material.DIAMOND_CHESTPLATE).withName("&a&lFortify").withLore("&fTake 3 hits to infect").build());
        inventory.setItem(4, new ItemStackBuilder(Material.BOW).withName("&a&lArcher").withLore("&fRecieve &6x2 &fArrows").build());
        inventory.setItem(6, new ItemStackBuilder(Material.STICK).withName("&a&lBoomStick").withLore("&fCreate a temporary arrow turret").build());
    }

    @Override
    public void click(Player player, ItemStack itemStack) {
        String itemName = getFriendlyName(itemStack);

        if (itemName == null) {
            return;
        }

        for (KitType kitType : KitType.values()) {
            if (kitType.getName().equalsIgnoreCase(itemName)) {
                plugin.getProfile(player).setHumanKit(kitType);
                Messages.UPDATED_KIT.send(player, itemName, Utils.color("&fHuman"));
                break;
            }
        }
    }

}