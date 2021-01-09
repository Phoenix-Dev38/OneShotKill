package com.github.phoenix_dev38.osk.utils;

import com.github.phoenix_dev38.osk.OneShotKill;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class TeamUtil {

    public static void registerNewTeam() {
        if (OneShotKill.sb.getTeam("PARTICIPANT") != null)
            OneShotKill.sb.getTeam("PARTICIPANT").unregister();
        Team team = OneShotKill.sb.registerNewTeam("PARTICIPANT");
        team.setAllowFriendlyFire(true);
        team.setCanSeeFriendlyInvisibles(false);
        team.setColor(ChatColor.GOLD);
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
    }

    public static void addPlayerToTeam(String teamName, Player player) {
        if (checkPlayerInTeam(player))
            return;
        for (Team team : OneShotKill.sb.getTeams()) {
            if (!teamName.equalsIgnoreCase(team.getName()))
                continue;
            team.addEntry(player.getName());
            player.setScoreboard(OneShotKill.sb);
            OneShotKill.playerRank.put(player, 0);
        }
    }

    public static void removePlayerFromTeam(Player player) {
        if (!checkPlayerInTeam(player))
            return;
        OneShotKill.sb.getEntryTeam(player.getName()).removeEntry(player.getName());
    }

    public static String getPlayerFromTeam(String teamName) {
        for (Team team : OneShotKill.sb.getTeams()) {
            if (!teamName.equalsIgnoreCase(team.getName()))
                continue;
            return team.getEntries().toString().replace("[", "").replace("]", "");
        }
        return "";
    }

    public static int getPlayerSizeFromTeam(String teamName) {
        for (Team team : OneShotKill.sb.getTeams()) {
            if (!teamName.equalsIgnoreCase(team.getName()))
                continue;
            return team.getSize();
        }
        return 0;
    }

    public static boolean checkPlayerInTeam(Player player) {
        for (Team team : OneShotKill.sb.getTeams()) {
            if (team.hasEntry(player.getName())) return true;
        }
        return false;
    }

    public static boolean checkPlayerInTeam(String teamName, Player player) {
        for (Team team : OneShotKill.sb.getTeams()) {
            if (teamName.equals(team.getName()) && team.hasEntry(player.getName())) return true;
        }
        return false;
    }
}
