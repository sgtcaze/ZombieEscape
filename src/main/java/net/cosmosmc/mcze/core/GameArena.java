package net.cosmosmc.mcze.core;

import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.constants.GameState;
import net.cosmosmc.mcze.core.constants.Messages;
import net.cosmosmc.mcze.events.GameOverEvent;
import net.cosmosmc.mcze.events.GameStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

// This class represents a 'Game' instance
// there can be multiple, but we're probably
// going to use just 1
public class GameArena {

    private final ZombieEscape PLUGIN;
    private GameState gameState;
    private final VoteManager VOTE_MANAGER;

    private final int MINIMUM_PLAYERS = 2;

    private final Set<UUID> HUMANS = new HashSet<>();
    private final Set<UUID> ZOMBIES = new HashSet<>();

    public GameArena(ZombieEscape plugin) {
        this.PLUGIN = plugin;
        gameState = GameState.WAITING;
        VOTE_MANAGER = new VoteManager();
    }

    public int getStartingZombies() {
        return (int) (0.25 * Bukkit.getOnlinePlayers().size()) + 1;
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

    public boolean shouldEnd() {
        return ZOMBIES.size() == 0 || HUMANS.size() == 0;
    }

    public int getZombieSize() {
        return ZOMBIES.size();
    }

    public int getHumansSize() {
        return HUMANS.size();
    }

    public boolean isHuman(Player player) {
        return HUMANS.contains(player.getUniqueId());
    }

    public boolean isZombie(Player player) {
        return ZOMBIES.contains(player.getUniqueId());
    }

    public boolean isSameTeam(Player playerOne, Player playerTwo) {
        return isHuman(playerOne) && isHuman(playerTwo) || isZombie(playerOne) && isZombie(playerTwo);
    }

    public void purgePlayer(Player player) {
        ZOMBIES.remove(player.getUniqueId());
        HUMANS.remove(player.getUniqueId());
    }

    public void addHuman(Player player) {
        ZOMBIES.remove(player.getUniqueId());
        HUMANS.add(player.getUniqueId());
    }

    public void addZombie(Player player) {
        HUMANS.remove(player.getUniqueId());
        ZOMBIES.add(player.getUniqueId());
    }

    public void startCountdown() {
        gameState = GameState.STARTING;

        new BukkitRunnable() {
            int countdown = 30;

            @Override
            public void run() {
                if (countdown != 0) {
                    countdown--;
                } else {
                    cancel();

                    if (gameState == GameState.STARTING && isMinimumMet()) {
                        startGame();
                    } else {
                        gameState = GameState.WAITING;
                    }
                }
            }
        }.runTaskTimer(PLUGIN, 0, 20);
    }

    public void startGame() {
        gameState = GameState.RUNNING;
        Bukkit.getPluginManager().callEvent(new GameStartEvent());

        // cleanup old data, if there is any
        ZOMBIES.clear();
        HUMANS.clear();

        // We want to randomize, so we temporarily make a list to accomplish this
        List<UUID> random = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            random.add(player.getUniqueId());
        }

        // Shuffle the players
        Collections.shuffle(random);

        // Select our ZOMBIES from the random list
        for (int i = 0; i < getStartingZombies(); i++) {
            ZOMBIES.add(random.get(i));
        }

        // Sort remaining players who are not ZOMBIES
        // TODO: Add their kit here
        // TODO: Teleport here
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isZombie(player)) {
                continue;
            }

            addHuman(player);
        }

        Messages.GAME_STARTED.broadcast();
    }

    public void endGame() {
        gameState = GameState.RESTRICTED;
        Bukkit.getPluginManager().callEvent(new GameOverEvent());


        if (getHumansSize() == 0) {
            // zombies won
        } else if (getZombieSize() == 0) {
            // humans won
        }

        // run later, reset game state to waiting
    }

}