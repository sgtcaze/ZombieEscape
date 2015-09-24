package net.cosmosmc.mcze.core.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum Achievements {

    FIRST_GAME_PLAYED("First Game", "Play 1 Game"),
    LONG_TIME_PLAYER("Long Time Player", "Play 100 games");
    
    private static int nextId;
    private final int id;
    private final String name;
    private final String description;
    
    private Achievements(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = nextId++;
    }
    
}
