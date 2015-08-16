package net.cosmosmc.mcze.core.kits;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public abstract class Kit {

    public abstract String getName();

    public abstract String getPerm();

    public abstract ItemStack getIcon();

    public abstract ItemStack getHelmet();

    public abstract ItemStack getChestplate();

    public abstract ItemStack getLeggings();

    public abstract ItemStack getBoots();

    public abstract List<ItemStack> extraItems();

    public abstract List<PotionEffect> getPotionEffects();

    public abstract double getHealth();

    public void giveLoadout(Player p) {
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);

        if (p.getInventory().getHelmet() == null) {
            if (getHelmet().getType() != Material.AIR) {
                ItemMeta meta = getHelmet().getItemMeta();
                getHelmet().setItemMeta(meta);
                p.getInventory().setHelmet((getHelmet()));
            }
        }

        if (p.getInventory().getChestplate() == null) {
            if (getChestplate().getType() != Material.AIR) {
                ItemMeta meta = getChestplate().getItemMeta();
                getChestplate().setItemMeta(meta);
                p.getInventory().setChestplate((getChestplate()));
            }
        }
        if (p.getInventory().getLeggings() == null) {
            if (getLeggings().getType() != Material.AIR) {
                ItemMeta meta = getLeggings().getItemMeta();
                getLeggings().setItemMeta(meta);
                p.getInventory().setLeggings((getLeggings()));
            }
        }
        if (p.getInventory().getBoots() == null) {
            if (getBoots().getType() != Material.AIR) {
                ItemMeta meta = getBoots().getItemMeta();
                getBoots().setItemMeta(meta);
                p.getInventory().setBoots((getBoots()));
            }
        }

        if (extraItems() != null && !extraItems().isEmpty()) {
            for (ItemStack i : extraItems()) {
                p.getInventory().setItem(extraItems().indexOf(i),i);
            }
        }
        p.getActivePotionEffects().clear();
        if (getPotionEffects() != null) {
            for(PotionEffect effect : getPotionEffects()){
                p.addPotionEffect(effect);
            }
        }

        p.setMaxHealth(getHealth());
        p.setHealth(p.getMaxHealth());
        p.setFoodLevel(40);

        p.updateInventory();
    }

    public PotionEffect createEffect(PotionEffectType type, int potency) {
        return new PotionEffect(type, 999999, potency - 1);
    }

    public PotionEffect createEffect(PotionEffectType type, int potency, int duration) {
        return new PotionEffect(type, duration, potency - 1);
    }

    public ItemStack colorArmor(ItemStack armor, Color color) {
        if (isApplicable(armor)) {
            LeatherArmorMeta meta = (LeatherArmorMeta) armor.getItemMeta();
            meta.setColor(color);
            armor.setItemMeta(meta);
            return armor;
        }
        return armor;
    }

    private boolean isApplicable(ItemStack item) {
        Material material = item.getType();
        switch (material) {
            case LEATHER_HELMET:
                return true;
            case LEATHER_CHESTPLATE:
                return true;
            case LEATHER_LEGGINGS:
                return true;
            case LEATHER_BOOTS:
                return true;
            default:
                return false;
        }
    }
}
