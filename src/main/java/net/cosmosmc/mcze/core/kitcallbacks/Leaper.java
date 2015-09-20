package net.cosmosmc.mcze.core.kitcallbacks;

import net.cosmosmc.mcze.core.constants.KitAction;
import net.cosmosmc.mcze.utils.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Leaper implements KitAction {
    private static final int COOLDOWN_TIME = 10; // Seconds
    private final Map<UUID, Long> LAST_USED = new HashMap<>();

    @Override
    public void giveKit(Player player) {
        player.getInventory().addItem(new ItemStackBuilder(Material.CARROT_ITEM).withName("&bKarrot").build());
    }

    @Override
    public void interact(PlayerInteractEvent event, ItemStack itemStack) {
        Player player = event.getPlayer();

        if(!LAST_USED.containsKey(player.getUniqueId()) || ((System.currentTimeMillis() - LAST_USED.get(player.getUniqueId())) > TimeUnit.SECONDS.toMillis(COOLDOWN_TIME))) {
            /* TODO: Make the leaper kit work. */

            LAST_USED.put(player.getUniqueId(), System.currentTimeMillis());
        } else {
            event.setCancelled(true);
        }
    }
}