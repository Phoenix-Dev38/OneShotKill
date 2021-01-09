package com.github.phoenix_dev38.osk;

import com.github.phoenix_dev38.osk.utils.GameUtil;
import com.github.phoenix_dev38.osk.utils.TeamUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener {

    public static int rank;

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL))
            event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (OneShotKill.isPrepare) {
            event.setCancelled(true);
            return;
        }
        if (!OneShotKill.isGame)
            return;
        if (!(event.getDamager() instanceof Arrow && event.getEntity() instanceof Player)) {
            event.setCancelled(true);
            return;
        }
        Player damager = (Player) ((Arrow) event.getDamager()).getShooter();
        if (!(damager.getInventory().getItem(EquipmentSlot.HAND).getType().equals(Material.BOW)
                || damager.getInventory().getItem(EquipmentSlot.OFF_HAND).getType().equals(Material.BOW)))
            return;
        Player entity = (Player) event.getEntity();
        if (!OneShotKill.playerRank.containsKey(damager))
            return;
        if (damager == entity) {
            damager.setVelocity(damager.getLocation().getDirection().multiply(3));
            damager.playSound(damager.getLocation(), Sound.ENTITY_CHICKEN_DEATH, 1, 1);
            damager.playSound(damager.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
            event.setCancelled(true);
            return;
        }
        entity.setGameMode(GameMode.SPECTATOR);
        if (TeamUtil.checkPlayerInTeam(entity))
            TeamUtil.removePlayerFromTeam(entity);
        damager.getWorld().strikeLightningEffect(damager.getLocation());
        damager.getWorld().playSound(damager.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        damager.getInventory().addItem(new ItemStack(Material.ARROW));
        damager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 8 * 20, 3));
        OneShotKill.playerRank.put(damager, OneShotKill.playerRank.get(damager) + 10);
        damager.sendActionBar("§e§lポイント数: " + OneShotKill.playerRank.get(damager));

        Bukkit.broadcastMessage(OneShotKill.PREFIX + "§a§l" + damager.getName() + "§fが§c§l" + entity.getName() + "§fを倒した。§e順位:§b§l" + rank + "§e位");
        rank--;
        if (rank <= 20 && rank >= 11)
            OneShotKill.playerRank.put(entity, OneShotKill.playerRank.get(entity) + 15);
        else if (rank == 10)
            OneShotKill.playerRank.put(entity, OneShotKill.playerRank.get(entity) + 30);
        else if (rank <= 9 && rank >= 6)
            OneShotKill.playerRank.put(entity, OneShotKill.playerRank.get(entity) + 40);
        else if (rank == 5)
            OneShotKill.playerRank.put(entity, OneShotKill.playerRank.get(entity) + 50);
        else if (rank == 4)
            OneShotKill.playerRank.put(entity, OneShotKill.playerRank.get(entity) + 55);
        else if (rank == 3)
            OneShotKill.playerRank.put(entity, OneShotKill.playerRank.get(entity) + 70);
        else if (rank == 2)
            OneShotKill.playerRank.put(entity, OneShotKill.playerRank.get(entity) + 80);
        else if (rank == 1) {
            OneShotKill.playerRank.put(damager, OneShotKill.playerRank.get(damager) + 100);
            Bukkit.broadcastMessage("§6§l" + TeamUtil.getPlayerFromTeam("PARTICIPANT") + "§aの獲得ポイント数: §e§l" + OneShotKill.playerRank.get(Bukkit.getPlayer(TeamUtil.getPlayerFromTeam("PARTICIPANT"))) + "ポイント");
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendTitle("§eWinner!", TeamUtil.getPlayerFromTeam("PARTICIPANT"), 40, 200, 40);
                Ranking.showRanking(onlinePlayer, OneShotKill.playerRank.size());
            }
            GameUtil.resetGame();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (TeamUtil.checkPlayerInTeam(event.getPlayer()))
            rank--;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!OneShotKill.isGame)
            return;
        Player player = event.getEntity();
        if (!OneShotKill.playerRank.containsKey(player))
            return;
        event.setDeathMessage(null);
        Bukkit.broadcastMessage(OneShotKill.PREFIX + "§c" + player.getName() + "§fが倒れた。§e順位:§b§l" + rank + "§e位");
        rank--;
        if (rank == 1) {
            OneShotKill.playerRank.put(player, OneShotKill.playerRank.get(player) + 100);
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendTitle("§eWinner!", TeamUtil.getPlayerFromTeam("PARTICIPANT"), 10, 60, 10));
            Bukkit.broadcastMessage("§6§l" + TeamUtil.getPlayerFromTeam("PARTICIPANT") + "§aの獲得ポイント数: §e§l" + OneShotKill.playerRank.get(Bukkit.getPlayer(TeamUtil.getPlayerFromTeam("PARTICIPANT"))) + "ポイント");
            GameUtil.resetGame();
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!OneShotKill.isGame)
            return;
        if (event.getFrom().getPitch() != event.getTo().getPitch() || event.getFrom().getYaw() != event.getTo().getYaw())
            return;
        Player player = event.getPlayer();
        if (event.getFrom().getY() < event.getTo().getY() && player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.EMERALD_BLOCK))
            player.setVelocity(player.getVelocity().add(new Vector(0, 1, 0)));
        if (!(player.hasPotionEffect(PotionEffectType.BLINDNESS) && player.hasPotionEffect(PotionEffectType.SLOW)))
            return;
        event.setCancelled(true);
        player.teleport(player.getLocation());
        player.teleport(event.getFrom());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!OneShotKill.isGame)
            return;
        if (!event.getHand().equals(EquipmentSlot.HAND))
            return;
        Player player = event.getPlayer();
        if (!player.getInventory().getItem(EquipmentSlot.HAND).getType().equals(Material.COMPASS))
            return;
        Player nearestPlayer;
        if (GameUtil.getNearestPlayer(player) == null) {
            player.sendActionBar("§cプレイヤーが見つかりませんでした。");
            return;
        }
        nearestPlayer = GameUtil.getNearestPlayer(player);
        player.sendActionBar("§a§lID:§a" + nearestPlayer.getName() + " §e§l距離:§e" + String.format("%.1f", player.getLocation().distance(nearestPlayer.getLocation())));
        player.setCompassTarget(nearestPlayer.getLocation());
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!OneShotKill.isGame)
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickupArrow(PlayerPickupArrowEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!OneShotKill.isGame)
            return;
        if (!(event.getEntity() instanceof Arrow && event.getEntity().getShooter() instanceof Player))
            return;
        Player shooter = (Player) event.getEntity().getShooter();
        Bukkit.getScheduler().runTaskLater(OneShotKill.getInstance(), () -> {
            if (!shooter.getInventory().contains(Material.ARROW))
                shooter.getInventory().addItem(new ItemStack(Material.ARROW));
        }, 5 * 20);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (OneShotKill.isGame)
            return;
        Player player = event.getPlayer();
        if (!OneShotKill.settingMode.contains(player.getName()))
            return;
        if (YamlFile.SETTINGYAML.getStringList("LocBlocks").isEmpty()) {
            player.sendMessage("§cOneShotKill/setting.yml内にブロック名を指定してください。");
            return;
        }
        Block block = event.getBlock();
        if (!YamlFile.SETTINGYAML.getStringList("LocBlocks").contains(block.getType().name()))
            return;
        String blockLoc = block.getWorld().getName() + ", " + block.getLocation().getBlockX() + ", " + block.getLocation().getBlockY() + ", " + block.getLocation().getBlockZ();
        List<String> locList = new ArrayList<>();
        locList.add(blockLoc);
        for (String blockName : YamlFile.LOCATIONYAML.getKeys(false)) {
            if (blockName.equalsIgnoreCase(block.getType().name()))
                locList.addAll(YamlFile.LOCATIONYAML.getStringList(blockName));
        }
        YamlFile.LOCATIONYAML.set(block.getType().name(), locList);
        YamlFile.saveLocation();

        player.sendMessage("§a" + block.getType().name() + "§rで§e" + blockLoc + "§rを設定しました。");
        event.setCancelled(true);
    }
}
