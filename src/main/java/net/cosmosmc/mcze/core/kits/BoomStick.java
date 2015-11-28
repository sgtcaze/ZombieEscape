package net.cosmosmc.mcze.core.kits;

import lombok.AllArgsConstructor;
import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.constants.KitAction;
import net.cosmosmc.mcze.core.constants.Messages;
import net.cosmosmc.mcze.utils.Cooldowns;
import net.cosmosmc.mcze.utils.ItemStackBuilder;
import net.cosmosmc.mcze.utils.Particles;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class BoomStick implements KitAction {

    private ZombieEscape plugin = JavaPlugin.getPlugin(ZombieEscape.class);

    @Override
    public void giveKit(Player player) {
        plugin.getGameArena().giveDefaults(player);
        player.getInventory().addItem(new ItemStackBuilder(Material.BLAZE_ROD).withName("&bBoomStick").build());
    }

    @Override
    public void interact(PlayerInteractEvent event, Player player, ItemStack itemStack) {
        if (Cooldowns.tryCooldown(player, "boomstick", 1000 * 30)) {
            new BoomStickTask(player.getLocation().add(0, 2, 0), player.getEyeLocation()).runTaskTimer(plugin, 0, 5);
        } else {
            Messages.COOLDOWN.send(player);
        }
    }

    @AllArgsConstructor
    class BoomStickTask extends BukkitRunnable {

        private Location startAt;
        private Location eyeLocation;
        private final long start = System.currentTimeMillis();

        @Override
        public void run() {
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start) >= 10) {
                cancel();
                return;
            }

            startAt.getWorld().spawnArrow(startAt, eyeLocation.getDirection().multiply(2), 1.2f, 12f);
            Particles.spawnParticles(startAt, null, 1, "VILLAGER_ANGRY");
        }

    }

}