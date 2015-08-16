package net.cosmosmc.mcze.core.kits;

import net.cosmosmc.mcze.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Tevin on 3/14/2015.
 */
public class KitManager {

    private Set<Kit> kits = new HashSet<>();
    private static KitManager instance;

    public static KitManager getInstance(){
        if(instance == null) instance = new KitManager();
        return instance;
    }

    public Kit getKit(String name) {
        for (Kit k : getKits()) {
            if (k.getName().equalsIgnoreCase(name)) return k;
        }
        return null;
    }

    public void loadKit(Kit kit){
        // No need to worry as any attempts to add a duplicate would fail
        kits.add(kit);
    }

    public void openGUIForPlayer(Player p){
        Inventory inv = Bukkit.createInventory(p, roundNumber(getKits().size()),"Kit Menu");
        for (Kit k : getKits()) {
            if(p.hasPermission(k.getPerm())){
                ItemStack stack = k.getIcon();
                ItemMeta meta = stack.getItemMeta();
                meta.setDisplayName(Utils.color("&a" + k.getName()));
                stack.setItemMeta(meta);
                inv.addItem(stack);
            }else{
                ItemStack stack = k.getIcon();
                ItemMeta meta = stack.getItemMeta();
                meta.setDisplayName(Utils.color("&c" + k.getName()));
                stack.setItemMeta(meta);
                inv.addItem(stack);
            }
        }
        p.openInventory(inv);
    }

    public Set<Kit> getKits() {
        return kits;
    }

    public int roundNumber(int number){
        if(number > 0 && number <= 9) return 9;
        if(number > 9 && number <= 18) return 18;
        if(number > 18 && number <= 27) return 27;
        if(number > 27 && number <= 36) return 36;
        if(number > 36 && number <= 45) return 45;
        if(number > 45 && number <= 54) return 54;
        return 54;
    }

}
