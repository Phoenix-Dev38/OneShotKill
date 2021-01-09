package com.github.phoenix_dev38.osk;

import com.github.phoenix_dev38.osk.utils.GameUtil;
import com.github.phoenix_dev38.osk.utils.TeamUtil;
import com.github.phoenix_dev38.osk.utils.WorldBorderUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class GameRunnable extends BukkitRunnable {

    int i = 0;

    @Override
    public void run() {
        if (!OneShotKill.isGame) {
            cancel();
            Bukkit.getScheduler().cancelTasks(OneShotKill.getInstance());
            return;
        }
        i++;
        if (i >= 30)
            OneShotKill.bossBar.setTitle("§e経過時間 §c-" + GameUtil.convertMinuteTimes((i - 30)) + "-");
        if (i >= 25 && i <= 29) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendTitle("§cOneShotKill is starting in", (30 - i) + "", 10, 20, 10);
                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            }
        } else if (i == 30) {
            OneShotKill.isPrepare = false;
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendTitle("§aOneShotKill is started!", "", 10, 20, 10);
                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_ENDER_DRAGON_AMBIENT, 1, 1);
                OneShotKill.bossBar.addPlayer(onlinePlayer);
            }
            PlayerListener.rank = TeamUtil.getPlayerSizeFromTeam("PARTICIPANT");
            Bukkit.getScheduler().scheduleSyncRepeatingTask(OneShotKill.getInstance(), () -> Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 8 * 20, 0))), 0, 120 * 20);
        } else if (i == 60 * 5)
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendTitle("§eWarning!", "§c範囲収縮が開始されます。", 10, 100, 10));
        else if (i > 60 * 5)
            WorldBorderUtil.removeWorldBorder("osk");
    }
}
