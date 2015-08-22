package net.cosmosmc.mcze.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public final class PlayerJoinTeamEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private Player player;
    private HashSet<UUID> team;

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return player;
    }

    public Set<UUID> getTeam() {
        return team;
    }

    public PlayerJoinTeamEvent(Player player, HashSet<UUID> team) {
        this.player = player;
        this.team = team;
    }
}
