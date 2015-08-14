package net.cosmosmc.mcze.core;

import net.cosmosmc.mcze.ZombieEscape;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class GameArena {

    private ZombieEscape plugin;
    private GameState gameState;
    private VoteManager voteManager;

    private final int MINIMUM_PLAYERS = 2;

    public GameArena(ZombieEscape plugin) {
        this.plugin = plugin;
        gameState = GameState.WAITING;
        voteManager = new VoteManager();
    }

    public boolean isMinimumMet() {
        return Bukkit.getOnlinePlayers().size() >= MINIMUM_PLAYERS;
    }

    public boolean isGameRunning() {
        return gameState == GameState.RUNNING;
    }

    public boolean shouldStart() {
        return gameState == GameState.WAITING && isMinimumMet();
    }

    public void startCountdown() {
        gameState = GameState.STARTING;

        new BukkitRunnable() {
            int countdown = 30;

            @Override
            public void run() {
                if (countdown != 0) {
                    // Do countdown
                } else {
                    cancel();

                    if (gameState == GameState.STARTING && isMinimumMet()) {
                        startGame();
                    } else {
                       // Reset
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void startGame() {

    }

    public void endGame() {

    }

}