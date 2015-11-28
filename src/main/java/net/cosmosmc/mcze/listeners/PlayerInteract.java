package net.cosmosmc.mcze.listeners;

import lombok.AllArgsConstructor;
import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.GameArena;
import net.cosmosmc.mcze.core.constants.GameState;
import net.cosmosmc.mcze.core.constants.KitType;
import net.cosmosmc.mcze.core.constants.Messages;
import net.cosmosmc.mcze.core.data.Door;
import net.cosmosmc.mcze.core.tasks.NukeRoomTask;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class PlayerInteract implements Listener {

    private ZombieEscape plugin;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        /* Check if a sign was interacted with */
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) event.getClickedBlock().getState();

                GameArena gameArena = plugin.getGameArena();
                for (Door door : gameArena.getDoors()) {
                    if (door.matches(sign)) {
                        if (door.isNukeroom()) {
                            if (gameArena.getGameState() != GameState.NUKEROOM) {
                                door.activate(player, sign, plugin);
                                gameArena.setGameState(GameState.NUKEROOM);
                                Messages.NUKEROOM_ACTIVATED.broadcast(player.getName());

                                NukeRoomTask nukeRoomTask = new NukeRoomTask(gameArena, gameArena.getNukeRoom(), plugin);
                                nukeRoomTask.runTaskTimer(plugin, 0, 20);
                                gameArena.setNukeRoomTask(nukeRoomTask);
                            }
                        } else {
                            door.activate(player, sign, plugin);
                        }
                        break;
                    }
                }
            }
        }

        /* Check for kit/selector interaction */
        ItemStack item = event.getItem();

        if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            String stackName = ChatColor.stripColor(item.getItemMeta().getDisplayName());

            switch (stackName) {
                case "Vote":
                    plugin.getMenuManager().getMenu("vote").show(player);
                    break;
                case "Human Kits":
                    plugin.getMenuManager().getMenu("hkits").show(player);
                    break;
                case "Zombie Kits":
                    plugin.getMenuManager().getMenu("zkits").show(player);
                    break;
                default:
                    /* We know iteration is O(N), however the N in this case is very small */
                    for (KitType kitType : KitType.values()) {
                        if (kitType.getName().equals(stackName)) {
                            /* Activates the kit callback */
                            kitType.getKitAction().interact(event, event.getPlayer(), item);
                            break;
                        }
                    }
            }
        }
    }

}