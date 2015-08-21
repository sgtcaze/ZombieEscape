package net.cosmosmc.mcze.listeners;

import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.GameArena;
import net.cosmosmc.mcze.profiles.Profile;
import net.cosmosmc.mcze.profiles.ProfileSaver;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    private final ZombieEscape PLUGIN;

    public PlayerQuit(ZombieEscape plugin) {
        this.PLUGIN = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        GameArena gameArena = PLUGIN.getGameArena();

        gameArena.purgePlayer(event.getPlayer());

        if (gameArena.isGameRunning()) {
            if (gameArena.shouldEnd()) {
                gameArena.endGame();
            }
        }

        Profile profile = PLUGIN.getGameManager().getRemovedProfile(event.getPlayer());
        new ProfileSaver(profile, PLUGIN).runTaskAsynchronously(PLUGIN);
    }

}