package net.cosmosmc.mcze.menus;

import net.cosmosmc.mcze.core.GameArena;
import net.cosmosmc.mcze.utils.ItemStackBuilder;
import net.cosmosmc.mcze.utils.menus.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VoteMenu extends Menu {

    private GameArena gameArena;

    public VoteMenu(String title, int size, GameArena gameArena) {
        super(title, size);
        this.gameArena = gameArena;
        int index = 0;

        for (String map : gameArena.getVoteManager().getMaps()) {
            inventory.setItem(index++, new ItemStackBuilder(Material.MAP).withName("&b" + map).withLore("&eClick To Vote!").build());
        }
    }

    @Override
    public void click(Player player, ItemStack itemStack) {
        String itemName = getFriendlyName(itemStack);

        if (itemName == null) {
            return;
        }

        for (String map : gameArena.getVoteManager().getMaps()) {
            if (map.equals(itemName)) {
                gameArena.getVoteManager().vote(player, map);
            }
        }
    }

}