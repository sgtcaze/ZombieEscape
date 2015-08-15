package net.cosmosmc.mcze.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerInfectedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}