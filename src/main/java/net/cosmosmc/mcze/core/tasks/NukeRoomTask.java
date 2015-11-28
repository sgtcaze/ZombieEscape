package net.cosmosmc.mcze.core.tasks;

import lombok.AllArgsConstructor;
import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.GameArena;
import net.cosmosmc.mcze.core.constants.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public class NukeRoomTask extends BukkitRunnable {

    private GameArena gameArena;
    private Location nukeroom;
    private ZombieEscape plugin;

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().distanceSquared(nukeroom) <= 25) {
                if (gameArena.isZombie(player)) {
                    Messages.NUKE_INCOMING_ALT.broadcast();
                } else {
                    Messages.NUKE_INCOMING.broadcast(player.getName());
                }

                new NukeRoomGameOver(gameArena).runTaskLater(plugin, 20 * 10);
                cancel();
                break;
            }
        }
    }

}