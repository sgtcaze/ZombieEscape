package net.cosmosmc.mcze.listeners;

import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.GameArena;
import net.cosmosmc.mcze.profiles.Profile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDeath implements Listener {

    private ZombieEscape PLUGIN;

    public PlayerDeath(ZombieEscape plugin) {
        this.PLUGIN = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        event.getDrops().clear();
        event.setDroppedExp(0);
        final Player player = event.getEntity();
        player.setHealth(20); // Do not show the respawn screen
        player.getInventory().clear();

        final GameArena gameArena = PLUGIN.getGameArena();

        if (!gameArena.isGameRunning()) {
            player.teleport(PLUGIN.getServerSpawn());
            return;
        }

        if (gameArena.isHuman(player)) {
            gameArena.addZombie(player);
            player.getInventory().setHelmet(new ItemStack(Material.JACK_O_LANTERN));
            gameArena.giveKit(player);

            if (player.getKiller() != null) {
                Profile zombieProfile = PLUGIN.getProfile(player.getKiller());
                if (zombieProfile != null) {
                    zombieProfile.setHumanKills(zombieProfile.getHumanKills() + 1);
                }
            }
        } else if (gameArena.isZombie(player)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    gameArena.teleportCheckpoint(player);
                    gameArena.giveKit(player);
                    player.setFireTicks(0);
                    player.setHealth(20);
                }
            }.runTaskLater(PLUGIN, 10);
        }

        if (gameArena.isGameRunning()) {
            if (gameArena.shouldEnd()) {
                gameArena.endGame();
            }
        }
    }

}