package net.cosmosmc.mcze.core;

import lombok.Getter;
import net.cosmosmc.mcze.profiles.Profile;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class GameManager {

    private Map<UUID, Profile> profiles = new HashMap<>();

    public Profile getProfile(Player player) {
        return profiles.get(player.getUniqueId());
    }

    public Profile getRemovedProfile(Player player) {
        return profiles.remove(player.getUniqueId());
    }

}