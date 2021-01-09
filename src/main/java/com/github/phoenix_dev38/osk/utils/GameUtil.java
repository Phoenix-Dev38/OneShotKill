package com.github.phoenix_dev38.osk.utils;

import com.github.phoenix_dev38.osk.GameRunnable;
import com.github.phoenix_dev38.osk.OneShotKill;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GameUtil {

    public static void startGame() {
        OneShotKill.isGame = true;

        ItemStack bow = new ItemStack(Material.BOW);
        bow.getItemMeta().setUnbreakable(true);
        bow.setItemMeta(bow.getItemMeta());
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.setGameMode(GameMode.ADVENTURE);
            onlinePlayer.getInventory().clear();
            onlinePlayer.getInventory().addItem(bow);
            onlinePlayer.getInventory().addItem(new ItemStack(Material.ARROW));
            onlinePlayer.getInventory().addItem(new ItemStack(Material.COMPASS));
            for (PotionEffect potionEffect : onlinePlayer.getActivePotionEffects())
                onlinePlayer.removePotionEffect(potionEffect.getType());
            onlinePlayer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 30 * 20, 99));
            onlinePlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30 * 20, 3));
            TeamUtil.addPlayerToTeam("PARTICIPANT", onlinePlayer);
        }
        LocUtil.teleportGameLocs();
        WorldBorderUtil.setWorldBorder("osk");
        new GameRunnable().runTaskTimer(OneShotKill.getInstance(), 20, 20);
    }

    public static void resetGame() {
        if (!OneShotKill.isGame)
            return;
        Bukkit.broadcastMessage(OneShotKill.PREFIX + "§aゲームが終了しました。");
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.setGameMode(GameMode.ADVENTURE);
            onlinePlayer.getInventory().clear();
            LocUtil.teleportMainSpawn(onlinePlayer);
            for (PotionEffect potionEffect : onlinePlayer.getActivePotionEffects())
                onlinePlayer.removePotionEffect(potionEffect.getType());
        }
        WorldBorderUtil.setWorldBorder("osk");

        OneShotKill.isGame = true;
        OneShotKill.isGame = false;

        OneShotKill.settingMode.clear();

        OneShotKill.playerRank.clear();

        OneShotKill.bossBar = Bukkit.getServer().createBossBar("§9§l待機中", BarColor.BLUE, BarStyle.SOLID);

        Bukkit.getScheduler().cancelTasks(OneShotKill.getInstance());
    }

    public static Player getNearestPlayer(Player player) {
        double distance = Double.POSITIVE_INFINITY;
        Player target = null;
        for (Entity entity : player.getNearbyEntities(200, 200, 200)) {
            if (!(entity instanceof Player))
                continue;
            if (entity == player) continue;
            if (!TeamUtil.checkPlayerInTeam(((Player) entity))) continue;
            double distanceTo = player.getLocation().distance(entity.getLocation());
            if (distanceTo > distance)
                continue;
            distance = distanceTo;
            target = (Player) entity;
        }
        return target;
    }

    public static String convertMinuteTimes(int time) {
        int second = time % 60;
        int minute = time / 60;
        if (String.valueOf(second).length() == 2) {
            return minute + " : " + second;
        }
        return minute + " : 0" + second;
    }
}
