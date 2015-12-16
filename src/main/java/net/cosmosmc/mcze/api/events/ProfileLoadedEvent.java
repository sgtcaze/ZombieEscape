package net.cosmosmc.mcze.api.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.cosmosmc.mcze.profiles.Profile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@AllArgsConstructor
public final class ProfileLoadedEvent extends Event {

    private Profile profile;

    private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}