package com.github.show0611.showPlugins.mserver.listeners

import com.github.show0611.showPlugins.mserver.Main
import com.github.show0611.showPlugins.mserver.utilities.SPMSItems
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
        cursor(event)
    }

    private fun cursor(event: PlayerInteractEvent) {
        val p = event.player
        val b = event.clickedBlock
        val item = event.item
        val im = item.itemMeta

        if (SPMSItems.checkItemStack(item, SPMSItems.Cursor())) return
        when (event.action) {
            Action.LEFT_CLICK_BLOCK -> {
                if (b.type != Material.CHEST) return
                try {
                    sql.execute("insert into Cursor(Location) values('${b.location}');")
                    val rs = sql.executeQuery("select ID from Cursor where Location = ${b.location};")
                    im.lore = listOfNotNull("#SPItems", "§2Select§a:§r ${rs.getInt(1)}")
                    item.itemMeta = im
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }

            Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK -> {
            }
        }
    }
}
