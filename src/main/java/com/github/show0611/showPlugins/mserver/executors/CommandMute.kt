package com.github.show0611.showPlugins.mserver.executors

import com.github.show0611.showPlugins.mserver.utilities.SPMSData
import com.github.show0611.showPlugins.mserver.utilities.Utils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

/**
 * Created by show0611 on 2017/02/02.
 */
class CommandMute : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        when (cmd.name.toLowerCase()) {
            "mute" -> {
                if (args.size == 0) return false

                val p = Utils.getOnlinePlayer(args[0].substring(1))
                if (p == null) {
                    sender.sendMessage("§2[§3ShowPlugin§2] §c" + args[0] + " はオフライン、もしくは存在しません！")
                    return true
                }

                if (args[0].startsWith("!")) {
                    SPMSData.ShowMuters.add(p.name)
                    SPMSData.Muters.add(p.name)
                    sender.sendMessage("§2[§3ShowPlugin§2] §3ミュートしました")
                } else {
                    SPMSData.HideMuters.add(p.name)
                    SPMSData.Muters.add(p.name)
                    sender.sendMessage("§2[§3ShowPlugin§2] §3ミュートしました")
                }
                return true
            }

            "unmute" -> {
                if (args.size == 0) return false

                var flag = 0
                var p1 = Utils.getOnlinePlayer(args[0])
                if (p1 == null) p1 = Utils.getOfflinePlayer(args[0])
                if (p1 == null) {
                    sender.sendMessage("§2[§3ShowPlugin§2] §c" + args[0] + " は存在しません！")
                    return true
                }

                for (str in SPMSData.ShowMuters) {
                    if (str == args[0]) {
                        SPMSData.ShowMuters.remove(str)
                        SPMSData.Muters.remove(str)
                        flag++
                        break
                    }
                }
                for (str in SPMSData.HideMuters) {
                    if (flag != 0) break
                    if (str == args[0]) {
                        SPMSData.HideMuters.remove(str)
                        SPMSData.Muters.remove(str)
                        break
                    }
                }

                if (flag == 0)
                    sender.sendMessage("§2[§3ShowPlugin§2] §b" + args[0] + " はミュートされていません")
                else
                    sender.sendMessage("§2[§3ShowPlugin§2] §a" + args[0] + " のミュートを解除しました")
                return true
            }


            "mutedshow" -> {
                if (args.size == 0) return false

                if (args[0] == "true") {
                    SPMSData.PermPlayers.add(sender.name)
                    sender.sendMessage("§2[§3ShowPlugin§2] §3値をtrueにセットしました")
                    sender.sendMessage("§2[§3ShowPlugin§2] §9ShowMuted§r: true")
                } else {
                    for (str in SPMSData.PermPlayers) {
                        if (str == args[0]) {
                            SPMSData.PermPlayers.remove(str)
                            sender.sendMessage("§2[§3ShowPlugin§2] §3値をfalseにセットしました")
                            sender.sendMessage("§2[§3ShowPlugin§2] §9ShowMuted§r: false")
                            break
                        }
                    }
                }
                return true
            }

            "listmute" -> {
                val list = SPMSData.Muters
                val limit = SPMSData.conf.getInt("Limit.MuteListup")
                var count = 0
                var page = 0
                if (args.size != 0) page = Integer.parseInt(args[0])

                for (i in list.indices) {
                    if (args.size != 0) {
                        if (count == 0) sender.sendMessage("---------- §2[§3Mute List: page $page§2] §r----------")

                        if (limit * (page - 1) > i) {
                            count++
                            continue
                        }
                        sender.sendMessage("・§a" + list[i])

                        if (count == (limit - 1) * page + (page - 1) || count == list.size - 1) {
                            sender.sendMessage("-------------------------------------")
                            return true
                        }
                        count++
                    } else {
                        if (count == 0) sender.sendMessage("---------- §2[§3Mute List: page 1§2] §r----------")

                        sender.sendMessage("・§a" + list[i])

                        if (count == limit - 1 || count == list.size - 1) {
                            sender.sendMessage("-------------------------------------")
                            return true
                        }
                        count++
                    }
                }
                return false
            }

            else -> return false
        }
    }
}
