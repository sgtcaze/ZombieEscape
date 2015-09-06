package net.cosmosmc.mcze.profiles;

import lombok.AllArgsConstructor;
import net.cosmosmc.mcze.ZombieEscape;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
public class ProfileLoader extends BukkitRunnable {

    private Profile profile;
    private ZombieEscape plugin;

    private static final String INSERT = "INSERT INTO data VALUES(?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE name=?";
    private static final String SELECT = "SELECT zombie_kills,human_kills,points,wins FROM data WHERE uuid=?";

    @Override
    public void run() {
        Connection connection = null;

        try {
            connection = plugin.getHikari().getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setString(1, profile.getUuid().toString());
            preparedStatement.setString(2, profile.getUuid().toString());
            preparedStatement.setInt(3, 0);
            preparedStatement.setInt(4, 0);
            preparedStatement.setInt(5, 0);
            preparedStatement.setInt(6, 0);
            preparedStatement.setString(7, profile.getName());
            preparedStatement.execute();

            preparedStatement = connection.prepareStatement(SELECT);
            preparedStatement.setString(1, profile.getUuid().toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                profile.setHumanKills(resultSet.getInt("human_kills"));
                profile.setZombieKills(resultSet.getInt("zombie_kills"));
                profile.setPoints(resultSet.getInt("points"));
                profile.setWins(resultSet.getInt("wins"));
                profile.setLoaded(true);
            }

            preparedStatement.close();
            resultSet.close();
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