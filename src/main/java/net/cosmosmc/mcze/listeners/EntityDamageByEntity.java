package net.cosmosmc.mcze.listeners;

import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntity implements Listener {

    private final ZombieEscape plugin;

    public EntityDamageByEntity(ZombieEscape plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player damaged = (Player) event.getEntity();
            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();

                //A zombie hurt another zombie
                if (plugin.getGameArena().isZombie(damaged) && plugin.getGameArena().isZombie(damager)) {
                    damager.sendMessage(Utils.color("&cYou cannot hurt other zombies!"));
                    event.setCancelled(true);
                }

                //A human hurt another human
                if (plugin.getGameArena().isHuman(damaged) && plugin.getGameArena().isHuman(damager)) {
                    damager.sendMessage(Utils.color("&cYou cannot hurt other humans!"));
                    event.setCancelled(true);
                }

                //Make the damaged player a zombie
                if (plugin.getGameArena().isZombie(damager) && plugin.getGameArena().isHuman(damaged)) {
                    plugin.getGameArena().addZombie(damaged);
                    damager.sendMessage(Utils.color("&aYou have infected &e" + damaged.getName() + "&a!"));
                }
            }
        }
    }

}