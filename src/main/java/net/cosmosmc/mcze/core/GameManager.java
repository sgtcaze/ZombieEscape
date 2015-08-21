package net.cosmosmc.mcze.core;

import lombok.Getter;
import net.cosmosmc.mcze.profiles.Profile;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class GameManager {

    private final Map<UUID, Profile> PROFILES = new HashMap<>();

    public Profile getProfile(Player player) {
        return PROFILES.get(player.getUniqueId());
    }

    public Profile getRemovedProfile(Player player) {
        return PROFILES.remove(player.getUniqueId());
    }

}