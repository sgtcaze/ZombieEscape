package net.cosmosmc.mcze.profiles;

import lombok.AllArgsConstructor;
import net.cosmosmc.mcze.ZombieEscape;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@AllArgsConstructor
public class ProfileSaver extends BukkitRunnable {

    private Profile profile;
    private ZombieEscape plugin;

    private static final String SAVE = "UPDATE data SET zombie_kills=?, human_kills=?, points=?, wins=?, achievements=?, human_kit=?, zombie_kit=? WHERE uuid=?";

    @Override
    public void run() {
        Connection connection = null;

        try {
            connection = plugin.getHikari().getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(SAVE);
            preparedStatement.setInt(1, profile.getZombieKills());
            preparedStatement.setInt(2, profile.getHumanKills());
            preparedStatement.setInt(3, profile.getPoints());
            preparedStatement.setInt(4, profile.getWins());
            preparedStatement.setString(5, new String(profile.getAchievements()));
            preparedStatement.setString(6, profile.getHumanKit().name());
            preparedStatement.setString(7, profile.getZombieKit().name());
            preparedStatement.setString(8, profile.getUuid().toString());
            preparedStatement.execute();
            preparedStatement.close();
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