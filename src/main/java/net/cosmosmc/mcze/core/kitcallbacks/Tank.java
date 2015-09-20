package net.cosmosmc.mcze.core.kitcallbacks;

import net.cosmosmc.mcze.core.constants.KitAction;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Tank implements KitAction {

    @Override
    public void giveKit(Player player) {
        player.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
    }

    @Override
    public void interact(PlayerInteractEvent event, ItemStack itemStack) {

    }

}