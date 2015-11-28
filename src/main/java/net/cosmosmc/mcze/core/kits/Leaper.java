package net.cosmosmc.mcze.core.kits;

import net.cosmosmc.mcze.ZombieEscape;
import net.cosmosmc.mcze.core.constants.KitAction;
import net.cosmosmc.mcze.core.constants.Messages;
import net.cosmosmc.mcze.utils.Cooldowns;
import net.cosmosmc.mcze.utils.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Leaper implements KitAction {

    private ZombieEscape plugin = JavaPlugin.getPlugin(ZombieEscape.class);

    @Override
    public void giveKit(Player player) {
        plugin.getGameArena().giveDefaults(player);
        player.getInventory().addItem(new ItemStackBuilder(Material.CARROT_STICK).withName("&bLeaper").build());
    }

    @Override
    public void interact(PlayerInteractEvent event, Player player, ItemStack itemStack) {
        if (Cooldowns.tryCooldown(player, "leaper", 1000 * 3)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 1));
            Vector vector = player.getLocation().getDirection().multiply(1.0);
            vector.setY(0.35);
            player.setVelocity(vector);
        } else {
            Messages.COOLDOWN.send(player);
        }
    }

}