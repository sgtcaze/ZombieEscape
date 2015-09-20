package net.cosmosmc.mcze.core.kitcallbacks;

import net.cosmosmc.mcze.core.constants.KitAction;
import net.cosmosmc.mcze.utils.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Leaper implements KitAction {

    @Override
    public void giveKit(Player player) {
        player.getInventory().addItem(new ItemStackBuilder(Material.CARROT_ITEM).withName("&bKarrot").build());
    }

    @Override
    public void interact(PlayerInteractEvent event, ItemStack itemStack) {
        // if time has passed, then allow
        // otherwise, cancel
    }

}