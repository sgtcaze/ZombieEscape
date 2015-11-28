package net.cosmosmc.mcze.core.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.cosmosmc.mcze.core.kits.*;

@Getter
@AllArgsConstructor
public enum KitType {

    MILKMAN("MilkMan", new MilkMan()),
    LEAPER("Leaper", new Leaper()),
    DECOY("Decoy", new Decoy()),

    FORTIFY("Fortify", new Fortify()),
    ARCHER("Archer", new Archer()),
    BOOMSTICK("BoomStick", new BoomStick());

    private String name;
    private KitAction kitAction;

}