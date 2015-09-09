package net.cosmosmc.mcze.utils;

import net.cosmosmc.mcze.ZombieEscape;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map.Entry;
import java.util.Set;

public class ArenaConfiguration {

    private final ZombieEscape PLUGIN;
    private final FileConfiguration config;

    public ArenaConfiguration(ZombieEscape plugin, String name) {
        this.PLUGIN = plugin;
        config = ConfigManager.getConfig(plugin, name).getConfig();
    }

    public Location loadArena(String name) {
        ConfigurationSection mainData = config.getConfigurationSection("arenas");
        ConfigurationSection arenaData = mainData.getConfigurationSection(name);
        ConfigurationSection spawnData = arenaData.getConfigurationSection("spawn");

        //TODO: make a dedicated arena class to hold data for spawn, name, etc (for now we'll just return the spawn location)
        //deserialize the location data from the config and return it
        return Location.deserialize(spawnData.getValues(false));
    }

    public void saveArena(String name, Location spawn) {
        ConfigurationSection mainData = config.getConfigurationSection("arenas");
        ConfigurationSection section = mainData.createSection(name).createSection("spawn");

        //serialize the location data and write it to the config
        for (Entry<String, Object> entry : spawn.serialize().entrySet()) {
            section.set(entry.getKey(), entry.getValue());
        }
    }

    public Set<String> getArenaNames() {
        ConfigurationSection arenas = config.getConfigurationSection("arenas");

        //return a list of all the keys in the "arenas" section, where keys are the arena names
        return arenas.getKeys(false);
    }

    public boolean doesArenaExist(String name) {
        ConfigurationSection mainData = config.getConfigurationSection("arenas");

        for (String keys : mainData.getKeys(false)) {
            if (keys.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

}