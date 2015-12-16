package net.cosmosmc.mcze.api.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.cosmosmc.mcze.core.constants.InfectReason;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@AllArgsConstructor
public final class PlayerInfectedEvent extends Event {

    private Player infected;
    private InfectReason infectReason;
    private Player source;
    private boolean cancelled;

    private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}