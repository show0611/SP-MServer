package com.github.show0611.showPlugins.mserver.listeners

import com.github.show0611.showPlugins.mserver.Main
import com.github.show0611.showPlugins.mserver.utilities.SPMSData
import com.github.show0611.showPlugins.mserver.utilities.Utils
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.metadata.FixedMetadataValue
import java.sql.SQLException
import java.util.*

/**
 * Created by show0611 on 2017/01/29.
 */
class ListenerHome : Listener {
    internal var sql = Main.home

    @EventHandler(priority = EventPriority.HIGH)
    fun home(event: PlayerJoinEvent) {
        val p = event.player
        val list = LinkedList<LinkedHashMap<String, Any>>()
        val invite = LinkedList<LinkedHashMap<String, Any>>()

        try {
            sql.execute("create table if not exists '" + p.uniqueId.toString()
                    + "'(ID integer primary key, Name text unique, Location text unique);")

            val rs = sql.executeQuery("select * from '" + p.uniqueId.toString() + "';")
            if (!SPMSData.HomeData.containsKey(p.uniqueId.toString())) {
                while (rs.next()) {
                    val map = LinkedHashMap<String, Any>()
                    map.put("ID", rs.getInt("ID"))
                    map.put("Name", rs.getString("Name"))
                    map.put("Location", Utils.toLocation(rs.getString("Location"))!!)
                    list.add(map)
                }
                SPMSData.HomeData.put(p.uniqueId.toString(), list)
            }
        } catch (e: SQLException) {
            SPMSData.HomeData.put(p.uniqueId.toString(), list)
        }

        val mv = FixedMetadataValue(Main.main, invite)
        p.setMetadata("Invites", mv)
    }
}
