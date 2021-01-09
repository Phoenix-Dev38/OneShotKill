package com.github.phoenix_dev38.osk.utils;

import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;

public class WorldBorderUtil {

    public static WorldBorder border;

    public static void setWorldBorder(String str) {
        border = Bukkit.getWorld(str).getWorldBorder();

        border.setSize(500.0);
        border.setCenter(0, 0);
        border.setDamageAmount(100.0);
    }

    public static void removeWorldBorder(String str) {
        border = Bukkit.getWorld(str).getWorldBorder();
        border.setSize(border.getSize() - 0.25);
    }
}