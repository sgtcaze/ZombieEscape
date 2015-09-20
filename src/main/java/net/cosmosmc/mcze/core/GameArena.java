package net.cosmosmc.mcze.core;

import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.constants.Achievements;
import net.cosmosmc.mcze.core.constants.GameState;
import net.cosmosmc.mcze.core.constants.KitType;
import net.cosmosmc.mcze.core.constants.Messages;
import net.cosmosmc.mcze.events.GameOverEvent;
import net.cosmosmc.mcze.events.GameStartEvent;
import net.cosmosmc.mcze.profiles.Profile;
import net.cosmosmc.mcze.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
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

    public int getStartingZombies() {
        return (int) (0.25 * Bukkit.getOnlinePlayers().size() + 1);
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

    public boolean isSameTeam(Player playerOne, Player playerTwo) {
        return isHuman(playerOne) && isHuman(playerTwo) || isZombie(playerOne) && isZombie(playerTwo);
    }

    public void purgePlayer(Player player) {
        zombies.remove(player.getUniqueId());
        humans.remove(player.getUniqueId());
    }

    public void addHuman(Player player) {
        zombies.remove(player.getUniqueId());
        humans.add(player.getUniqueId());
    }

    public void addZombie(Player player) {
        humans.remove(player.getUniqueId());
        zombies.add(player.getUniqueId());
    }

    public void giveKit(Player player) {
        Profile profile = PLUGIN.getGameManager().getProfile(player);
        KitType kitType = isHuman(player) ? profile.getHumanKit() : profile.getZombieKit();
        kitType.getKitAction().giveKit(player);
    }

    public void startCountdown() {
        gameState = GameState.STARTING;

        new BukkitRunnable() {
            int countdown = 30;

            @Override
            public void run() {
                if (countdown != 0) {
                    countdown--;
                    Bukkit.broadcastMessage(ChatColor.GOLD + "Countdown: " + countdown);
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

        final String YOU_ARE_ZOMBIE = Utils.color("&aYou are a zombie!");
        final String YOU_ARE_HUMAN = Utils.color("&eYou are a human!");

        // cleanup old data, if there is any
        zombies.clear();
        humans.clear();

        // We want to randomize, so we temporarily make a list to accomplish this
        List<UUID> random = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            random.add(player.getUniqueId());
        }

        // Shuffle the players
        Collections.shuffle(random);

        // Select our zombies from the random list
        for (int i = 0; i < getStartingZombies(); i++) {
            zombies.add(random.get(i));
        }

        // Sort remaining players who are not zombies
        // TODO: Add their kit here
        // TODO: Teleport here
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isZombie(player)) {
                player.sendMessage(YOU_ARE_ZOMBIE);
                PLUGIN.getMenuManager().getMenu("zkits").show(player);
                continue;
            }

            player.sendMessage(YOU_ARE_HUMAN);
            PLUGIN.getMenuManager().getMenu("hkits").show(player);
            addHuman(player);
        }

        Messages.GAME_STARTED.broadcast();
    }

    public void endGame() {
        gameState = GameState.RESTRICTED;
        Bukkit.getPluginManager().callEvent(new GameOverEvent());

        Messages.GAME_ENDED.broadcast();

        // awardOnline();

        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = PLUGIN.getGameManager().getProfile(player);

            if (profile != null) {
                profile.setGamesPlayed(profile.getGamesPlayed() + 1);
                if (profile.getGamesPlayed() == 100) {
                    profile.awardAchievement(Achievements.LONG_TIME_PLAYER);
                }
            }
        }

        if (getHumansSize() == 0) {
            // zombies won
        } else if (getZombieSize() == 0) {
            // humans won
        }

        // run later, reset game state to waiting
    }

    public void awardOnline(Achievements achievement) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = PLUGIN.getGameManager().getProfile(player);

            if (profile != null) {
                profile.awardAchievement(achievement);
            }
        }
    }

}