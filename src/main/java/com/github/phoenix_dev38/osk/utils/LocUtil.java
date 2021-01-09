package com.github.phoenix_dev38.osk.utils;

import com.github.phoenix_dev38.osk.YamlFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocUtil {

    public static void setMainSpawn(Player player) {
        Location loc = player.getLocation();
        String world = loc.getWorld().getName();
        double x = loc.getBlockX() + 0.5;
        int y = loc.getBlockY();
        double z = loc.getBlockZ() + 0.5;

        YamlFile.LOCATIONYAML.set("MainSpawn.world", world);
        YamlFile.LOCATIONYAML.set("MainSpawn.x", x);
        YamlFile.LOCATIONYAML.set("MainSpawn.y", y);
        YamlFile.LOCATIONYAML.set("MainSpawn.z", z);
        YamlFile.saveLocation();
    }

    public static void teleportMainSpawn(Player player) {
        if (YamlFile.LOCATIONYAML.getString("MainSpawn.world").isEmpty())
            return;
        String world = YamlFile.LOCATIONYAML.getString("MainSpawn.world");
        double x = YamlFile.LOCATIONYAML.getDouble("MainSpawn.x");
        int y = YamlFile.LOCATIONYAML.getInt("MainSpawn.y");
        double z = YamlFile.LOCATIONYAML.getDouble("MainSpawn.z");
        Location loc = new Location(Bukkit.getWorld(world), x, y, z);

        player.teleport(loc);
    }

    public static void teleportGameLocs() {
        List<String> blockNameList = new ArrayList<>();
        for (String blockName : YamlFile.LOCATIONYAML.getKeys(false)) {
            if (!blockName.contains("MainSpawn"))
                blockNameList.add(blockName);
        }
        Collections.shuffle(blockNameList);

        List<String> blockLocList = new ArrayList<>(YamlFile.LOCATIONYAML.getStringList(blockNameList.get(0)));
        Collections.shuffle(blockLocList);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            String world = blockLocList.get(0).split(", ")[0];
            double x = Double.parseDouble(blockLocList.get(0).split(", ")[1]) + 0.5;
            double y = Double.parseDouble(blockLocList.get(0).split(", ")[2]) + 1.5;
            double z = Double.parseDouble(blockLocList.get(0).split(", ")[3]) + 0.5;
            Location loc = new Location(Bukkit.getWorld(world), x, y, z);

            onlinePlayer.teleport(loc);
            blockLocList.remove(blockLocList.get(0));
        }
    }
}
