package net.cosmosmc.mcze;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import net.cosmosmc.mcze.core.GameArena;
import net.cosmosmc.mcze.core.GameManager;
import net.cosmosmc.mcze.guis.HumanKitMenu;
import net.cosmosmc.mcze.guis.ZombieKitMenu;
import net.cosmosmc.mcze.listeners.*;
import net.cosmosmc.mcze.utils.menus.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class ZombieEscape extends JavaPlugin {

    private GameArena gameArena;
    private HikariDataSource hikari;
    private GameManager gameManager;
    private MenuManager menuManager;

    @Override
    public void onEnable() {
        setupHikari();

        gameArena = new GameArena(this);
        gameManager = new GameManager();
        menuManager = new MenuManager(this);

        menuManager.addMenu("hkits", new HumanKitMenu("Human Kit Menu", 9));
        menuManager.addMenu("zkits", new ZombieKitMenu("Zombie Kit Menu", 9));

        registerListeners();
    }

    // TODO: Cleanup open files/instances/etc
    @Override
    public void onDisable() {
        if (hikari != null) {
            hikari.close();
        }
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new EntityDamageByEntity(this), this);
        pm.registerEvents(new FoodLevelChange(), this);
        pm.registerEvents(new PlayerDeath(this), this);
        pm.registerEvents(new PlayerDropItem(), this);
        pm.registerEvents(new PlayerInteract(this), this);
        pm.registerEvents(new PlayerJoin(this), this);
        pm.registerEvents(new PlayerPickupItem(), this);
        pm.registerEvents(new PlayerQuit(this), this);
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