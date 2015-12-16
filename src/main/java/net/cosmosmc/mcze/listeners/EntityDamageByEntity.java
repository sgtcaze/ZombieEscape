package net.cosmosmc.mcze.listeners;

import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.api.events.PlayerInfectedEvent;
import net.cosmosmc.mcze.core.GameArena;
import net.cosmosmc.mcze.core.constants.InfectReason;
import net.cosmosmc.mcze.core.constants.Messages;
import net.cosmosmc.mcze.profiles.Profile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class EntityDamageByEntity implements Listener {

    private final ZombieEscape PLUGIN;

    public EntityDamageByEntity(ZombieEscape plugin) {
        this.PLUGIN = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity().getWorld().equals(Bukkit.getWorlds().get(0))) {
            event.setCancelled(true);
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        GameArena gameArena = PLUGIN.getGameArena();

        Player damaged = (Player) event.getEntity();

        if (event.getDamager() instanceof Projectile) {
            if (gameArena.isHuman(damaged)) {
                event.setCancelled(true);
                return;
            }
        }

        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player damager = (Player) event.getDamager();

        if (gameArena.isGameRunning()) {
            if (gameArena.isSameTeam(damaged, damager)) {
                event.setCancelled(true);
            } else if (gameArena.isHuman(damaged) && gameArena.isZombie(damager)) {
                if (checkFortify(damaged)) {
                    event.setCancelled(true);
                    return;
                }

                PlayerInfectedEvent pie = new PlayerInfectedEvent(damaged, InfectReason.ZOMBIE_BITE, damager, false);
                Bukkit.getPluginManager().callEvent(pie);

                if (pie.isCancelled()) {
                    event.setCancelled(true);
                    return;
                }

                gameArena.addZombie(damaged);

                Profile damagerProfile = PLUGIN.getProfile(damager);
                if (damagerProfile != null) {
                    damagerProfile.setHumanKills(damagerProfile.getHumanKills() + 1);
                }

                damaged.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 5));
                damaged.getInventory().clear();
                damaged.getInventory().setArmorContents(null);
                gameArena.giveKit(damaged);

                Messages.PLAYER_INFECTED_OTHER.broadcast(damager.getName(), damaged.getName());

                if (gameArena.shouldEnd()) {
                    gameArena.endGame();
                }
            } else if (gameArena.isNotPlaying(damager) || gameArena.isNotPlaying(damaged)) {
                event.setCancelled(true);
            }
        }
    }

    private boolean checkFortify(Player player) {
        if (player.getInventory().getChestplate() == null) return false;
        switch (player.getInventory().getChestplate().getType()) {
            case DIAMOND_CHESTPLATE:
                player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                return true;
            case IRON_CHESTPLATE:
                player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                return true;
            case LEATHER_CHESTPLATE:
                player.getInventory().setChestplate(null);
                return true;
            default:
                return false;
        }
    }

}