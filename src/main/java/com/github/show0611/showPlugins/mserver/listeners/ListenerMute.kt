package com.github.show0611.showPlugins.mserver.listeners

import com.github.show0611.showPlugins.mserver.utilities.SPMSData
import com.github.show0611.showPlugins.mserver.utilities.Utils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent

/**
 * Created by show0611 on 2017/02/02.
 */
class ListenerMute : Listener {
    @EventHandler
    fun muteChat(event: AsyncPlayerChatEvent) {
        val p = event.player

        for (muter in SPMSData.ShowMuters) {
            if (muter != p.name) continue

            for (str in SPMSData.PermPlayers) {
                val pp = Utils.getOnlinePlayer(str) ?: continue

                pp.sendMessage("§2[§3MuterChat§2] §r" + p.name + ": " + event.message)
            }
            event.isCancelled = true
            break
        }
    }

    @EventHandler
    fun muteCmd(event: PlayerCommandPreprocessEvent) {
        val p = event.player

        for (muter in SPMSData.Muters) {
            if (muter == p.name) {
                event.isCancelled = true
                break
            }
        }
    }
}
