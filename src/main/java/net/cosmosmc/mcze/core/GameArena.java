package net.cosmosmc.mcze.core;

import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.constants.GameState;
import net.cosmosmc.mcze.core.constants.Messages;
import net.cosmosmc.mcze.events.GameOverEvent;
import net.cosmosmc.mcze.events.GameStartEvent;
import net.cosmosmc.mcze.utils.ItemUtils;
import net.cosmosmc.mcze.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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

    private HashSet<UUID> humans = new HashSet<>();
    private HashSet<UUID> zombies = new HashSet<>();

    public GameArena(ZombieEscape plugin) {
        this.PLUGIN = plugin;
        gameState = GameState.WAITING;
        VOTE_MANAGER = new VoteManager();
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
        return zombies.size() == 0 || humans.size() == 0;
    }

    public int getZombieSize() {
        return zombies.size();
    }

    public int getHumansSize() {
        return humans.size();
    }

    public boolean isHuman(Player player) {
        return humans.contains(player.getUniqueId());
    }

    public boolean isZombie(Player player) {
        return zombies.contains(player.getUniqueId());
    }

    public void addHuman(Player player) {
        humans.add(player.getUniqueId());
        //Not sure where we're going to store these kind of locations
        //Probably just through YamlConfiguration
        //player.teleport(spawn);
        player.getInventory().addItem(ItemUtils.createEnchantedItem(Material.BOW, Utils.color("&aZombie Shooter"), Enchantment.ARROW_INFINITE, 1));
    }

    public void addZombie(Player player) {
        zombies.add(player.getUniqueId());
        //Not sure where we're going to store these kind of locations
        //Probably just through YamlConfiguration
        //player.teleport(spawn);
        player.getInventory().addItem(ItemUtils.createEnchantedItem(Material.IRON_SWORD, Utils.color("&aHuman Slicer"), Enchantment.DAMAGE_ALL, 1));
    }

    public int getStartingZombies() {
        return (int) (0.25 * Bukkit.getOnlinePlayers().size()) + 1;
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
                        // Reset
                    }
                }
            }
        }.runTaskTimer(PLUGIN, 0, 20);
    }

    public void startGame() {
        gameState = GameState.RUNNING;
        Bukkit.getPluginManager().callEvent(new GameStartEvent());

        // cleanup old data, if there is any
        zombies.clear();
        humans.clear();

        // We want to randomize, so we temporarily make a list to accomplish this
        List<UUID> random = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            random.add(player.getUniqueId());
        }

        Collections.shuffle(random);

        for (int i = 0; i < getStartingZombies(); i++) {
            zombies.add(random.get(i));
        }

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