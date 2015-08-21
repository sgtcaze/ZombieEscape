package net.cosmosmc.mcze.core;

import lombok.AllArgsConstructor;
import net.cosmosmc.mcze.events.PlayerMapVoteEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class VoteManager {

    private final Set<UUID> VOTED = new HashSet<>();
    private final Map<String, Integer> VOTES = new HashMap<>();

    public void setMapPool(String... mapPool) {
        for (String map : mapPool) {
            VOTES.put(map, 0);
        }
    }

    public int getVotesOf(String map) {
        return VOTES.get(map);
    }

    public TreeMap<String, Integer> getOrdered() {
        OrderComparator orderComparator = new OrderComparator(VOTES);
        TreeMap<String, Integer> ordered = new TreeMap<>(orderComparator);
        ordered.putAll(VOTES);
        return ordered;
    }

    public Set<String> getTopMaps() {
        return getOrdered().keySet();
    }

    public Collection<Integer> getTopVotes() {
        return getOrdered().values();
    }

    public boolean vote(Player player, String map) {
        UUID uuid = player.getUniqueId();

        if (VOTED.contains(uuid)) {
            return false;
        }
        //Here because if they already voted it should not call the event
        Bukkit.getPluginManager().callEvent(new PlayerMapVoteEvent());
        //Able to cancel it?
        VOTED.add(uuid);
        VOTES.put(map, VOTES.get(map) + 1);
        return true;
    }

    @AllArgsConstructor
    class OrderComparator implements Comparator<String> {

        private Map<String, Integer> input;

        public int compare(String a, String b) {
            return input.get(a) >= input.get(b) ? -1 : 1;
        }

    }

}