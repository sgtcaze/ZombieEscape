package net.cosmosmc.mcze.core.tasks;

import lombok.AllArgsConstructor;
import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.GameArena;
import net.cosmosmc.mcze.core.constants.GameState;
import net.cosmosmc.mcze.core.constants.Messages;
import net.cosmosmc.mcze.utils.ItemStackBuilder;
import net.cosmosmc.mcze.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public class GameResetTask extends BukkitRunnable {

    private ZombieEscape plugin;
    private GameArena gameArena;

    @Override
    public void run() {
        gameArena.setRound(gameArena.getRound() + 1);
        if (gameArena.getRound() == 4) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.teleport(plugin.getServerSpawn());
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                player.getInventory().addItem(new ItemStackBuilder(Material.BOOK).withName("&aVote").build());
            }

            gameArena.setRound(1);
            gameArena.setGameState(GameState.RESETTING);
            Bukkit.getConsoleSender().sendMessage(Utils.color("&6Game reset task, resetting"));

            Messages.MATCH_OVER.broadcast();

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (gameArena.getArenaWorld() != null) {
                        Bukkit.unloadWorld(gameArena.getArenaWorld(), false);
                    }

                    gameArena.setGameState(GameState.WAITING);
                    if (gameArena.shouldStart()) {
                        gameArena.startCountdown();
                    } else {
                        Bukkit.getConsoleSender().sendMessage(Utils.color("&6Game reset task, waiting (shouldn't start)"));
                    }
                }
            }.runTaskLater(plugin, 20 * 15);
        } else {
            gameArena.resetRound();
            Bukkit.getConsoleSender().sendMessage(Utils.color("&6Fired resetRound"));
        }
    }

}