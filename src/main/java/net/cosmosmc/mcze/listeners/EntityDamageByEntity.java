package net.cosmosmc.mcze.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntity implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player damaged = (Player) event.getEntity();
            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();
                //TODO: Check if damager and damaged are in the same team, cancel event
                //TODO: Check if damager is a zombie, make damaged a zombie too
                //We could probably just check if the players' UUID is in some kind of collection to compare teams
            }
        }
    }
}
