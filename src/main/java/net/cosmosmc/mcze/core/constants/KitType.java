package net.cosmosmc.mcze.core.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.cosmosmc.mcze.core.kitcallbacks.Leaper;
import net.cosmosmc.mcze.core.kitcallbacks.Tank;

@Getter
@AllArgsConstructor
public enum KitType {

    LEAPER("Leaper", new Leaper()),

    TANK("Tank", new Tank());

    private String name;
    private KitAction kitAction;

}