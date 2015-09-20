package net.cosmosmc.mcze.core.constants;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public interface KitAction {

    void giveKit(Player player);

    void interact(PlayerInteractEvent event, ItemStack itemStack);

}