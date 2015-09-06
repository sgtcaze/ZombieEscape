package net.cosmosmc.mcze.utils;

import net.cosmosmc.mcze.ZombieEscape;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Configuration {
    private final ZombieEscape INSTANCE;
    private final File SETTINGS_FOLDER;
    private final File ARENAS_FOLDER;
    private final File SETTINGS_FILE;
    private final YamlConfiguration SETTINGS_CONFIG;

    public Configuration(ZombieEscape instance) {
        this.INSTANCE = instance;
        this.SETTINGS_FOLDER = new File(INSTANCE.getDataFolder(), "Settings");
        this.ARENAS_FOLDER = new File(INSTANCE.getDataFolder(), "Arenas");
        this.SETTINGS_FILE = new File(SETTINGS_FOLDER, "Settings.yml");
        this.SETTINGS_CONFIG = YamlConfiguration.loadConfiguration(SETTINGS_FILE);

        if(!SETTINGS_FOLDER.exists()) SETTINGS_FOLDER.mkdirs();
        if(!ARENAS_FOLDER.exists()) ARENAS_FOLDER.mkdirs();

        if(!SETTINGS_CONFIG.isConfigurationSection("Database")) {
            SETTINGS_CONFIG.set("Database.Address", "localhost:3306");
            SETTINGS_CONFIG.set("Database.Schema", "example");
            SETTINGS_CONFIG.set("Database.Username", "root");
            SETTINGS_CONFIG.set("Database.Password", "root");

            saveSettingsConfig();
        }
    }

    public File getArenasFolder() {
        return ARENAS_FOLDER;
    }

    public YamlConfiguration getSettingsConfig() {
        return SETTINGS_CONFIG;
    }

    @Deprecated
    public YamlConfiguration getArenaConfig(String name) {
        return null;
    }

    public void saveSettingsConfig() {
        try {
            SETTINGS_CONFIG.save(SETTINGS_FILE);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void saveArenaConfig(String name, YamlConfiguration configuration) {
        try {
            configuration.save(new File(ARENAS_FOLDER, name + ".yml"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
