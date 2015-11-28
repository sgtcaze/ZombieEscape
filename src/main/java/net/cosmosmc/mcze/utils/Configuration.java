package net.cosmosmc.mcze.utils;

import com.zaxxer.hikari.HikariDataSource;
import net.cosmosmc.mcze.ZombieEscape;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import sun.dc.pr.PRError;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Configuration {

    private final File SETTINGS_FILE;
    private final YamlConfiguration SETTINGS_CONFIG;

    public Configuration(ZombieEscape instance) {
        this.SETTINGS_FILE = new File(instance.getDataFolder(), "Settings.yml");
        this.SETTINGS_CONFIG = YamlConfiguration.loadConfiguration(SETTINGS_FILE);

        if (!SETTINGS_CONFIG.isConfigurationSection("Database")) {
            SETTINGS_CONFIG.set("Database.Address", "localhost:3306");
            SETTINGS_CONFIG.set("Database.Schema", "example");
            SETTINGS_CONFIG.set("Database.Username", "root");
            SETTINGS_CONFIG.set("Database.Password", "root");
            saveSettingsConfig();
        }
    }

    public YamlConfiguration getSettingsConfig() {
        return SETTINGS_CONFIG;
    }

    public void saveSettingsConfig() {
        try {
            SETTINGS_CONFIG.save(SETTINGS_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSpawn(Player player) {
        SETTINGS_CONFIG.set("Spawn", serializeLocation(player.getLocation()));
    }

    public Location getSpawn() {
        String[] parts = SETTINGS_CONFIG.getString("Spawn").split(" ");
        return new Location(Bukkit.getWorlds().get(0), Double.valueOf(parts[0]), Double.valueOf(parts[1]),
                Double.valueOf(parts[2]), Float.valueOf(parts[3]), Float.valueOf(parts[4]));
    }

    public String serializeLocation(Location location) {
        return location.getX() + " " + location.getY() + " " + location.getZ() + " " + location.getYaw() + " " + location.getPitch();
    }

    public void setupHikari(HikariDataSource hikari, FileConfiguration settings) {
        String address = settings.getString("Database.Address");
        String database = settings.getString("Database.Schema");
        String username = settings.getString("Database.Username");
        String password = settings.getString("Database.Password");

        hikari.setMaximumPoolSize(10);
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", address.split(":")[0]);
        hikari.addDataSourceProperty("port", address.split(":")[1]);
        hikari.addDataSourceProperty("databaseName", database);
        hikari.addDataSourceProperty("user", username);
        hikari.addDataSourceProperty("password", password);

        Connection connection = null;

        try {
            connection = hikari.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `ze_players` (`uuid` varchar(36) NOT NULL, `name` varchar(16) NOT NULL, `zombie_kills` int(11) NOT NULL, " +
                    "`human_kills` int(11) NOT NULL, `points` int(11) NOT NULL, `wins` int(11) NOT NULL, `achievements` varchar(32) NOT NULL, " +
                    "`human_kit` varchar(32) NOT NULL, `zombie_kit` varchar(32) NOT NULL, PRIMARY KEY (`uuid`)) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}