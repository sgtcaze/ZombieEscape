package net.cosmosmc.mcze.core.constants;

import net.cosmosmc.mcze.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// TODO: Work on locale support
public enum Messages {

    // Game Creation Messages
    ADDED_CORNER("&cAdded door edge %s"),
    BANTER("&bSomeone forgot to wipe their nose, now look where we're at! &c&lZOMBIES&b! Round &c(%s/3)&b. You will get your kit in 10 seconds."),
    BAD_INPUT("&c%s is not a number."),
    BAD_SECONDS("&cThe seconds must be positive."),
    BLOCK_NOT_SIGN("&cThe target block is not a sign, and cannot be set."),
    CLASS_INFORMATION("&bZombie Escape: &9Class Information" +
            "\n&cMain Class: &f%s" +
            "\n&cZombie Class: &a%s"),
    COOLDOWN("&cPlease wait for the cooldown to finish."),
    CHECKPOINT_ACTIVATED("&aCheckpoint #%s was activated by: &f%s"),
    CREATED_NUKEROOM("&cCreated the nukeroom here."),
    CREATED_CHECKPOINT("&cCreated checkpoint with ID %s"),
    CREATED_SIGN("&cCreated sign with ID %s with a timer %s seconds"),
    CREATED_SPAWN("&cCreated a spawn with ID %s"),
    DOOR_OPENED("&b10 Second Door Activated: &cRUN"),
    DOOR_ALREADY_ACTIVATED("&eDoor Already Activated: &cDEFEND"),
    DOOR_EDGE_BAD("&cThe door edge must be between 1 and 2."),
    DOOR_ACTIVATED("&aYou activated this door. Hang tight!"),
    GAME_FILE_NULL("&cThe game file is null. Please use /arena load <fileName>."),
    GAME_NOT_RUNNING("&cA game is not currently running."),
    JOINED_INFECTED("&c%s joined as Infected"),
    LOADING_FILE("&cPreparing to modify the file %s"),
    MATCH_OVER("&c3 &erounds have been played. Playing a new match soon."),
    NUKE_INCOMING("&9%s has Triggered the Nuke. &c10 &9Seconds till it arrives! GET INSIDE!"),
    NUKE_INCOMING_ALT("&9A Zombie triggered the Nuke! ALL HOPE IS LOST! NUKE NUKE NUKE!"),
    NUKEROOM_ACTIVATED("&a%s Activated the Nukeroom Door: &cDEFEND"),
    NUKEROOM_OPEN_FOR("&bNukeroom Open for 10 Seconds: &cRUN"),
    POSITIVE_VALUES("&cMake sure the input values are positive."),
    PLAYER_INFECTED_OTHER("&c%s infected %s"),
    PLAYER_ONLY_COMMAND("&cThis is a player only command."),
    STARTING_ZOMBIE("&c%s is a starting Zombie!"),
    UPDATED_KIT("&aYou've equipped the %s kit &f(%s&f)&a."),
    USAGE_GAME("&cCommand Usage for /game" +
            "\n/game load <file_name> &7loads a file" +
            "\n/game door add <seconds> &7adds a sign with a timer" +
            "\n/game door timer <ID> <seconds> &7sets a sign's timer" +
            "\n/game door edge <ID> <1,2> &7sets a door's edges" +
            "\n&e&lNOT DONE &c/game door delete <ID> &7deletes a game sign" +
            "\n&e&lNOT DONE &c/game door view <ID> &7teleport to a game sign" +
            "\n/game addspawn &7adds a spawn location" +
            "\n/game checkpoint &7adds a checkpoint" +
            "\n/game nukeroom &7sets the nukeroom for this map"),
    UNKNOWN_COMMAND("&cCommand unknown! Use &a/game help &cfor more information!"),
    CORRECTION("&cDid you mean &a/game %s&c?"),
    VOTED("&aYou voted for &2%s"),
    VOTED_ALREADY("&cYou already voted."),
    RELOADING("&aReloading..."),
    YOU_ARE_A_HUMAN("&bYou are a Human! Make it to the Nukeroom to survive!"),
    YOU_ARE_A_ZOMBIE("&aGrr Brains! Infect all the humans before they nuke you!"),
    WIN_HUMANS("&bThe Humans won this round!"),
    WIN_ZOMBIES("&cThe Zombies won this round!"),
    ZOMBIE_ONLY_COMMAND("&cThis is a Zombie only command!"),
    ZTELE_COMMAND("&cType &e/ztele &cto Teleport to the new Zombie Checkpoint!");

    private String message;

    Messages(String message) {
        this.message = Utils.color(message);
    }

    public String toString() {
        return message;
    }

    public String toString(Object... parts) {
        return String.format(message, parts);
    }

    public void send(Player player) {
        player.sendMessage(message);
    }

    public void send(CommandSender sender) {
        sender.sendMessage(message);
    }

    public void send(Player player, String replacement) {
        player.sendMessage(message.replace("%s", replacement));
    }

    public void send(Player player, Object... replacements) {
        player.sendMessage(String.format(message, replacements));
    }

    public void broadcast() {
        Bukkit.broadcastMessage(message);
    }

    public void broadcast(String replacement) {
        Bukkit.broadcastMessage(message.replace("%s", replacement));
    }

    public void broadcast(Object... replacements) {
        Bukkit.broadcastMessage(String.format(message, replacements));
    }

}