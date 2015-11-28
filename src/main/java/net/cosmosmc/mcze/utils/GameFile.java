package net.cosmosmc.mcze.utils;

import lombok.Getter;
import net.cosmosmc.mcze.core.data.Checkpoint;
import net.cosmosmc.mcze.core.data.Door;
import net.cosmosmc.mcze.core.data.Section;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GameFile {

    private File flatFile;

    @Getter
    private FileConfiguration config;

    public GameFile(String path, String name) {
        flatFile = new File(path, name);

        if (!flatFile.exists()) {
            try {
                FileWriter writer = new FileWriter(flatFile);
                writer.write("ArenaName: " + name.split(".yml")[0]);
                writer.close();
            } catch (IOException e) {
                // Ignore
            }
        }

        config = new YamlConfiguration();

        try {
            config.load(flatFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveFile() {
        try {
            config.save(flatFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String serializeLocation(Location location) {
        return location.getX() + " " + location.getY() + " " + location.getZ() + " " + location.getYaw() + " " + location.getPitch();
    }

    public Location deserializeLocation(World world, String input) {
        String[] parts = input.split(" ");
        return new Location(world, Double.valueOf(parts[0]), Double.valueOf(parts[1]),
                Double.valueOf(parts[2]), Float.valueOf(parts[3]), Float.valueOf(parts[4]));
    }

    public ArrayList<Door> getDoors(World world) {
        ArrayList<Door> doors = new ArrayList<>();
        for (String key : config.getConfigurationSection("Doors").getKeys(false)) {
            if (key.equals("Amount")) {
                continue; // Skip, this is just to keep track
            }

            Door door = new Door();
            door.setId(Integer.parseInt(key));
            door.setLocation(getLocation(world, "Doors." + key + ".Location"));
            door.setSeconds(config.getInt("Doors." + key + ".Timer"));

            Location edge1 = locationFromConfig(world, "Doors." + key + ".Edge1");
            Location edge2 = locationFromConfig(world, "Doors." + key + ".Edge2");
            Section section = new Section(edge1, edge2);
            door.setSection(section);
            door.setNukeroom(config.getBoolean("Doors." + key + ".Nukeroom"));
            doors.add(door);
        }
        return doors;
    }

    public ArrayList<Checkpoint> getCheckpoints(World world) {
        ArrayList<Checkpoint> checkpoints = new ArrayList<>();
        for (String key : config.getConfigurationSection("Checkpoints").getKeys(false)) {
            if (key.equals("Amount")) {
                continue; // Skip, this is just to keep track
            }

            Checkpoint checkpoint = new Checkpoint();
            checkpoint.setId(Integer.parseInt(key));
            checkpoint.setLocation(locationFromConfig(world, "Checkpoints." + key + ".Location"));
            checkpoints.add(checkpoint);
        }
        return checkpoints;
    }

    private Location locationFromConfig(World world, String path) {
        String[] parts = config.getString(path).split(" ");
        int x = (int) Double.parseDouble(parts[0]);
        int y = (int) Double.parseDouble(parts[1]);
        int z = (int) Double.parseDouble(parts[2]);
        float yaw = (float) Double.parseDouble(parts[3]);
        float pitch = (float) Double.parseDouble(parts[4]);
        return new Location(world, x + 0.5, y + 0.5, z + 0.5, yaw, pitch);
    }

    public Location getLocation(World world, String path) {
        world = world == null ? Bukkit.getWorld(path + ".World") : world;
        return locationFromConfig(world, path);
    }

    public ArrayList<Location> getLocations(World world, String path) {
        ArrayList<Location> spawns = new ArrayList<>();
        world = world == null ? Bukkit.getWorld(path + ".World") : world;

        for (String set : config.getConfigurationSection(path).getKeys(false)) {
            if (!set.equals("Amount")) {
                spawns.add(locationFromConfig(world, path + "." + set + ".Location"));
            }
        }
        spawns.trimToSize(); // Trim any unnecessary array values
        return spawns;
    }

    public int createListLocation(Player p, Location location, String path) {
        int next = config.getInt(path + ".Amount") + 1;
        location = location == null ? p.getLocation() : location;
        config.set(path + ".Amount", next);
        config.set(path + "." + next + ".Location", serializeLocation(location));
        saveFile();
        return next;
    }

    public void createLocation(Player p, Location location, String path) {
        location = location == null ? p.getLocation() : location;
        config.set(path, serializeLocation(location));
        saveFile();
    }

}