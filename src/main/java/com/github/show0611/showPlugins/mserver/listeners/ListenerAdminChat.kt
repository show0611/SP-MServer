package com.github.show0611.showPlugins.mserver.listeners

import com.github.show0611.showPlugins.mserver.utilities.Utils
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent

/**
 * Created by show0611 on 2017/02/03.
 */
class ListenerAdminChat : Listener {
    @EventHandler
    fun adminChat(event: PlayerCommandPreprocessEvent) {
        val p = event.player
        var msg = event.message
        val splt = msg.split(" ")
        msg = msg.substring(splt[0].length)

        when {
            splt[0].startsWith("/operatorchat") || splt[0].startsWith("/opc") || splt[0].startsWith("/oc") -> {
                if (!p.hasPermission("sp.mserver.operatorchat")) return
                Bukkit.broadcast("§2[§3OpChat§2] §r${p.name}§a:§r ${Utils.tacc('&', msg)}", "sp.mserver.operatorchat")
            }
            splt[0].startsWith("/adminchat") || splt[0].startsWith("/adc") || splt[0].startsWith("/ac") -> {
                if (!p.hasPermission("sp.mserver.adminchat")) return
                Bukkit.broadcast("§2[§3AdminChat§2] §r${p.name}§a:§r ${Utils.tacc('&', msg)}", "sp.mserver.adminchat")
            }
        }
    }
}
