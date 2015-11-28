package net.cosmosmc.mcze.core.data;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import lombok.Getter;
import lombok.Setter;
import net.cosmosmc.mcze.utils.Utils;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

@Getter
@Setter
public class Checkpoint {

    private int id;
    private boolean activated;
    private Location location;
    private Hologram hologram;

    /**
     * Creates a Hologram Checkpoint. The checkpoint will
     * display as inactive until a Zombie comes close enough.
     *
     * @param plugin the plugin instance
     */
    public void create(Plugin plugin) {
        activated = false;
        if (hologram == null) {
            hologram = HologramsAPI.createHologram(plugin, location);
            hologram.appendTextLine(Utils.color("&a&lCHECKPOINT #" + id));
            hologram.appendTextLine(Utils.color("Not Active"));
        } else {
            hologram.removeLine(1);
            hologram.appendTextLine("Not Active");
        }
    }

    /**
     * Activates the Hologram Checkpoint. The checkpoint will
     * display as active, and cannot be triggered again until reset.
     */
    public void activate() {
        activated = true;
        hologram.removeLine(1);
        hologram.appendTextLine(Utils.color("&fACTIVATED"));
    }

}