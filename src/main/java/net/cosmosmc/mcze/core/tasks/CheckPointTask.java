package net.cosmosmc.mcze.core.tasks;

import lombok.AllArgsConstructor;
import net.cosmosmc.mcze.core.GameArena;
import net.cosmosmc.mcze.core.constants.Messages;
import net.cosmosmc.mcze.core.data.Checkpoint;
import net.cosmosmc.mcze.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

/**
 * This class is responsible for detecting when Zombies
 * come close to a checkpoint, and activates them accordingly.
 */
@AllArgsConstructor
public class CheckPointTask extends BukkitRunnable {

    private GameArena gameArena;
    private ArrayList<Checkpoint> checkpoints;

    @Override
    public void run() {
        for (Checkpoint checkpoint : checkpoints) {
            if (checkpoint.isActivated()) {
                continue;
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (gameArena.isHuman(player)) {
                    continue;
                }

                if (player.getLocation().distanceSquared(checkpoint.getLocation()) <= 25) {
                    checkpoint.activate();
                    Messages.CHECKPOINT_ACTIVATED.broadcast(checkpoint.getId(), player.getName());
                    gameArena.broadcast(Messages.ZTELE_COMMAND.toString(), false);
                    break;
                }
            }
        }
    }

}