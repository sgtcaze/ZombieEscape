package net.cosmosmc.mcze.core.kits;

import lombok.AllArgsConstructor;
import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.constants.KitAction;
import net.cosmosmc.mcze.core.constants.Messages;
import net.cosmosmc.mcze.utils.Cooldowns;
import net.cosmosmc.mcze.utils.ItemStackBuilder;
import net.cosmosmc.mcze.utils.Particles;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class Decoy implements KitAction {

    private ZombieEscape plugin = JavaPlugin.getPlugin(ZombieEscape.class);

    @Override
    public void giveKit(Player player) {
        plugin.getGameArena().giveDefaults(player);
        player.getInventory().addItem(new ItemStackBuilder(Material.SLIME_BALL).withName("&aDecoy").build());
    }

    @Override
    public void interact(PlayerInteractEvent event, Player player, ItemStack itemStack) {
        if (Cooldowns.tryCooldown(player, "decoy", 1000 * 30)) {
            player.getInventory().setHelmet(null);
            new DecoyTask(player).runTaskTimer(plugin, 0, 5);
        } else {
            Messages.COOLDOWN.send(player);
        }
    }

    @AllArgsConstructor
    class DecoyTask extends BukkitRunnable {

        private Player player;
        private final long start = System.currentTimeMillis();

        @Override
        public void run() {
            if (!player.isOnline() || TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start) >= 10) {
                if (player.isOnline()) {
                    player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 5));
                }

                cancel();
                return;
            }

            Particles.spawnParticles(player.getLocation(), null, 3, "VILLAGER_HAPPY");
        }

    }

}