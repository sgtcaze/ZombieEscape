package net.cosmosmc.mcze.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItem implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        /* TODO: Cancel this event based on if the player is in an arena and if so check if the state allows this event or not. */
        event.setCancelled(true);
    }

}