package net.cosmosmc.mcze.core.kits;

import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.constants.KitAction;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class MilkMan implements KitAction {

    private ZombieEscape plugin = JavaPlugin.getPlugin(ZombieEscape.class);

    @Override
    public void giveKit(Player player) {
        plugin.getGameArena().giveDefaults(player);

        for (int amount = 0; amount < 3; amount++) {
            player.getInventory().setItem(amount, new ItemStack(Material.MILK_BUCKET));
        }
    }

    @Override
    public void interact(PlayerInteractEvent event, Player player, ItemStack itemStack) {

    }

}