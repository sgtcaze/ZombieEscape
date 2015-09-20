package net.cosmosmc.mcze.profiles;

import lombok.Getter;
import lombok.Setter;
import net.cosmosmc.mcze.core.constants.Achievements;
import net.cosmosmc.mcze.core.constants.KitType;
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
    private int gamesPlayed;

    private boolean loaded;

    private char[] achievements;

    private KitType humanKit;
    private KitType zombieKit;

    public Profile(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
    }

    public void awardAchievement(Achievements achievement) {
        achievements[achievement.getId()] = 't';
    }

}