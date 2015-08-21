package net.cosmosmc.mcze.listeners;

import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.GameArena;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntity implements Listener {

    private final ZombieEscape PLUGIN;

    public EntityDamageByEntity(ZombieEscape plugin) {
        this.PLUGIN = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player damaged = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        GameArena gameArena = PLUGIN.getGameArena();

        if (gameArena.isGameRunning()) {
            if (gameArena.isSameTeam(damaged, damager)) {
                event.setCancelled(true);
            } else if (gameArena.isHuman(damaged) && gameArena.isZombie(damager)) {
                gameArena.addZombie(damaged);

                if (gameArena.shouldEnd()) {
                    gameArena.endGame();
                }
            }
        }
    }

}