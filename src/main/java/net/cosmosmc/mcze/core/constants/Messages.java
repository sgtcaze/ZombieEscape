package net.cosmosmc.mcze.core.constants;

import net.cosmosmc.mcze.utils.Translation;
import net.cosmosmc.mcze.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

// TODO: Add more languages and messages
public enum Messages {

    GAME_STARTED("&3The game has started!",
            new Translation(Language.SPANISH, "&3El juego ha comenzado!"),
            new Translation(Language.FRENCH, "&3Le jeu a commencé!"),
            new Translation(Language.GERMAN, "&3Das Spiel hat begonnen!") //translations taken from Google Translate
    );

    private final static String DEFAULT_VARIABLE = "%s"; //default string to be replaced in a given message if needed
    private final String MESSAGE;
    private final Translation[] TRANSLATIONS;

    Messages(String message, Translation... translations) {
        if(message == null)
            throw new IllegalArgumentException("Message cannot be null!");
        this.MESSAGE = Utils.color(message);
        this.TRANSLATIONS = translations == null ? new Translation[0] : translations;
    }

    public void send(Player player) {
        player.sendMessage(MESSAGE);
    }

    public void send(Player player, Language language) {
        send(player, null, language);
    }

    public void send(Player player, String replacement, Language language) {
        for(Translation t : TRANSLATIONS) { //if there is an available translation, send it
            if(language == t.getLanguage()) {
                if(replacement == null) {
                    player.sendMessage(t.getTranslation());
                    return;
                }
                player.sendMessage(addReplacement(t.getTranslation(), DEFAULT_VARIABLE, replacement));
                return;
            }
        }
        send(player); //send in english if no other translations are available
    }

    public void send(Player player, String replacement) {
        player.sendMessage(addReplacement(replacement));
    }

    public void broadcast() {
        Bukkit.broadcastMessage(MESSAGE);
    }

    public void broadcast(String replacement) {
        Bukkit.broadcastMessage(addReplacement(replacement));
    }

    private String addReplacement(String replacement) {
        return addReplacement(DEFAULT_VARIABLE, replacement);
    }

    private String addReplacement(String variable, String replacement) {
        return addReplacement(MESSAGE, variable, replacement);
    }

    private static String addReplacement(String message, String variable, String replacement) {
        return message.replace(variable, replacement);
    }

}