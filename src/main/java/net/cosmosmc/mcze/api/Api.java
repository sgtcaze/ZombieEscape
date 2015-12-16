package net.cosmosmc.mcze.api;

import net.cosmosmc.mcze.core.constants.KitType;
import org.bukkit.entity.Player;

public interface Api {

    void setKit(Player player, KitType kitType);

}