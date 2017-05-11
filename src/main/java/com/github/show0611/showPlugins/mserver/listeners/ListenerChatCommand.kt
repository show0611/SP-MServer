package com.github.show0611.showPlugins.mserver.listeners

import com.github.show0611.showPlugins.mserver.Main
import com.github.show0611.showPlugins.mserver.utilities.SPMSData
import com.github.show0611.showPlugins.mserver.utilities.Utils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.sql.SQLException

/**
 * Created by show0611 on 17/04/19.
 */
class ListenerChatCommand : Listener {
    var m = SPMSData.Memory
    val sql = Main.pd

    @EventHandler
    fun onAsyncPlayerChat(event: AsyncPlayerChatEvent) {
        val p = event.player
        val msg = event.message
        val splt = msg.split(" ".toRegex())

        if (!splt[0].startsWith(".#")) return

        when (splt[0]) {
            ".#shop" -> {
                when (splt[1]) {
                    "set" -> {
                    }

                    "register" -> {
                        if (!Utils.check(p.player.uniqueId.toString())) return

                        val t = Utils.getOnlinePlayer(splt[2])
                        if (t == null) {
                            p.sendMessage("§2[§3ShowPlugin§2] §3Player wasn't found.")
                            return
                        }

                        m.add("${t.uniqueId}")
                        t.sendMessage("§2[§3ShowPlugin§2] §3You can register Show's BackShop guest. If you want, please enter \".#shop accept <PassWord>\".")
                    }

                    "accept" -> {
                        try {
                            if (m.contains("${p.uniqueId}")) {
                                sql.execute("insert into BackShopUsers('${p.uniqueId}', '${p.name}', '${Utils.encrypt("${p.uniqueId}${splt[2]}")}');")
                                p.sendMessage("§2[§3ShowPlugin§2] §3You can go to Show's BackShop. Enter \".#shop access <PassWord>\" to go to shop.")
                            }
                        } catch (e: SQLException) {
                        }
                    }

                    "access" -> {
                    }
                }
            }
        }
    }
}