package net.cosmosmc.mcze.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Map.Entry;
import java.util.Set;

import net.cosmosmc.mcze.ZombieEscape;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ArenaConfiguration {

    private final ZombieEscape plugin;

    private final String name;

    private File configFile;

    private FileConfiguration config;

    public ArenaConfiguration(ZombieEscape plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.configFile = new File(plugin.getDataFolder(), name + ".yml");

        reloadConfig();
    }

    public void reloadConfig() {
        // load the file and replace the current instance
        this.config = YamlConfiguration.loadConfiguration(this.configFile);

        Reader defaultConfigStream = null;
        try {
            // create an InputStream to read from the *default* config
            defaultConfigStream = new InputStreamReader(this.plugin.getResource(this.name + ".yml"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            // shouldn't happen, but print the error if it does
            e.printStackTrace();
        }
        if (defaultConfigStream != null) {
            // if a default configuration exists, set it as our config's default
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(defaultConfigStream);
            this.config.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (this.config == null) {
            // if our config is null, try to give it a value
            reloadConfig();
        }

        return this.config;
    }

    public boolean saveConfig() {
        if (this.config == null || this.configFile == null) {
            // we obviously can't save if there's nothing to save or anywhere to save to
            return false;
        }

        try {
            // if the save succeeds, return true, otherwise false
            this.config.save(this.configFile);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void saveDefaultConfig() {
        if (this.configFile == null) {
            // if the config file is null, set it to the correct location
            this.configFile = new File(this.plugin.getDataFolder(), this.name + ".yml");
        }

        if (!this.configFile.exists()) {
            // if the config file does not exist, exist it shall
            plugin.saveResource(this.name + ".yml", false);
        }
    }

    public Location loadArena(String name) {
        ConfigurationSection mainData = getConfig().getConfigurationSection("arenas");
        ConfigurationSection arenaData = mainData.getConfigurationSection(name);
        ConfigurationSection spawnData = arenaData.getConfigurationSection("spawn");

        //TODO: make a dedicated arena class to hold data for spawn, name, etc (for now we'll just return the spawn location)
        //deserialize the location data from the config and return it
        return Location.deserialize(spawnData.getValues(false));
    }

    public void saveArena(String name, Location spawn) {
        ConfigurationSection mainData = getConfig().getConfigurationSection("arenas");
        ConfigurationSection section = mainData.createSection(name).createSection("spawn");

        //serialize the location data and write it to the config
        for (Entry<String, Object> entry : spawn.serialize().entrySet()) {
            section.set(entry.getKey(), entry.getValue());
        }
    }

    public Set<String> getArenaNames() {
        ConfigurationSection arenas = getConfig().getConfigurationSection("arenas");

        //return a list of all the keys in the "arenas" section, where keys are the arena names
        return arenas.getKeys(false);
    }

    public boolean doesArenaExist(String name) {
        ConfigurationSection mainData = getConfig().getConfigurationSection("arenas");

        for (String keys : mainData.getKeys(false)) {
            if (keys.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

}