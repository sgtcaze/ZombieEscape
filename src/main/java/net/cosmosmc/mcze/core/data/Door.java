package net.cosmosmc.mcze.core.data;

import lombok.Getter;
import lombok.Setter;
import net.cosmosmc.mcze.core.constants.Messages;
import net.cosmosmc.mcze.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
@Setter
public class Door {

    private int id;
    private int task;
    private int seconds;
    private boolean nukeroom;
    private boolean activated;
    private Location location;
    private Section section;

    /**
     * Checks if a given sign corresponds to this door
     *
     * @param sign the sign to check
     * @return whether this matches the sign or not
     */
    public boolean matches(Sign sign) {
        return sign.getLine(0).equals("Door [" + id + "]");
    }

    /**
     * Restores the blocks between the Door corners.
     */
    public void close() {
        section.restore();
    }

    /**
     * Closes a Door, resets the activation state and
     * clears the task id. NOTE: this should only be
     * run if the game ends while the doors are still
     * open. Otherwise we may have doors that STAY open.
     */
    public void cleanup() {
        if (activated) {
            Bukkit.getScheduler().cancelTask(task);
            close();
        }

        task = -1;
        activated = false;
    }

    /**
     * Opens the Door Open at a given sign. The
     * door will stay open for 10 seconds, then
     * will close once more.
     *
     * @param plugin the plugin instance
     * @param sign   the sign that was interacted with
     */
    public void open(Plugin plugin, final Sign sign) {
        section.clear();
        sign.setLine(2, Utils.color("&4&lRUN!"));
        sign.update();

        if (nukeroom) {
            Messages.NUKEROOM_OPEN_FOR.broadcast();
        } else {
            Messages.DOOR_OPENED.broadcast();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                activated = false;
                section.restore();
                sign.setLine(1, "Timer: " + seconds + "s");
                sign.setLine(2, "CLICK TO");
                sign.setLine(3, "ACTIVATE");
                sign.update();
            }
        }.runTaskLater(plugin, 20 * 10);
    }

    /**
     * Activates the Door Open sequence. If the door is already
     * activated, it will notify the activator. Otherwise, it will
     * start a countdown provided by the timer. At the end, it will
     * open the door.
     *
     * @param player the player to activate the door
     * @param sign   the sign that was interacted with
     * @param plugin the plugin instance
     */
    public void activate(Player player, final Sign sign, final Plugin plugin) {
        if (isActivated()) {
            Messages.DOOR_ALREADY_ACTIVATED.send(player);
            return;
        }

        Messages.DOOR_ACTIVATED.send(player);

        activated = true;
        sign.setLine(2, Utils.color("&4&lACTIVATED"));
        sign.setLine(3, "");

        task = new BukkitRunnable() {
            int countdown = seconds;

            @Override
            public void run() {
                if (countdown == 0) {
                    open(plugin, sign);
                    cancel();
                    task = -1;
                    return;
                }

                sign.setLine(1, "Timer: " + countdown + "s");
                sign.update();
                countdown--;
            }
        }.runTaskTimer(plugin, 0, 20).getTaskId();
    }

}