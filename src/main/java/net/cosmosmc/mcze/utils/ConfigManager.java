package net.cosmosmc.mcze.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;

import net.cosmosmc.mcze.ZombieEscape;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {

    private final String NAME;
    private FileConfiguration fc;
    private File file;
    private final ZombieEscape PLUGIN;
    private static List<ConfigManager> configs = new ArrayList<>();

    private ConfigManager(ZombieEscape p, String n) {
        this.NAME = n;
        this.PLUGIN = p;

        configs.add(this);
    }

    /**
     * Gets the owner of the config
     *
     * @return The player as type bukkit.entity.Player
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
     * Get a config from type 'Config'. If it doesn't exist it will create a new Config. NOTE: String n must be exactly the Config's name.
     *
     * @param n
     *            The Name of the config found by getName()
     * @return Config for given name.
     */
    public static ConfigManager getConfig(ZombieEscape p, String n) {
        for (ConfigManager c : configs) {
            if (c.getName().equals(n)) {
                return c;
            }
        }
        return new ConfigManager(p, n);
    }

    /**
     * Deletes the file
     *
     * @return True if the config was successfully deleted. If anything went
     *         wrong it returns false
     */
    public boolean delete() {
        if (getFile().delete()) {
            return true;
        }
        return false;
    }

    /**
     * Checks to make sure the config is null or not. This is only a check and
     * it wont create the config.
     *
     * @return True if it exists & False if it doesn't
     */
    public boolean exists() {
        if (fc == null || file == null) {
            File temp = new File(getDataFolder(), getName() + ".yml");
            if (!temp.exists()) {
                return false;
            } else {
                file = temp;
            }
        }
        return true;
    }

    /**
     * Gets the plugin's folder. If none exists it will create it.
     *
     * @return The folder as type java.io.File
     */
    public File getDataFolder() {
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
            file = new File(getDataFolder(), getName() + ".yml");
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
     *         org.bukkit.configuration.file.FileConfiguration
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
    @SuppressWarnings("deprecation")
    public void reload() {
        if (file == null) {
            file = new File(getDataFolder(), getName() + ".yml");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            fc = YamlConfiguration.loadConfiguration(file);
            InputStream defConfigStream = PLUGIN.getResource(getName() + ".yml");
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