package net.cosmosmc.mcze.listeners;

import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.GameArena;
import net.cosmosmc.mcze.profiles.Profile;
import net.cosmosmc.mcze.profiles.ProfileLoader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private final ZombieEscape PLUGIN;

    public PlayerJoin(ZombieEscape plugin) {
        this.PLUGIN = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        GameArena gameArena = PLUGIN.getGameArena();

        if (gameArena.isGameRunning()) {
            gameArena.addSpectator(event.getPlayer());
        } else {
            if (gameArena.shouldStart()) {
                gameArena.startCountdown();
            }
        }

        final Profile PROFILE = new Profile(event.getPlayer());
        PLUGIN.getGameManager().getProfiles().put(event.getPlayer().getUniqueId(), PROFILE);
        new ProfileLoader(PROFILE, PLUGIN).runTaskAsynchronously(PLUGIN);
    }

}