package net.cosmosmc.mcze.listeners;

import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.GameArena;
import net.cosmosmc.mcze.profiles.Profile;
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

        if (!gameArena.isGameRunning()) {
            return; // TODO: Teleport, or listen to respawn?
        }

        if (gameArena.isHuman(player)) {
            gameArena.addZombie(player);

            if (player.getKiller() != null) {
                Profile zombieProfile = PLUGIN.getGameManager().getProfile(player.getKiller());
                zombieProfile.setHumanKills(zombieProfile.getHumanKills() + 1); // Could throw NPE - BEWARE! (We will address this later)
            }
        } else if (gameArena.isZombie(player)) {
            // TODO: Teleport to a checkpoint
        }

        if (gameArena.isGameRunning()) {
            if (gameArena.shouldEnd()) {
                gameArena.endGame();
            }
        }
    }

}