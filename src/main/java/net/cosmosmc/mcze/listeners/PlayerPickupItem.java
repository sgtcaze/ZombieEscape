package net.cosmosmc.mcze.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerPickupItem implements Listener {

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        /* TODO: Cancel this event based on if the player is in an arena and if so check if the state allows this event or not. */
        if (event.getPlayer().getWorld().getName().equals("world")) {
            event.setCancelled(true);
        }
    }

}