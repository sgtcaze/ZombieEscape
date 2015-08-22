package net.cosmosmc.mcze.utils;


import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public class ItemStackBuilder {

    private final ItemStack	is;

    // Made a easier ItemStackBuilder that is chainable

    public ItemStackBuilder(final Material mat) {
        is = new ItemStack(mat);
    }

    public ItemStackBuilder(final ItemStack is) {
        this.is = is;
    }

    public ItemStackBuilder amount(final int amount) {
        is.setAmount(amount);
        return this;
    }

    public ItemStackBuilder name(final String name) {
        final ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(Utils.color(name));
        is.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder lore(final String name) {
        final ItemMeta meta = is.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        lore.add(Utils.color(name));
        meta.setLore(lore);
        is.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder durability(final int durability) {
        is.setDurability((short) durability);
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemStackBuilder data(final int data) {
        is.setData(new MaterialData(is.getType(), (byte) data));
        return this;
    }

    public ItemStackBuilder enchantment(final Enchantment enchantment, final int level) {
        is.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemStackBuilder enchantment(final Enchantment enchantment) {
        is.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemStackBuilder type(final Material material) {
        is.setType(material);
        return this;
    }

    public ItemStackBuilder clearLore() {
        final ItemMeta meta = is.getItemMeta();
        meta.setLore(new ArrayList<String>());
        is.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder clearEnchantments() {
        for (final Enchantment e : is.getEnchantments().keySet()) {
            is.removeEnchantment(e);
        }
        return this;
    }

    public ItemStackBuilder color(Color color) {
        if (is.getType() == Material.LEATHER_BOOTS || is.getType() == Material.LEATHER_CHESTPLATE || is.getType() == Material.LEATHER_HELMET
                || is.getType() == Material.LEATHER_LEGGINGS) {
            LeatherArmorMeta meta = (LeatherArmorMeta) is.getItemMeta();
            meta.setColor(color);
            is.setItemMeta(meta);
            return this;
        } else {
            throw new IllegalArgumentException("color is only applicable for leather armor!");
        }
    }

    public ItemStack build() {
        return is;
    }

}
