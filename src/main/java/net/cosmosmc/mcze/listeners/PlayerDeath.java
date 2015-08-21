package net.cosmosmc.mcze.listeners;

import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.GameArena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    private ZombieEscape PLUGIN;

    public PlayerDeath(ZombieEscape plugin) {
        this.PLUGIN = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        GameArena gameArena = PLUGIN.getGameArena();

        if(!gameArena.isGameRunning()) {
            return; // TODO: Teleport, or listen to respawn?
        }

        if (gameArena.isHuman(player)) {
            gameArena.addZombie(player);
        } else if(gameArena.isZombie(player)) {
            // TODO: Teleport to a checkpoint
        }

        // TODO: Game State check
    }

}