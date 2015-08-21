package net.cosmosmc.mcze.profiles;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
public class Profile {

    private UUID uuid;
    private String name;

    private int zombieKills;
    private int humanKills;
    private int points;
    private int wins;

    private boolean loaded;

    public Profile(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
    }

}