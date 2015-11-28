package net.cosmosmc.mcze.core.kits;

import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.constants.KitAction;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Archer implements KitAction {

    private ZombieEscape plugin = JavaPlugin.getPlugin(ZombieEscape.class);

    @Override
    public void giveKit(Player player) {
        plugin.getGameArena().giveDefaults(player);
        player.getInventory().setItem(8, new ItemStack(Material.ARROW, 16));
    }

    @Override
    public void interact(PlayerInteractEvent event, Player player, ItemStack itemStack) {

    }

}