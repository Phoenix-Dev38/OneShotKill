package com.github.phoenix_dev38.osk;

import com.github.phoenix_dev38.osk.utils.GameUtil;
import com.github.phoenix_dev38.osk.utils.LocUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("oneshotkill")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "このコマンドはゲーム内から実行してください。");
                return true;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("oneshotkill.use")) {
                player.sendMessage("§cあなたは権限を持っていません。");
                return true;
            }
            switch (args.length) {
                case 0:
                    sendPlayerOfCommandDescription(player);
                    break;
                case 1:
                    if (args[0].equalsIgnoreCase("help"))
                        sendPlayerOfCommandDescription(player);
                    else if (args[0].equalsIgnoreCase("sm")) {
                        if (!OneShotKill.settingMode.contains(player.getName())) {
                            OneShotKill.settingMode.add(player.getName());
                            player.sendMessage(OneShotKill.PREFIX + "§aSettingModeをオンにしました。");
                        } else {
                            OneShotKill.settingMode.remove(player.getName());
                            player.sendMessage(OneShotKill.PREFIX + "§aSettingModeをオフにしました。");
                        }
                    } else if (args[0].equalsIgnoreCase("debug"))
                        System.out.println("Debug");
                    else if (args[0].equalsIgnoreCase("start")) {
                        if (OneShotKill.isGame) {
                            player.sendMessage("§c既にゲームは開始されています。リセットする場合は /osk reset を実行してください。");
                            return true;
                        }
                        GameUtil.startGame();
                    } else if (args[0].equalsIgnoreCase("reset")) {
                        if (!OneShotKill.isGame) {
                            player.sendMessage("§c既にゲームは終了されています。開始する場合は /osk start を実行してください。");
                            return true;
                        }
                        GameUtil.resetGame();
                    } else if (args[0].equalsIgnoreCase("reload")) {
                        YamlFile.loadLocation();
                        YamlFile.loadSetting();
                        player.sendMessage(OneShotKill.PREFIX + "§alocation.yml, setting.ymlを再読み込みました。");
                    }
                    break;
                case 2:
                    if (args[0].equalsIgnoreCase("set")) {
                        if (args[1].equalsIgnoreCase("main")) {
                            LocUtil.setMainSpawn(player);
                            player.sendMessage(OneShotKill.PREFIX + "§aメインスポーン地点を設定しました。");
                        } else player.sendMessage("§cmainを指定してください。");
                    }
                    break;
            }
        }
        return false;
    }

    private void sendPlayerOfCommandDescription(Player player) {
        player.sendMessage("§c--- §b§lOneShotKill §bCommand List §c---");
        player.sendMessage("§aOneShotKillを[osk]と省略することもできます。");
        player.sendMessage("§b/osk help §a<当プラグインのコマンドリストを表示します。>");
        player.sendMessage("§b/osk sm §a<SettingModeの切り替えを行います。>");
        player.sendMessage("§b/osk start §a<ゲームを開始します。>");
        player.sendMessage("§b/osk reset §a<ゲームをリセットします。>");
        player.sendMessage("§b/osk reload §a<location.yml, setting.ymlを再読み込みします。>");
        player.sendMessage("§b/osk set main §a<Werewolfのメインスポーン地点を設定します。>");
    }
}
