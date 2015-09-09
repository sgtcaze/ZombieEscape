package net.cosmosmc.mcze.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import net.cosmosmc.mcze.ZombieEscape;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author TotallyGamerJet
 */
public class ConfigManager {

    private final String NAME;
    private final String PATH;
    private FileConfiguration fc;
    private File file;
    private final ZombieEscape PLUGIN;
    private static List<ConfigManager> configs = new ArrayList<>();

    private ConfigManager(ZombieEscape p, String path, String n) {
        this.NAME = n;
        this.PATH = path;
        this.PLUGIN = p;

        configs.add(this);
    }

    /**
     * Gets the name of the config
     *
     * @return The name as type String
     */
    public String getName() {
        if (NAME == null)
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        return NAME;
    }

    /**
     * Get a config from type 'ConfigManager'. If it doesn't exist it will create a new ConfigManager. This makes the path an empty String.
     * NOTE: String n must be exactly the Config's name.
     * NOTE: Path is inferred to be nothing.
     *
     * @param n The Name of the config found by getName()
     * @return Config for given name.
     */
    public static ConfigManager getConfig(ZombieEscape p, String n) {
        for (ConfigManager c : configs) {
            if (c.getName().equals(n)) {
                if(c.getPath().equals("")) {
                    return c;
                }
            }
        }
        return new ConfigManager(p, "", n);
    }

    /**
     * Get a config from type 'ConfigManager'. If it doesn't exist it will create a new ConfigManager.
     * NOTE: String n must be exactly the Config's name.
     * NOTE: Path CANNOT be null. It will break everything.
     * Use getConfig(ZombieEscape, name);
     * If you must use this method please do "";
     *
     * @param n The Name of the config found by getName()
     * @param path The path for the file
     * @return ConfigManager for given name.
     */
    public static ConfigManager getConfig(ZombieEscape p, String path, String n) {
        for (ConfigManager c : configs) {
            if (c.getName().equals(n)) {
                if(c.getPath().endsWith(path)) {
                    return c;
                }
            }
        }
        if(!path.startsWith("/")) //Makes the path correctly formatted.
            path = "/" + path;
        if(!path.endsWith("/"))
            path = path + "/";
        return new ConfigManager(p, path, n);
    }

    /**
     * Deletes the file
     *
     * @return True if the config was successfully deleted. If anything went
     * wrong it returns false
     */
    public boolean delete() {
        return getFile().delete();
    }

    /**
     * Checks to make sure the config is null or not. This is only a check and
     * it wont create the config.
     *
     * @return True if it exists & False if it doesn't
     */
    public boolean exists() {
        if (fc == null || file == null) {
            File temp = new File(getDataFolder() + getPath(), getName() + ".yml");
            if (!temp.exists()) {
                return false;
            }
                file = temp;
        }
        return true;
    }

    /**
     * Makes the path directory
     *
     * @return The path as typ String.
     */
    public String getPath() {
        File path = new File(getDataFolder() + PATH);
        if(!path.exists())
            path.mkdirs();
        return PATH;
    }

    /**
     * Gets the plugin's folder. If none exists it will create it.
     *
     * @return The folder as type java.io.File
     */
    public File getDataFolder() { //I do it this way because PLUGIN.getDataFolder() kept breaking...
        File dir = new File(ConfigManager.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));
        File d = new File(dir.getParentFile().getPath(), PLUGIN.getName());
        if (!d.exists()) {
            d.mkdirs();
        }
        return d;
    }

    /**
     * Gets the File for the owner
     *
     * @return The File as type java.io.File
     */
    public File getFile() {
        if (file == null) {
            file = new File(getDataFolder() + getPath(), getName() + ".yml");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * Gets the config for the owner
     *
     * @return The config as type
     * org.bukkit.configuration.file.FileConfiguration
     */
    public FileConfiguration getConfig() {
        if (fc == null) {
            fc = YamlConfiguration.loadConfiguration(getFile());
        }
        return fc;
    }

    /**
     * Reloads or "Gets" the file and config
     */
    public void reload() {
        if (file == null) {
            file = new File(getDataFolder() + getPath(), getName() + ".yml");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            fc = YamlConfiguration.loadConfiguration(file);
            Reader defConfigStream = null;
            try {
                defConfigStream = new InputStreamReader(PLUGIN.getResource(getName() + ".yml"), "UTF8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                fc.setDefaults(defConfig);
            }
        }
    }

    /**
     * Deletes then creates the config
     */
    public void resetConfig() {
        delete();
        getConfig();
    }

    /**
     * Saves the DefaultConfig
     */
    public void saveDefaultConfig() {
        if (!exists()) {
            // if the config file does not exist, exist it shall
            PLUGIN.saveResource(getDataFolder() + getPath() + getName() + ".yml", false);
        }
    }

    /**
     * Saves the config
     */
    public void saveConfig() {
        try {
            getConfig().save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}