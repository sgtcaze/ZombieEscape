package net.cosmosmc.mcze.commands;

import net.cosmosmc.mcze.core.constants.Messages;
import net.cosmosmc.mcze.utils.GameFile;
import net.cosmosmc.mcze.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Game implements CommandExecutor {

    /**
     * Represents a Game File to edit. We apply changes
     * here to assist map making and live debugging.
     * NOTE: Not final because this can change.
     */
    private GameFile editedFile;

    /**
     * This is the ingame /game command. This is used to create, remove, modify
     * the Game File above.
     * NOTE: We run File I/O on the MAIN thread for convenience, and to keep our
     * code concise.
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            return false;
        }

        Player player = (Player) commandSender;

        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "addspawn":
                    int spawn = editedFile.createListLocation(player, null, "Spawns");
                    Messages.CREATED_SPAWN.send(player, spawn);
                    break;
                case "checkpoint":
                    int checkpoint = editedFile.createListLocation(player, null, "Checkpoints");
                    Messages.CREATED_CHECKPOINT.send(player, checkpoint);
                    break;
                case "nukeroom":
                    editedFile.getConfig().set("Nukeroom", editedFile.serializeLocation(player.getLocation()));
                    editedFile.saveFile();
                    Messages.CREATED_NUKEROOM.send(player);
                    break;
                default:
                    sendUsage(player);
            }
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "load":
                    load(player, args[1]);
                    break;
                default:
                    sendUsage(player);
            }
        } else if (args.length == 3) {
            switch (args[0].toLowerCase()) {
                case "door":
                    doorSubCommand(player, args);
                    break;
                default:
                    sendUsage(player);
            }
        } else if (args.length == 4) {
            switch (args[0].toLowerCase()) {
                case "door":
                    doorSubCommand(player, args);
                    break;
                default:
                    sendUsage(player);
            }
        } else {
            sendUsage(player);
        }
        return false;
    }

    /**
     * Sends command usage for /game to the provided player
     *
     * @param player the player to send the usage to
     */
    private void sendUsage(Player player) {
        Messages.USAGE_GAME.send(player);
    }

    /**
     * Loads/Creates a new Game File for editing.
     * NOTE: The file is in the YAML format, and
     * should match the name of the arena.
     *
     * @param player the player who ran /game
     * @param input  the confirmation message
     */
    private void load(Player player, String input) {
        input += ".yml";
        editedFile = new GameFile("plugins/ZombieEscape/", input);
        Messages.LOADING_FILE.send(player, input);
    }

    /**
     * Check if the current Game File is null or not. Will
     * send an error message if the file isn't loaded.
     *
     * @param player the player to send the message to
     * @return if the edited file is null
     */
    private boolean isFileNull(Player player) {
        if (editedFile == null) {
            Messages.GAME_FILE_NULL.send(player);
            return true;
        }
        return false;
    }

    /**
     * Processes all subcommands of /door. Some arguments may
     * range from 3 to 4, but will never throw an exception.
     *
     * @param player the player to run the subcommand
     * @param args   the /game command arguments
     */
    private void doorSubCommand(Player player, String[] args) {
        switch (args[1].toLowerCase()) {
            case "add":
                if (!isFileNull(player)) {
                    addDoor(player, args[2]);
                }
                break;
            case "timer":
                if (!isFileNull(player)) {
                    timer(player, args);
                }
                break;
            case "view":
                if (!isFileNull(player)) {
                    viewFile(player, args[2]);
                }
                break;
            case "delete":
                if (!isFileNull(player)) {
                    deleteDoor(player, args[2]);
                }
                break;
            case "edge":
                if (!isFileNull(player)) {
                    doorEdge(player, args[2], args[3]);
                }
                break;
        }
    }

    /**
     * Creates a door with a given time in seconds.
     *
     * @param player the player who is setting the arena up
     * @param input  the time, in seconds, the door will take to open
     */
    private void addDoor(Player player, String input) {
        Block block = player.getEyeLocation().getBlock();
        Material material = block.getType();
        if (material != Material.SIGN_POST && material != Material.WALL_SIGN) {
            Messages.BLOCK_NOT_SIGN.send(player);
            return;
        }

        int seconds = Utils.getNumber(player, input);

        if (seconds < 0) {
            Messages.BAD_SECONDS.send(player);
            return;
        }

        int signID = editedFile.createListLocation(player, block.getLocation(), "Doors");
        editedFile.getConfig().set("Doors." + signID + ".Timer", seconds);
        editedFile.saveFile();
        Messages.CREATED_SIGN.send(player, signID, seconds);
    }

    /**
     * Sets a timer for a given door.
     *
     * @param player the player who is setting the arena up
     * @param args   the time, in seconds, the door will take to open
     */
    private void timer(Player player, String[] args) {
        int id = Utils.getNumber(player, args[2]);
        int seconds = Utils.getNumber(player, args[3]);

        if (id < 0 || seconds < 0) {
            Messages.POSITIVE_VALUES.send(player);
            return;
        }

        editedFile.getConfig().set("Signs." + id + ".Timer", seconds);
        editedFile.saveFile();
    }

    /**
     * Views current information for a provided Door.
     *
     * @param player the player to view the information
     * @param input  the id of the door
     */
    private void viewFile(Player player, String input) {
        // TODO: Load file and view it
    }

    /**
     * Removes a door from the game file.
     *
     * @param player the player who is setting the arena up
     * @param input  the id of the door
     */
    private void deleteDoor(Player player, String input) {
        // TODO: Delete door
    }

    /**
     * Creates a door edge. This is a location that will be used
     * to remove/fill in the blocks.
     *
     * @param player  the player who is setting the arena up
     * @param inputId the id of the door to modify
     * @param corner  the corner, 1 or 2
     */
    private void doorEdge(Player player, String inputId, String corner) {
        int id = Utils.getNumber(player, inputId);
        int input = Utils.getNumber(player, corner);

        if (id < 0) {
            Messages.POSITIVE_VALUES.send(player);
            return;
        }

        if (input < 1 || input > 2) {
            Messages.DOOR_EDGE_BAD.send(player);
            return;
        }

        Location lookingAt = player.getEyeLocation().getBlock().getLocation();
        editedFile.getConfig().set("Doors." + id + ".Edge" + input, editedFile.serializeLocation(lookingAt));
        editedFile.saveFile();
        Messages.ADDED_CORNER.send(player, corner);
    }

}