package com.github.show0611.showPlugins.mserver.executors;

import com.github.show0611.showPlugins.mserver.utilities.SPMSData;
import com.github.show0611.showPlugins.mserver.utilities.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by show0611 on 2017/02/02.
 */
public class CommandMute implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        switch (cmd.getName().toLowerCase()) {
        case ("mute"):
            if (args.length == 0) return false;

            Player p = Utils.getOnlinePlayer(args[0].substring(1));
            if (p == null) {
                sender.sendMessage("§2[§3ShowPlugin§2] §c" + args[0] + " はオフライン、もしくは存在しません！");
                return true;
            }

            if (args[0].startsWith("!")) {
                SPMSData.ShowMuters.add(p.getName());
                SPMSData.Muters.add(p.getName());
                sender.sendMessage("§2[§3ShowPlugin§2] §3ミュートしました");
            } else {
                SPMSData.HideMuters.add(p.getName());
                SPMSData.Muters.add(p.getName());
                sender.sendMessage("§2[§3ShowPlugin§2] §3ミュートしました");
            }
            return true;

        case ("unmute"):
            if (args.length == 0) return false;

            int flag = 0;
            Player p1 = Utils.getOnlinePlayer(args[0]);
            if (p1 == null) p1 = Utils.getOfflinePlayer(args[0]);
            if (p1 == null) {
                sender.sendMessage("§2[§3ShowPlugin§2] §c" + args[0] + " は存在しません！");
                return true;
            }

            for (String str : SPMSData.ShowMuters) {
                if (str.equals(args[0])) {
                    SPMSData.ShowMuters.remove(str);
                    SPMSData.Muters.remove(str);
                    flag++;
                    break;
                }
            }
            for (String str : SPMSData.HideMuters) {
                if (flag != 0) break;
                if (str.equals(args[0])) {
                    SPMSData.HideMuters.remove(str);
                    SPMSData.Muters.remove(str);
                    break;
                }
            }

            if (flag == 0) sender.sendMessage("§2[§3ShowPlugin§2] §b" + args[0] + " はミュートされていません");
            else sender.sendMessage("§2[§3ShowPlugin§2] §a" + args[0] + " のミュートを解除しました");
            return true;


        case ("mutedshow"):
            if (args.length == 0) return false;

            if (args[0].equals("true")) {
                SPMSData.PermPlayers.add(sender.getName());
                sender.sendMessage("§2[§3ShowPlugin§2] §3値をtrueにセットしました");
                sender.sendMessage("§2[§3ShowPlugin§2] §9ShowMuted§r: true");
            } else {
                for (String str : SPMSData.PermPlayers) {
                    if (str.equals(args[0])) {
                        SPMSData.PermPlayers.remove(str);
                        sender.sendMessage("§2[§3ShowPlugin§2] §3値をfalseにセットしました");
                        sender.sendMessage("§2[§3ShowPlugin§2] §9ShowMuted§r: false");
                        break;
                    }
                }
            }
            return true;

        case ("listmute"):
            List<String> list = SPMSData.Muters;
            int limit = SPMSData.conf.getInt("Limit.MuteListup");
            int count = 0;
            int page = 0;
            if (args.length != 0) page = Integer.parseInt(args[0]);

            for (int i = 0; i < list.size(); i++) {
                if (args.length != 0) {
                    if (count == 0) sender.sendMessage("---------- §2[§3Mute List: page " + page + "§2] §r----------");

                    if (limit * (page - 1) > i) {
                        count++;
                        continue;
                    }
                    sender.sendMessage("・§a" + list.get(i));

                    if (count == (limit - 1) * page + (page - 1) || count == list.size() - 1) {
                        sender.sendMessage("-------------------------------------");
                        return true;
                    }
                    count++;
                } else {
                    if (count == 0) sender.sendMessage("---------- §2[§3Mute List: page 1§2] §r----------");

                    sender.sendMessage("・§a" + list.get(i));

                    if (count == (limit - 1) || count == list.size() - 1) {
                        sender.sendMessage("-------------------------------------");
                        return true;
                    }
                    count++;
                }
            }
            return false;

        default:
            return false;
        }
    }
}
