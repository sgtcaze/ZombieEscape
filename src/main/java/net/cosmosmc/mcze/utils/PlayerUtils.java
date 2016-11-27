package net.cosmosmc.mcze.utils;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by JJOL on 16/08/2015.
 */
public class PlayerUtils {

    public static void refreshPlayer(Player player) {
        if (!player.isDead()) {
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setSaturation(20);
        }

        player.setFireTicks(0);
        player.setExp(0.0f);
        player.setLevel(0);



        removePotionEffects(player);
    }

    public static void removePotionEffects(Player player) {
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
    }

    // Maybe Later?
    public static void givePlayerKit() {

    }





    /*
    * Really Raw Model of Kits/Templates for giving Players Items and Effects
    * */

    public static void givePlayerTemplate(Player player, PlayerTemplate template) {

        PotionEffect[] potionEffects = (PotionEffect[]) template.getPotionEffects().toArray();

        int limit = (int) MathUtils.max(
                template.getHotbarIndex(),
                template.getInventoryIndex(),
                template.getPotionEffects().toArray().length);

        PlayerInventory inventory = player.getInventory();
        inventory.setArmorContents(template.getArmorContents());

        for (int i = 0; i < limit; i++) {
            if(i < potionEffects.length)
                player.addPotionEffect(potionEffects[i]);
            if(i <= template.getHotbarIndex())
                inventory.setItem(i, template.getHotbarItems()[i]);
            if(i <= template.getInventoryIndex())
                inventory.setItem(i + 9, template.getInventoryItems()[i]);
        }
    }

    /*
    * PlayerTemplate is a Utility Class to store a Player Model that is easy to be constructed
    * */
    public static class PlayerTemplate {


        /*
        * I used arrays instead of lists because I want to reduce the memory usage as minimal as possible
        * */

        @Getter
        private final ItemStack[] armorContents = new ItemStack[4];
        private int armorIndex = 0;
        @Getter
        private final ItemStack[] hotbarItems   = new ItemStack[9];
        @Getter
        private int hotbarIndex = 0;
        @Getter
        private final ItemStack[] inventoryItems = new ItemStack[36];
        @Getter
        private int inventoryIndex = 0;
        @Getter
        private Set<PotionEffect> potionEffects = new HashSet<>();


        public PlayerTemplate addPotion(PotionEffect potionEffect) {
            potionEffects.add(potionEffect);
            return this;
        }

        public PlayerTemplate addPotions(PotionEffect... potionEffects) {
            for(PotionEffect potionEffect : potionEffects) {
                this.potionEffects.add(potionEffect);
            }
            return this;
        }

        public PlayerTemplate addHotbarItem(ItemStack itemStack) {
            if (hotbarIndex < hotbarItems.length) {
                hotbarItems[hotbarIndex++] = itemStack;
            }
            return this;
        }

        public PlayerTemplate addHotbarItems(ItemStack... items) {
            int limit = Math.min(items.length, hotbarItems.length);
            for (; hotbarIndex < limit; hotbarIndex++) {
                hotbarItems[hotbarIndex] = items[hotbarIndex];
            }
            return this;
        }

        public PlayerTemplate addInventoryItem(ItemStack itemStack) {
            if (inventoryIndex < inventoryItems.length) {
                inventoryItems[inventoryIndex++] = itemStack;
            }
            return this;
        }

        public PlayerTemplate addInventoryItems(ItemStack... items) {
            int limit = Math.min(items.length, inventoryItems.length);
            for (; inventoryIndex < limit; inventoryIndex++) {
                inventoryItems[inventoryIndex] = items[inventoryIndex];
            }
            return this;
        }

        public PlayerTemplate addArmorComponent(ItemStack armorItem) {
            if (armorIndex < armorContents.length) {
                armorContents[armorIndex++] = armorItem;
            }
            return this;
        }

        public PlayerTemplate addArmorComponents(ItemStack... items) {
            int limit = Math.min(items.length, armorContents.length);
            for (; armorIndex < limit; armorIndex++) {
                armorContents[armorIndex] = items[armorIndex];
            }
            return this;
        }

    }

}
