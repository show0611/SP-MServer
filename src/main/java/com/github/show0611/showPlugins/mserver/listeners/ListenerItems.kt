package com.github.show0611.showPlugins.mserver.listeners

import com.github.show0611.showPlugins.mserver.Main
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import java.sql.SQLException

/**
 * Created by show0611 on 2017/02/10.
 */
class ListenerItems : Listener {
    internal var sql = Main.item

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        chestLinker(event)
    }

    /*
     * 未実装
     */
    private fun chestLinker(event: PlayerInteractEvent) {
        val p = event.player
        val b = event.clickedBlock

        when (event.action) {
            Action.LEFT_CLICK_BLOCK -> {
                if (b.type != Material.CHEST) return
                try {
                    sql.execute("insert into ChestLinker(Location) values('" + b.location.toString() + "');")
                } catch (e: SQLException) {
                    e.printStackTrace()
                }

            }

            Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK -> {
            }
        }
    }
}
