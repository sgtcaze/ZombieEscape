package net.cosmosmc.mcze;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import net.cosmosmc.mcze.core.GameArena;
import net.cosmosmc.mcze.listeners.EntityDamageByEntity;
import net.cosmosmc.mcze.listeners.PlayerInteract;
import net.cosmosmc.mcze.listeners.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class ZombieEscape extends JavaPlugin {

    private GameArena gameArena;
    private HikariDataSource hikari;

    @Override
    public void onEnable() {
        setupHikari();

        gameArena = new GameArena(this);

        registerListeners();
    }

    @Override
    public void onDisable() {
        if (hikari != null) {
            hikari.close();
        }
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerJoin(this), this);
        pm.registerEvents(new EntityDamageByEntity(this), this);
        pm.registerEvents(new PlayerInteract(this), this);
    }

    private void setupHikari() {
        FileConfiguration config = getConfig();

        String address = config.getString("Database.Address");
        String name = config.getString("Database.Schema");
        String username = config.getString("Database.Username");
        String password = config.getString("Database.Password");

        hikari = new HikariDataSource();
        hikari.setMaximumPoolSize(10);
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", address);
        hikari.addDataSourceProperty("port", "3306");
        hikari.addDataSourceProperty("databaseName", name);
        hikari.addDataSourceProperty("user", username);
        hikari.addDataSourceProperty("password", password);
    }

}