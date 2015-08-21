package net.cosmosmc.mcze.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChange implements Listener {

    @EventHandler
    public void onStarve(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

}