package net.cosmosmc.mcze.listeners;

import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.utils.Utils;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Class by itsMonkey
 *
 * Note from sgtcaze -
 * will split door timers into separate
 * runnables, so we can try our best to
 * keep the code concise
 */
public class PlayerInteract implements Listener {

    private ZombieEscape plugin;

    public PlayerInteract(ZombieEscape plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock() instanceof Sign) {
                final Sign sign = (Sign) event.getClickedBlock();

                //Sign has been activated already
                if (sign.hasMetadata("clicked")) {
                    return;
                }

                sign.setMetadata("clicked", new FixedMetadataValue(plugin, true));
                player.sendMessage(Utils.color("&aYou have activated this door!"));
                //Add this to the database for doors opened?

                new BukkitRunnable() {

                    //Door format for anyone wondering http://prntscr.com/84zvlc
                    String[] lines = sign.getLine(1).replace("s", "").split(" ");
                    int time = Integer.parseInt(lines[1]);

                    @Override
                    public void run() {

                        if (time != 1) {
                            time--;
                            sign.setLine(1, lines[0] + " " + time + "s");
                            sign.update();
                        } else {
                            //Open the door. Pretty sure we will store the location of the blocks we need to
                            //Set to air somewhere so I'll just wait for that.
                        }
                    }
                }.runTaskTimerAsynchronously(plugin, 0, 20);
            }
        }
    }

}