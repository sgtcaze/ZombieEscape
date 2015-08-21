package net.cosmosmc.mcze.profiles;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
public class Profile {

    private final UUID UNIQUE_ID;
    private final String NAME;

    private int zombieKills;
    private int humanKills;
    private int points;
    private int wins;

    private boolean loaded;

    public Profile(Player player) {
        this.UNIQUE_ID = player.getUniqueId();
        this.NAME = player.getName();
    }

}