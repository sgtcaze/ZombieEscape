package net.cosmosmc.mcze.utils;

import net.cosmosmc.mcze.ZombieEscape;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.Map.Entry;
import java.util.Set;

public class ArenaConfiguration {

    private final ZombieEscape PLUGIN;

    private final String NAME;

    private File configFile;

    private FileConfiguration config;

    public ArenaConfiguration(ZombieEscape plugin, String name) {
        this.PLUGIN = plugin;
        this.NAME = name;
        this.configFile = new File(plugin.getDataFolder(), name + ".yml");

        reloadConfig();
    }

    public void reloadConfig() {
        // load the file and replace the current instance
        this.config = YamlConfiguration.loadConfiguration(this.configFile);

        Reader defaultConfigStream = null;
        try {
            // create an InputStream to read from the *default* config
            defaultConfigStream = new InputStreamReader(this.PLUGIN.getResource(this.NAME + ".yml"), "UTF8");
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
            this.configFile = new File(this.PLUGIN.getDataFolder(), this.NAME + ".yml");
        }

        if (!this.configFile.exists()) {
            // if the config file does not exist, exist it shall
            PLUGIN.saveResource(this.NAME + ".yml", false);
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