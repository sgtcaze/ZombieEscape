package net.cosmosmc.mcze.core.constants;

import net.cosmosmc.mcze.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

// TODO: Support multiple languages
// TODO: Create more methods for simplicity
public enum Messages {

    GAME_STARTED("&3The game has started!");

    private final String MESSAGE;

    Messages(String message) {
        this.MESSAGE = Utils.color(message);
    }

    public void send(Player player) {
        player.sendMessage(MESSAGE);
    }

    public void send(Player player, String replacement) {
        player.sendMessage(MESSAGE.replace("%s", replacement));
    }

    public void broadcast() {
        Bukkit.broadcastMessage(MESSAGE);
    }

}