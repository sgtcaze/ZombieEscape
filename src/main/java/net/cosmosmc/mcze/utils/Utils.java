package net.cosmosmc.mcze.utils;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import net.cosmosmc.mcze.core.constants.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Utils {

    private Utils() {
    }

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static int getNumber(Player player, String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            if (player != null) {
                Messages.BAD_INPUT.send(player, input);
            }

            return -1;
        }
    }

}