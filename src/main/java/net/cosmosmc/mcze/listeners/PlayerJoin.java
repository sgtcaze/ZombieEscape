package net.cosmosmc.mcze.listeners;

import lombok.AllArgsConstructor;
import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.GameArena;
import net.cosmosmc.mcze.core.constants.GameState;
import net.cosmosmc.mcze.core.constants.Messages;
import net.cosmosmc.mcze.profiles.Profile;
import net.cosmosmc.mcze.profiles.ProfileLoader;
import net.cosmosmc.mcze.utils.ItemStackBuilder;
import net.cosmosmc.mcze.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public class PlayerJoin implements Listener {

    private ZombieEscape plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        final GameArena gameArena = plugin.getGameArena();
        final Player player = event.getPlayer();

        player.sendMessage(Utils.color("&aWelcome to Zombie Escape!"));
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        if (gameArena.isGameRunning()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (gameArena.getZombieSize() != 0) {
                        gameArena.addZombie(player);
                        gameArena.teleportCheckpoint(player);
                        Messages.JOINED_INFECTED.broadcast(player.getName());
                    }
                }
            }.runTaskLater(plugin, 10);
        } else {
            if (gameArena.shouldStart()) {
                gameArena.startCountdown();
            }

            //if (gameArena.getGameState() == GameState.STARTING) {
            //    gameArena.teleportCheckpoint(player);
           // } else {
                teleport(player, plugin.getServerSpawn());
           // }
        }

        final Profile PROFILE = new Profile(event.getPlayer());
        plugin.getProfiles().put(event.getPlayer().getUniqueId(), PROFILE);
        new ProfileLoader(PROFILE, plugin).runTaskAsynchronously(plugin);
    }

    private void teleport(final Player player, final Location location) {
        new BukkitRunnable() {
            @Override
            public void run() {
                player.getInventory().addItem(new ItemStackBuilder(Material.BOOK).withName("&aVote").build());
                player.teleport(location);
            }
        }.runTaskLater(plugin, 10);
    }

}