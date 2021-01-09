package com.github.phoenix_dev38.osk;

import com.github.phoenix_dev38.osk.utils.TeamUtil;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class OneShotKill extends JavaPlugin {

    public static boolean isPrepare = true;
    public static boolean isGame = false;

    public final static String PREFIX = "§b§l[OSK] §r";

    public static List<String> settingMode = new ArrayList<>();

    public static Map<Player, Integer> playerRank = new HashMap<>();

    private static OneShotKill instance;

    public static Scoreboard sb;

    public static BossBar bossBar;

    @Override
    public void onEnable() {
        instance = this;
        sb = Bukkit.getScoreboardManager().getNewScoreboard();
        bossBar = getServer().createBossBar("§9§l待機中", BarColor.BLUE, BarStyle.SOLID);

        TeamUtil.registerNewTeam();

        getCommand("oneshotkill").setExecutor(new PlayerCommand());

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        YamlFile.loadLocation();
        YamlFile.loadSetting();

        YamlFile.saveLocation();
        YamlFile.saveSetting();

        if (YamlFile.SETTINGYAML.get("LocBlocks") == null) {
            List<String> blockList = new ArrayList<>();
            blockList.add("DIAMOND_BLOCK");
            YamlFile.SETTINGYAML.set("LocBlocks", blockList);
            YamlFile.saveSetting();
        }
    }

    public static OneShotKill getInstance() {
        return instance;
    }
}
