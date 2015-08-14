package net.cosmosmc.mcze.core;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.*;

public class VoteManager {

    private Set<UUID> voted = new HashSet<>();
    private Map<String, Integer> votes = new HashMap<>();

    public void setMapPool(String... mapPool) {
        for (String map : mapPool) {
            votes.put(map, 0);
        }
    }

    public int getVotesOf(String map) {
        return votes.get(map);
    }

    public TreeMap<String, Integer> getOrdered() {
        OrderComparator orderComparator = new OrderComparator(votes);
        TreeMap<String, Integer> ordered = new TreeMap<>(orderComparator);
        ordered.putAll(votes);
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

        if (voted.contains(uuid)) {
            return false;
        }

        voted.add(uuid);
        votes.put(map, votes.get(map) + 1);
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