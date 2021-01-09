package com.github.phoenix_dev38.osk;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Ranking {

    private static Map<String, Integer> getPlayerRank() {
        LinkedHashMap<String, Integer> ranking = new LinkedHashMap<>();
        OneShotKill.playerRank.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .forEach(player -> ranking.put(String.valueOf(player.getKey()), player.getValue()));
        return ranking;
    }

    public static void showRanking(Player player, int number) {
        int rank = 1;
        player.sendMessage("");
        player.sendMessage("§c--- §b§lOneShotKill §9§lRanking-Top§b§l" + number + " §a§lPoints §c---");
        player.sendMessage("");
        for (Map.Entry<String, Integer> ranking : getPlayerRank().entrySet()) {
            if (rank > number)
                break;
            player.sendMessage("§b§l#" + rank + " §a§l" + Bukkit.getPlayer(ranking.getKey()) + " §7§l➤ §6"
                    + ranking.getValue() + "§cポイント");
            rank++;
        }
        player.sendMessage("");
    }
}
