package net.cosmosmc.mcze.core;

import lombok.Getter;
import lombok.Setter;
import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.constants.Achievements;
import net.cosmosmc.mcze.core.constants.GameState;
import net.cosmosmc.mcze.core.constants.KitType;
import net.cosmosmc.mcze.core.constants.Messages;
import net.cosmosmc.mcze.core.data.Checkpoint;
import net.cosmosmc.mcze.core.data.Door;
import net.cosmosmc.mcze.core.tasks.CheckPointTask;
import net.cosmosmc.mcze.core.tasks.GameResetTask;
import net.cosmosmc.mcze.core.tasks.NukeRoomTask;
import net.cosmosmc.mcze.api.events.GameOverEvent;
import net.cosmosmc.mcze.api.events.GameStartEvent;
import net.cosmosmc.mcze.profiles.Profile;
import net.cosmosmc.mcze.utils.GameFile;
import net.cosmosmc.mcze.utils.ItemStackBuilder;
import net.cosmosmc.mcze.utils.Utils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class GameArena {

    /**
     * The Game File associated with this
     * Game Arena class. This should only
     * be loaded after a map vote.
     */
    @Getter
    private GameFile gameFile;

    /**
     * Checks for player proximity to checkpoints
     */
    private CheckPointTask checkPointTask;

    /**
     * Checks for the nukeroom
     */
    @Setter
    private NukeRoomTask nukeRoomTask;

    /**
     * Represents the active arena world
     */
    @Getter
    private World arenaWorld;

    /**
     * The Global minimum players for this
     * game instance.
     */
    private final static int MINIMUM_PLAYERS = 5;

    /**
     * How many times the provided game has
     * played the game map. NOTE: We have 3
     * rounds before we switch map rotation.
     */
    @Getter
    @Setter
    private int round = 1;

    /**
     * The current state of the game.
     */
    @Getter
    @Setter
    private GameState gameState = GameState.WAITING;

    @Getter
    @Setter
    private Location nukeRoom;

    @Getter
    @Setter
    private String mapSelection = null;

    private final ZombieEscape PLUGIN;
    private final VoteManager VOTE_MANAGER;

    /**
     * Stores all players considered 'Humans' when
     * the game is active.
     */
    private HashSet<UUID> humans = new HashSet<>();

    /**
     * Stores all players considered 'Zombies' when
     * the game is active.
     */
    private HashSet<UUID> zombies = new HashSet<>();

    /**
     * Stores all the spawns the players will teleport
     * to during game start.
     */
    private ArrayList<Location> spawns = new ArrayList<>();

    /**
     * Stores all the doors the players can activate at
     * a later time.
     */
    @Getter
    private ArrayList<Door> doors = new ArrayList<>();

    /**
     * Stores all of our checkpoints
     */
    @Getter
    private ArrayList<Checkpoint> checkpoints = new ArrayList<>();

    public GameArena(ZombieEscape plugin) {
        this.PLUGIN = plugin;
        VOTE_MANAGER = new VoteManager();

        File[] arenas = new File(plugin.getConfiguration().getSettingsConfig().getString("ArenasPath")).listFiles();

        if (arenas != null) {
            for (File arenaFile : arenas) {
                VOTE_MANAGER.addMap(arenaFile.getName().split(".yml")[0]);
            }
        }
    }

    public VoteManager getVoteManager() {
        return VOTE_MANAGER;
    }

    /**
     * Computes the number of Zombies per Humans
     *
     * @return the amount of Zombies to make
     */
    public int getStartingZombies() {
        return (int) (0.15 * Bukkit.getOnlinePlayers().size() + 1);
    }

    /**
     * Determines if the minimum number of players
     * are met for the game state to change.
     *
     * @return if the minimum requirement is met
     */
    public boolean isMinimumMet() {
        return Bukkit.getOnlinePlayers().size() >= MINIMUM_PLAYERS;
    }

    /**
     * Determines if the game is active
     *
     * @return if the game state is running
     */
    public boolean isGameRunning() {
        switch (gameState) {
            case RUNNING:
            case NUKEROOM:
                return true;
            default:
                return false;
        }
    }

    /**
     * Determines if the game should start
     *
     * @return if the state and minium are met
     */
    public boolean shouldStart() {
        return gameState == GameState.WAITING && isMinimumMet();
    }

    /**
     * Determines if the game should end
     *
     * @return true if the humans or zombies size is 0
     */
    public boolean shouldEnd() {
        return (zombies.size() == 0 || humans.size() == 0) && isGameRunning();
    }

    /**
     * Determines the number of Zombies in the game
     *
     * @return the number of Zombies
     */
    public int getZombieSize() {
        return zombies.size();
    }

    /**
     * Determines the number of Humans in the game
     *
     * @return the number of Humans
     */
    public int getHumansSize() {
        return humans.size();
    }

    /**
     * Determines if the provided player is a Human
     *
     * @param player the player to check
     * @return if the player is a Human
     */
    public boolean isHuman(Player player) {
        return humans.contains(player.getUniqueId());
    }

    /**
     * Determines if the provided player is a Zombie
     *
     * @param player the player to check
     * @return if the player is a Zombie
     */
    public boolean isZombie(Player player) {
        return zombies.contains(player.getUniqueId());
    }

    /**
     * Determines if two players belong to the same team
     *
     * @param playerOne the first player to check
     * @param playerTwo the second player to check
     * @return if the players belong to the same team
     */
    public boolean isSameTeam(Player playerOne, Player playerTwo) {
        return isHuman(playerOne) && isHuman(playerTwo) || isZombie(playerOne) && isZombie(playerTwo);
    }

    /**
     * Removes the player from the current ADTs.
     *
     * @param player the player to remove
     */
    public void purgePlayer(Player player) {
        zombies.remove(player.getUniqueId());
        humans.remove(player.getUniqueId());
    }

    /**
     * Adds a player to the Human team
     *
     * @param player the player to add
     */
    public void addHuman(Player player) {
        zombies.remove(player.getUniqueId());
        humans.add(player.getUniqueId());
    }

    /**
     * Adds a player to the Zombies team
     *
     * @param player the player to add
     */
    public void addZombie(Player player) {
        humans.remove(player.getUniqueId());
        zombies.add(player.getUniqueId());

        // TODO: Modulairze
        player.setHealth(20D);
        player.setFireTicks(0);

        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
    }

    public boolean isNotPlaying(Player player) {
        return !humans.contains(player.getUniqueId()) && !zombies.contains(player.getUniqueId());
    }

    public void broadcast(String message, boolean humanMessage) {
        if (humanMessage) {
            send(humans, message);
        } else {
            send(zombies, message);
        }
    }

    private void send(HashSet<UUID> members, String message) {
        for (UUID member : members) {
            Player player = Bukkit.getPlayer(member);

            if (player != null) {
                player.sendMessage(message);
            }
        }
    }

    public void giveDefaults(Player player) {
        if (isHuman(player)) {
            ItemStack object = new ItemStack(Material.POTION);
            Potion potion = new Potion(1);
            potion.setType(PotionType.SPEED);
            potion.setLevel(1);
            potion.setSplash(true);
            potion.apply(object);

            PlayerInventory playerInventory = player.getInventory();
            playerInventory.clear();
            playerInventory.setArmorContents(null);
            playerInventory.addItem(new ItemStackBuilder(Material.BOW).withEnchantment(Enchantment.ARROW_KNOCKBACK).build());
            playerInventory.addItem(new ItemStack(Material.SNOW_BALL, 6));
            playerInventory.addItem(new ItemStack(Material.EGG, 5));
            playerInventory.addItem(new ItemStack(object));
            playerInventory.setItem(8, new ItemStack(Material.ARROW, 8));
        } else {
            PlayerInventory playerInventory = player.getInventory();
            playerInventory.clear();
            playerInventory.setArmorContents(null);
            // playerInventory.setHelmet(new ItemStack(Material.WOOL, 1, (short) 5));
            playerInventory.setHelmet(new ItemStack(Material.JACK_O_LANTERN));
        }
    }

    /**
     * Teleports a player to a checkpoint if they
     * are activated. We reverse the search so
     * we don't teleport them to multiple checkpoints
     *
     * @param player the player to teleport
     */
    public void teleportCheckpoint(Player player) {
        for (int index = checkpoints.size() - 1; index >= 0; index--) {
            Checkpoint checkpoint = checkpoints.get(index);
            if (checkpoint.isActivated()) {
                player.teleport(checkpoint.getLocation());
                return;
            }
        }

        player.teleport(spawns.get(0)); // Otherwise teleport to spawn
    }

    /**
     * Gives a kit to a player
     *
     * @param player the player to give the kit to
     */
    public void giveKit(Player player) {
        Profile profile = PLUGIN.getProfile(player);
        KitType kitType = isHuman(player) ? profile.getHumanKit() : profile.getZombieKit();

        if (kitType == null) {
            kitType = isHuman(player) ? KitType.ARCHER : KitType.MILKMAN;
        }

        kitType.getKitAction().giveKit(player);
    }

    /**
     * Resets the current round by teleporting everyone
     * back to their spawns and beginning the state over.
     */
    public void resetRound() {
        this.handleStart();
    }

    /**
     * Starts a countdown for the current game arena.
     */
    public void startCountdown() {
        gameState = GameState.STARTING;

        new BukkitRunnable() {
            int countdown = 30;

            @Override
            public void run() {
                if (countdown != 0) {
                    if (countdown == 30 || countdown == 20 || countdown == 10 || countdown <= 5 && countdown > 0) {
                        Bukkit.broadcastMessage(ChatColor.YELLOW + "Game will start in " + ChatColor.RED + countdown + ChatColor.YELLOW + " seconds");
                    }

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

    /**
     * Initializes the game state, and sets up defaults
     */
    public void startGame() {
        Bukkit.getPluginManager().callEvent(new GameStartEvent());

        final String MAPS_PATH = PLUGIN.getConfiguration().getSettingsConfig().getString("MapsPath");
        final String ARENAS_PATH = PLUGIN.getConfiguration().getSettingsConfig().getString("ArenasPath");

        this.mapSelection = VOTE_MANAGER.getWinningMap();
        this.gameFile = new GameFile(ARENAS_PATH, mapSelection + ".yml");
        this.arenaWorld = Bukkit.createWorld(new WorldCreator(MAPS_PATH + gameFile.getConfig().getString("World")));
        this.arenaWorld.setSpawnFlags(false, false);
        this.arenaWorld.setGameRuleValue("doMobSpawning", "false");
        this.spawns = gameFile.getLocations(arenaWorld, "Spawns");
        this.doors = gameFile.getDoors(arenaWorld);
        this.checkpoints = gameFile.getCheckpoints(arenaWorld);
        this.nukeRoom = gameFile.getLocation(arenaWorld, "Nukeroom");
        this.handleStart();

        VOTE_MANAGER.resetVotes();

        Bukkit.getConsoleSender().sendMessage(Utils.color("&6Start game method"));

        // Clear entities
        for (Entity entity : arenaWorld.getEntities()) {
            if (!(entity instanceof Player) && (entity instanceof Animals || entity instanceof Monster)) {
                entity.remove();
            }
        }
    }

    /**
     * Ends the game state by resetting the game state, clearing doors,
     * and resuming later.
     */
    public void endGame() {
        if (!isGameRunning()) {
            Bukkit.getConsoleSender().sendMessage(Utils.color("&6Fired end game (not running)"));
            return;
        }

        Bukkit.getConsoleSender().sendMessage(Utils.color("&6Fired end game"));

        Bukkit.getPluginManager().callEvent(new GameOverEvent());

        for (Door door : doors) {
            door.cleanup();
        }

        if (checkPointTask != null) {
            checkPointTask.cancel();
            checkPointTask = null;
        }

        if (nukeRoomTask != null) {
            nukeRoomTask.cancel();
            nukeRoomTask = null;
        }

        boolean humansWon = getHumansSize() != 0;

        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = PLUGIN.getProfile(player);

            if (profile != null) {
                profile.setGamesPlayed(profile.getGamesPlayed() + 1);
                if (profile.getGamesPlayed() == 100) {
                    profile.awardAchievement(Achievements.LONG_TIME_PLAYER);
                }

                if (humansWon && isHuman(player)) {
                    profile.setWins(profile.getWins() + 1);
                }
            }
        }

        if (humansWon) {
            Messages.WIN_HUMANS.broadcast();
        } else {
            Messages.WIN_ZOMBIES.broadcast();
        }

        new GameResetTask(PLUGIN, this).runTaskLater(PLUGIN, 20 * 5);
    }

    /**
     * Clears old data, and sets up new data. This is run in both
     * the startGame() at round 0. This is also called during the
     * reset round.
     */
    private void handleStart() {
        if (!isMinimumMet()) {
            Bukkit.getConsoleSender().sendMessage(Utils.color("&6Fired handle start (min not met)"));
            gameState = GameState.WAITING;
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                player.teleport(PLUGIN.getServerSpawn());
            }
            return;
        }

        gameState = GameState.STARTING;

        Bukkit.getConsoleSender().sendMessage(Utils.color("&6Fired handle start"));

        Messages.BANTER.broadcast(round);
        String creator = gameFile.getConfig().getString("Creator");
        Bukkit.broadcastMessage(Utils.color("&aYou are playing &2&l" + mapSelection.replace("_", " ") + " &aby &2&l" + creator + "&a."));

        // cleanup old data, if there is any
        zombies.clear();
        humans.clear();

        if (checkPointTask != null) {
            checkPointTask.cancel();
        }

        checkPointTask = new CheckPointTask(this, checkpoints);
        checkPointTask.runTaskTimer(PLUGIN, 0, 20);

        final ItemStack HUMAN_KIT_SELECTOR = new ItemStackBuilder(Material.SKULL_ITEM).withData(3).withName("&fHuman Kits").build();
        final ItemStack ZOMBIE_KIT_SELECTOR = new ItemStackBuilder(Material.SKULL_ITEM).withData(2).withName("&aZombie Kits").build();

        int index = 0;

        // Sort remaining players who are not zombies
        for (Player player : Bukkit.getOnlinePlayers()) {
            // This is to make sure we do not hit
            // an Exception.
            if (index == spawns.size()) {
                index = 0;
            }

            addHuman(player);
            player.teleport(spawns.get(index++));
            player.getInventory().setArmorContents(null);
            player.getInventory().clear();
            player.setFireTicks(0);
            player.setHealth(20);
            player.getInventory().setItem(3, HUMAN_KIT_SELECTOR);
            player.getInventory().setItem(5, ZOMBIE_KIT_SELECTOR);
        }

        for (Checkpoint checkpoint : checkpoints) {
            checkpoint.create(PLUGIN);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (gameState == GameState.STARTING) {
                    selectZombiesLater();
                    gameState = GameState.RUNNING;
                } else {
                    Bukkit.getConsoleSender().sendMessage(Utils.color("&6Fired handle start (game not running)"));
                }
            }
        }.runTaskLater(PLUGIN, 20 * 15);
    }

    private void selectZombiesLater() {
        Bukkit.getConsoleSender().sendMessage(Utils.color("&6Firing select zombies"));
        // We want to randomize, so we temporarily make a list to accomplish this
        List<UUID> random = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            random.add(player.getUniqueId());
        }

        // Shuffle the players
        Collections.shuffle(random);
        int index = 0;
        // Select our zombies from the random list
        for (int i = 0; i < getStartingZombies(); i++) {
            if (index == spawns.size()) {
                index = 0;
            }

            UUID uuid = random.get(i);
            zombies.add(uuid);
            humans.remove(uuid);
            Player player = Bukkit.getPlayer(uuid);
            player.teleport(spawns.get(index++));
            Messages.STARTING_ZOMBIE.broadcast(player.getName());
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().clear();
            player.closeInventory();
            giveKit(player);

            if (isZombie(player)) {
                Messages.YOU_ARE_A_ZOMBIE.send(player);
                continue;
            }

            Messages.YOU_ARE_A_HUMAN.send(player);
        }
    }

}