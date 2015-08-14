package net.cosmosmc.mcze.listeners;

import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.GameArena;
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

        } else {
            if (gameArena.shouldStart()) {
                gameArena.startCountdown();
            }
        }
    }

}