package net.cosmosmc.mcze;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import net.cosmosmc.mcze.api.Api;
import net.cosmosmc.mcze.api.ZEApi;
import net.cosmosmc.mcze.commands.Game;
import net.cosmosmc.mcze.commands.SetLobbySpawn;
import net.cosmosmc.mcze.commands.Ztele;
import net.cosmosmc.mcze.core.GameArena;
import net.cosmosmc.mcze.listeners.*;
import net.cosmosmc.mcze.menus.HumanKitMenu;
import net.cosmosmc.mcze.menus.VoteMenu;
import net.cosmosmc.mcze.menus.ZombieKitMenu;
import net.cosmosmc.mcze.profiles.Profile;
import net.cosmosmc.mcze.utils.Configuration;
import net.cosmosmc.mcze.utils.Utils;
import net.cosmosmc.mcze.utils.menus.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * NOTE TO CONTRIBUTORS:
 * Alpha versions of Zombie Escape WILL print out debug information
 * regardless. Please do not remove these until we are out of ALPHA.
 */
@Getter
public class ZombieEscape extends JavaPlugin {

    private static Api api;
    private Location serverSpawn;

    private GameArena gameArena;
    private MenuManager menuManager;
    private Configuration configuration;
    private HikariDataSource hikari = new HikariDataSource();

    private Map<UUID, Profile> profiles = new HashMap<>();

    @Override
    public void onEnable() {
        configuration = new Configuration(this);
        configuration.setupHikari(hikari, configuration.getSettingsConfig());

        gameArena = new GameArena(this);
        menuManager = new MenuManager(this);

        serverSpawn = configuration.getSpawn();

        Bukkit.setSpawnRadius(0);

        World world = Bukkit.getWorlds().get(0);
        world.setSpawnFlags(false, false);
        world.setGameRuleValue("doMobSpawning", "false");

        for (Entity entity : world.getEntities()) {
            if (!(entity instanceof Player) && entity instanceof LivingEntity) {
                entity.remove();
            }
        }

        registerListeners();
        getCommand("game").setExecutor(new Game());
        getCommand("setlobbyspawn").setExecutor(new SetLobbySpawn(this));
        getCommand("ztele").setExecutor(new Ztele(this));

        menuManager.addMenu("hkits", new HumanKitMenu("Human Kit Menu", 9, this));
        menuManager.addMenu("zkits", new ZombieKitMenu("Zombie Kit Menu", 9, this));
        menuManager.addMenu("vote", new VoteMenu(Utils.color("&8Vote"), 9, gameArena));
    }

    @Override
    public void onDisable() {
        if (hikari != null) {
            hikari.close();
        }

        // TODO: Cleanup open files/instances/etc
    }

    public static Api getApi() {
        if (api == null) {
            api = new ZEApi();
        }

        return api;
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new EntityDamageByEntity(this), this);
        pm.registerEvents(new PlayerDeath(this), this);
        pm.registerEvents(new PlayerInteract(this), this);
        pm.registerEvents(new PlayerJoin(this), this);
        pm.registerEvents(new PlayerPickupItem(), this);
        pm.registerEvents(new PlayerQuit(this), this);

        pm.registerEvents(new ServerListener(this), this);
    }

    public Profile getProfile(Player player) {
        return profiles.get(player.getUniqueId());
    }

    public Profile getRemovedProfile(Player player) {
        return profiles.remove(player.getUniqueId());
    }

}