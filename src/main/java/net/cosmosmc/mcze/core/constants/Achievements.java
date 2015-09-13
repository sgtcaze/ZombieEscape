package net.cosmosmc.mcze.core.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Achievements {

    FIRST_GAME_PLAYED(0, "First Game", "Play 1 Game"),
    LONG_TIME_PLAYER(1, "Long Time Player", "Play 100 games");

    private int id;
    private String name;
    private String description;
}