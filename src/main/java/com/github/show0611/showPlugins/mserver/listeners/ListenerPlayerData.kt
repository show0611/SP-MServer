package com.github.show0611.showPlugins.mserver.listeners

import com.github.show0611.showPlugins.mserver.Main
import com.github.show0611.showPlugins.mserver.utilities.SPMSData
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.*
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by show0611 on 2017/01/30.
 */
class ListenerPlayerData : Listener {
    internal var sdf = SimpleDateFormat("yyyy.MM.dd_HH:mm:ss")
    internal var sql = Main.pd
    internal var data = LinkedHashMap<String, String>()

    @EventHandler(priority = EventPriority.HIGHEST)
    fun playerData(event: PlayerJoinEvent) {
        val cp = event.player as CraftPlayer
        try {
            sql.execute("insert into PlayerData values('" + cp.name + "', '" + cp.uniqueId.toString() +
                    "', '" + cp.address.address.toString().substring(1) + "', '" + cp.handle.locale +
                    "', '" + sdf.format(Date(cp.firstPlayed)) + "', '" + sdf.format(Date(cp.lastPlayed)) +
                    "', '" + sdf.format(Date()) + "', 'null', 'null', 'null');")
            data.put("PlayerName", cp.name)
            data.put("UUID", cp.uniqueId.toString())
            data.put("GlobalIP", cp.address.address.toString().substring(1))
            data.put("Language", cp.handle.locale)
            data.put("FirstLogin", sdf.format(Date(cp.firstPlayed)))
            data.put("LastLogout", sdf.format(Date(cp.lastPlayed)))
            data.put("LatestLogin", sdf.format(Date()))
            data.put("LastLogoutLocation", "null")
            data.put("BedLocation", "null")
            data.put("DefaultHome", "null")
            SPMSData.PlayerData.put(cp.uniqueId.toString(), data.clone() as LinkedHashMap<String, String>)
            data.clear()
        } catch (e: SQLException) {
            if (!e.message!!.contains("A UNIQUE constraint failed (UNIQUE constraint failed: PlayerData.UUID)")) e.printStackTrace()
            try {
                val rs = sql.executeQuery("select * from PlayerData where UUID='" + cp.uniqueId.toString() + "';")
                Main.loadPD(LinkedHashMap<String, String>(), rs)
                val map = SPMSData.PlayerData[cp.uniqueId.toString()]!!

                map.replace("PlayerName", cp.name)
                map.replace("GlobalIP", cp.address.address.toString())
                map.replace("Language", cp.handle.locale)
                map.replace("LastLogout", sdf.format(Date(cp.lastPlayed)))
                map.replace("LatestLogin", sdf.format(Date()))
                SPMSData.PlayerData.replace(cp.uniqueId.toString(), map.clone() as LinkedHashMap<String, String>)
                map.clear()

                sql.execute("update PlayerData set " +
                        "PlayerName='" + map["PlayerName"] + "', " +
                        "GlobalIP='" + map["GlobalIP"] + "', " +
                        "Language='" + map["Language"] + "', " +
                        "LastLogout='" + map["LastLogout"] + "', " +
                        "LatestLogin='" + map["LatestLogin"] + "' " +
                        "where UUID='" + cp.uniqueId.toString() + "';")
            } catch (e1: SQLException) {
                e1.printStackTrace()
            }
        }
    }

    @EventHandler
    fun playerLogout(event: PlayerQuitEvent) {
        val p = event.player
        val map = SPMSData.PlayerData[p.uniqueId.toString()]!!

        map.replace("LatestLogout", sdf.format(Date()))
        map.replace("LatestLogoutLocation", p.location.toString())
        try {
            sql.execute("update PlayerData set LastLogoutLocation='" +
                    map["LastLogoutLocation"] + "', LastLogout='" +
                    map["LastLogout"] + "' where UUID='" + p.uniqueId.toString() + "';")
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        SPMSData.PlayerData.replace(p.uniqueId.toString(), map.clone() as LinkedHashMap<String, String>)
    }

    @EventHandler
    fun banned(event: PlayerKickEvent) {
        if (!event.player.isBanned) {
            val p = event.player
            val cp = p as CraftPlayer
            val map = LinkedHashMap<String, String>()
            map.put("PlayerName", p.getName())
            map.put("UUID", p.getUniqueId().toString())
            map.put("GlobalIP", p.getAddress().address.toString())
            map.put("Language", cp.handle.locale)
            map.put("FirstLogin", sdf.format(Date(cp.firstPlayed)))
            map.put("LastLogout", sdf.format(Date(cp.lastPlayed)))
            map.put("LatestLogin", sdf.format(Date()))
            map.put("LastLogoutLocation", "null")
            map.put("BedLocation", if (p.getBedSpawnLocation().toString().isNullOrEmpty()) p.getBedSpawnLocation().toString() else "null")
            map.put("DefaultHome", "null")
            map.put("BanDate", sdf.format(Date()))
            map.put("BanLocation", p.getLocation().toString())
            map.put("BanReason", event.reason)
            map.put("PardonDate", "null")

            try {
                sql.execute("insert into BannedPlayers values('" + map["PlayerName"] + "', '" + map["UUID"] + "', '" + map["GlobalIP"] + "', '" + map["Language"] + "', '" + map["FirstLogin"] + "', '" + map["LastLogout"] + "', '" + map["LatestLogin"] + "', '" + map["LastLogoutLocation"] + "', '" + map["BedLocation"] + "', '" + map["DefaultHome"] + "', '" + map["BanDate"] + "', '" + map["BanLocation"] + "', '" + map["BanReason"] + "', '" + map["PardonDate"] + "');")
            } catch (e: SQLException) {
            }

            SPMSData.BannedPlayers.put(p.getUniqueId().toString(), map)

            try {
                sql.execute("delete from PlayerData where UUID='" + p.getUniqueId().toString() + "';")
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    @EventHandler
    fun bedin(event: PlayerBedEnterEvent) {
        val p = event.player
        SPMSData.PlayerData[p.uniqueId.toString()]!!.replace("BedLocation", p.bedSpawnLocation.toString())
        try {
            sql.execute("update PlayerData set BedLocation='" +
                    p.bedSpawnLocation.toString() + "' where UUID='" + p.uniqueId.toString() + "';")
        } catch (e: SQLException) {
        }
    }

    @EventHandler
    fun login(event: PlayerLoginEvent) {
        val p = event.player
        if (!p.isBanned) {
            try {
                val rs = sql.executeQuery("select PlayerName from BannedPlayers where UUID='" + p.uniqueId.toString() + "';")
                rs.next()

                if (!rs.next()) sql.execute("delete from BannedPlayers where UUID='" + p.uniqueId.toString() + "';")
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }
}
