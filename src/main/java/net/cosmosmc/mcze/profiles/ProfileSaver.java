package net.cosmosmc.mcze.profiles;

import lombok.AllArgsConstructor;
import net.cosmosmc.mcze.ZombieEscape;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class ProfileSaver extends BukkitRunnable {

    private Profile profile;
    private ZombieEscape plugin;

    @Override
    public void run() {
        Connection connection = null;

        try {
            connection = plugin.getHikari().getConnection();

            // TODO: Save stats
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}