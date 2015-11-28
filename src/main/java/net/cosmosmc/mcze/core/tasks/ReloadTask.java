package net.cosmosmc.mcze.core.tasks;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public class ReloadTask extends BukkitRunnable {

    private Player player;

    @Override
    public void run() {
        player.getInventory().addItem(new ItemStack(Material.SNOW_BALL, 6));
    }

}