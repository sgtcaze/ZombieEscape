package net.cosmosmc.mcze.core.data;

import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

public class Section {

    private World world;
    private int minX, minY, minZ, maxX, maxY, maxZ;
    private Map<Location, SectionBlock> blocks = new HashMap<>();

    /**
     * Initializes the data members of this class. The minimum and
     * maximum points are created so we get a rectangular region.
     *
     * @param location  an arbitrary corner
     * @param location2 another arbitrary corner
     */
    public Section(Location location, Location location2) {
        this.minX = Math.min(location.getBlockX(), location2.getBlockX());
        this.minY = Math.min(location.getBlockY(), location2.getBlockY());
        this.minZ = Math.min(location.getBlockZ(), location2.getBlockZ());
        this.maxX = Math.max(location.getBlockX(), location2.getBlockX());
        this.maxY = Math.max(location.getBlockY(), location2.getBlockY());
        this.maxZ = Math.max(location.getBlockZ(), location2.getBlockZ());
        this.world = location.getWorld();
    }

    /**
     * A small data class that is used to record block data.
     * NOTE: We use Private as our modifier since no outside
     * class from this package should access this.
     */
    @AllArgsConstructor
    private class SectionBlock {
        private Material material;
        private byte data;
    }

    /**
     * Clears any and all blocks between the corners. NOTE: This
     * can be a very expensive call. However, we anticipate most
     * doors to be small, and therefore inexpensive by and large.
     */
    public void clear() {
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= this.maxZ; z++) {
                    Location loc = new Location(world, x, y, z);
                    Block block = loc.getBlock();
                    if (block != null) {
                        blocks.put(loc, new SectionBlock(block.getType(), block.getData()));
                        loc.getBlock().setType(Material.AIR);
                    }
                }
            }
        }
    }

    /**
     * Restores blocks between the corners. NOTE: This will NOT
     * work if called before clear().
     */
    public void restore() {
        for (Map.Entry<Location, SectionBlock> block : blocks.entrySet()) {
            SectionBlock sectionBlock = block.getValue();
            block.getKey().getBlock().setTypeIdAndData(sectionBlock.material.getId(), sectionBlock.data, true);
        }
        blocks.clear();
    }

}