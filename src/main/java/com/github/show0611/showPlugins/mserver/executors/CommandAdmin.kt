package com.github.show0611.showPlugins.mserver.executors

import com.github.show0611.showPlugins.mserver.Main
import com.github.show0611.showPlugins.mserver.utilities.SPMSData
import com.github.show0611.showPlugins.mserver.utilities.Utils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer
import java.sql.SQLException
import java.util.LinkedHashMap
import kotlin.properties.Delegates

/**
 * Created by show0611 on 17/04/13.
 */
class CommandAdmin : CommandExecutor {
    val pd = SPMSData.PlayerData
    val home = SPMSData.HomeData

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        val cp = sender as CraftPlayer

        when (cmd.name.toLowerCase()) {
            "administrator" -> {
                if (args.isEmpty()) return false

                when (args[0].toLowerCase()) {
                    "bedtp" -> {
                        try {
                            if (args[1].isEmpty()) {
                                cp.teleport(Utils.toLocation(pd[cp.uniqueId.toString()]!!["BedLocation"]))
                                return true

                            } else if (Utils.getOfflinePlayer(args[1]) != null) {
                                val p = Utils.getOfflinePlayer(args[1])!!
                                cp.teleport(Utils.toLocation(pd[p.uniqueId.toString()]!!["BedLocation"]))
                                return true

                            }
                        } catch(e: SQLException) {
                            e.printStackTrace()
                            return false
                        }
                    }

                    "home" -> {
                        if (args[1].isNotEmpty()) {
                            val p = Utils.getOfflinePlayer(args[1])!! as CraftPlayer

                            for (m in SPMSData.HomeData[p.uniqueId.toString()]!!) {
                                if (m["Name"] == args[2]) {
                                    p.teleport(Utils.toLocation(m["Location"].toString()))
                                    return true
                                }
                            }
                            return false
                        } else {
                            try {
                                cp.teleport(Utils.toLocation(SPMSData.PlayerData[cp.uniqueId.toString()]!!["DefaultHome"]))
                            } catch (e: Exception) {
                                cp.sendMessage("§2[§3ShowPlugin§2] §cHomeが設定されていません。/sethomeコマンドを実行してから使用してください。")
                            }

                            return true
                        }
                    }

                    "llltp" -> {
                        if (args[1].isEmpty()) return false

                        if (Utils.getOfflinePlayer(args[1]) == null) {
                            cp.sendMessage("§2[§3ShowPlugin§2] §c指定された相手が見つかりませんでした。名前に間違いが無いか確認して、もう一度実行してください。")
                        }

                        val p = Utils.getOfflinePlayer(args[1])!!
                        try {
                            p.teleport(Utils.toLocation(pd[p.uniqueId.toString()]!!["LastLogoutLocation"]))
                        } catch (e: Exception) {
                            p.sendMessage("§2[§3ShowPlugin§2] §c指定された相手の最終ログアウト場所が見つかりませんでした。")
                        }

                        return true
                    }

                    else -> return false
                }
                return true
            }

            else -> return false
        }
    }
}