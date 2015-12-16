package net.cosmosmc.mcze.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Is only called when a match (best of 3 rounds) starts
 */
public final class GameStartEvent extends Event {



    private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}