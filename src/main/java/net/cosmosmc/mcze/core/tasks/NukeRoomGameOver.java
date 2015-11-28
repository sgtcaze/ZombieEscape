package net.cosmosmc.mcze.core.tasks;

import lombok.AllArgsConstructor;
import net.cosmosmc.mcze.core.GameArena;
import net.cosmosmc.mcze.core.constants.GameState;
import net.cosmosmc.mcze.utils.Particles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public class NukeRoomGameOver extends BukkitRunnable {

    private GameArena gameArena;

    @Override
    public void run() {
        if (gameArena.getGameState() == GameState.NUKEROOM) {
            for(Player player : Bukkit.getOnlinePlayers()) {
                Particles.spawnParticles(player.getLocation(), player, 3, "EXPLOSION_HUGE");
            }

            gameArena.endGame();
        }
    }

}
