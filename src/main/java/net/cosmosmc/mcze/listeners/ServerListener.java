package net.cosmosmc.mcze.listeners;

import lombok.AllArgsConstructor;
import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.constants.Messages;
import net.cosmosmc.mcze.core.tasks.ReloadTask;
import net.cosmosmc.mcze.api.events.ProfileLoadedEvent;
import net.cosmosmc.mcze.profiles.Profile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@AllArgsConstructor
public class ServerListener implements Listener {

    private ZombieEscape plugin;

    /* This is for listening to loaded profiles, no biggie */
    @EventHandler
    public void onProfileLoad(ProfileLoadedEvent event) {
        Profile profile = event.getProfile();
        Player player = Bukkit.getPlayer(profile.getUuid());
        if (player != null) {
            Messages.CLASS_INFORMATION.send(player, profile.getHumanKit().getName(), profile.getZombieKit().getName());
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        switch (event.getSlotType()) {
            case CRAFTING:
            case ARMOR:
            case FUEL:
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        event.setCancelled(!event.getPlayer().isOp());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        event.setCancelled(event.getPlayer().getItemInHand().getType() == Material.SKULL_ITEM || !event.getPlayer().isOp());
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity().getWorld().equals(Bukkit.getWorlds().get(0)) || event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onStarve(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onThrow(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();

        if (projectile instanceof Snowball && projectile.getShooter() instanceof Player) {
            Player player = (Player) projectile.getShooter();
            if (player.getItemInHand().getType() == Material.SNOW_BALL && player.getItemInHand().getAmount() == 0) {
                // ran out of snowballs
                new ReloadTask(player).runTaskLater(plugin, 20 * 3);
                Messages.RELOADING.send(player);
            }
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        if (projectile instanceof Arrow) {
            projectile.remove();
        } else if (projectile instanceof Snowball && event.getEntity() instanceof Player) {
            ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 2, 1));
        } else if (projectile instanceof Egg && projectile.getShooter() instanceof Player) {
            projectile.getWorld().createExplosion(projectile.getLocation(), 0.0F);

            for (Entity entity : projectile.getNearbyEntities(5, 5, 5)) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    if (plugin.getGameArena().isZombie(player)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 1));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 5, 1));
                    }
                }
            }
        }
    }

    /**
     * This is specific to my test server to prevent Crop trample.
     */
    @EventHandler
    public void onTrample(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) {
            return;
        }

        if (e.getAction() == Action.PHYSICAL) {
            Block block = e.getClickedBlock();

            Material material = block.getType();

            if (material == Material.CROPS || material == Material.SOIL) {
                e.setUseInteractedBlock(PlayerInteractEvent.Result.DENY);
                e.setCancelled(true);

                block.setType(material);
                block.setData(block.getData());
            }
        }
    }

}
