package net.cosmosmc.mcze.listeners;

import lombok.AllArgsConstructor;
import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.GameArena;
import net.cosmosmc.mcze.profiles.Profile;
import net.cosmosmc.mcze.profiles.ProfileSaver;
import net.cosmosmc.mcze.utils.Cooldowns;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class PlayerQuit implements Listener {

    private ZombieEscape plugin;

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        GameArena gameArena = plugin.getGameArena();

        gameArena.purgePlayer(event.getPlayer());

        if (gameArena.isGameRunning()) {
            if (gameArena.shouldEnd()) {
                // Possibly deduct points?
                gameArena.endGame();
            }
        }

        Cooldowns.removeCooldowns(event.getPlayer());

        Profile profile = plugin.getRemovedProfile(event.getPlayer());
        new ProfileSaver(profile, plugin).runTaskAsynchronously(plugin);
    }

}