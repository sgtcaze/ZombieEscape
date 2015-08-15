package net.cosmosmc.mcze.utils;

import org.bukkit.ChatColor;

public class Utils {

    //Easier way of sending messages
    public static String color(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
