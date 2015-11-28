package net.cosmosmc.mcze.core.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Achievements {

    UNDEAD_SLAYER("Undead Slayer", "Kill a Zombie"),
    BRAIN_EATER("Brain Eater", "Kill a Human"),
    INFECTION("Infection", "Get Infected by a Zombie"),
    FIRST_GAME_PLAYED("First Game", "Play 1 Game"),
    LONG_TIME_PLAYER("Long Time Player", "Play 100 games");

    private String name;
    private String description;

}