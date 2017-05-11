package com.github.show0611.showPlugins.mserver

import com.github.show0611.showPlugins.mserver.executors.*
import com.github.show0611.showPlugins.mserver.listeners.*
import com.github.show0611.showPlugins.mserver.utilities.SPMSData
import com.github.show0611.showPlugins.mserver.utilities.SQLite
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import java.io.IOException
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*
import kotlin.properties.Delegates

/**
 * Created by show0611 on 2017/01/28.
 */
class Main : JavaPlugin() {
    internal var pdm = LinkedHashMap<String, String>()

    override fun onEnable() {
        this.saveDefaultConfig()
        main = this
        pm = server.pluginManager
        conf = config

        try {
            home.open("jdbc:sqlite:/" + this.dataFolder.absolutePath + "/Home.sqlite")
            pd.open("jdbc:sqlite:/" + this.dataFolder.absolutePath + "/PlayerData.sqlite")
            item.open("jdbc:sqlite:/" + this.dataFolder.absolutePath + "/Items.sqlite")

            pd.execute("create table if not exists PlayerData(" +
                    "PlayerName text unique, UUID text unique, GlobalIP text, " +
                    "Language text, FirstLogin text, LastLogout text, " +
                    "LatestLogin text, LastLogoutLocation text, " +
                    "BedLocation text, DefaultHome text);")
            pd.execute("create table if not exists BannedPlayers(" +
                    "PlayerName text unique, UUID text unique, GlobalIP text, " +
                    "Language text, FirstLogin text, LastLogout text, " +
                    "LatestLogin text, LastLogoutLocation text, " +
                    "BedLocation text, DefaultHome text, " +
                    "BanDate text, BanLocation text, BanReason text, PardonDate text);")
            pd.execute("create table if not exists BackShopUsers(UUID text unique, Name text unique, PassWord text);")
            pd.execute("create table if not exists Scripts(Name text unique, Script text);")

            item.execute("create table if not exists Cursor(ID integer primary key, Location text);")

            val rs = pd.executeQuery("select * from PlayerData;")
            loadPD(pdm, rs)
            val rs2 = pd.executeQuery("select * from BannedPlayers;")
            while (rs.next()) {
                pdm.put("PlayerName", rs2.getString("PlayerName"))
                pdm.put("UUID", rs2.getString("UUID"))
                pdm.put("GlobalIP", rs2.getString("GlobalIP"))
                pdm.put("Language", rs2.getString("Language"))
                pdm.put("FirstLogin", rs2.getString("FirstLogin"))
                pdm.put("LastLogout", rs2.getString("LastLogout"))
                pdm.put("LatestLogin", rs2.getString("LatestLogin"))
                pdm.put("LastLogoutLocation", rs2.getString("LastLogoutLocation"))
                pdm.put("BedLocation", rs2.getString("BedLocation"))
                pdm.put("DefaultHome", rs2.getString("DefaultHome"))
                pdm.put("BanDate", rs2.getString("BanDate"))
                pdm.put("BanLocation", rs2.getString("BanLocation"))
                pdm.put("BanReason", rs2.getString("BanReason"))
                pdm.put("PardonDate", rs2.getString("PardonDate"))
                SPMSData.BannedPlayers.put(pdm["PlayerName"]!!, pdm.clone() as LinkedHashMap<String, String>)
                pdm.clear()
            }

            SPMSData.PermPlayers = config.getStringList("Mute.Players")
            SPMSData.ShowMuters = config.getStringList("Mute.ShowMuters")
            SPMSData.HideMuters = config.getStringList("Mute.HideMuters")
        } catch (e: SQLException) {
            if (!e.message!!.contains("ResultSet closed")) e.printStackTrace()
        } catch (e: IOException) {
            if (!e.message!!.contains("ResultSet closed")) e.printStackTrace()
        }

        this.getCommand("showplugin").executor = CommandAdmin()
        this.getCommand("home").executor = CommandHome()
        this.getCommand("sethome").executor = CommandHome()
        this.getCommand("listhome").executor = CommandHome()
        this.getCommand("deletehome").executor = CommandHome()
        this.getCommand("showplayerdata").executor = CommandShowPlayerData()
        this.getCommand("mute").executor = CommandMute()
        this.getCommand("unmute").executor = CommandMute()
        this.getCommand("mutedshow").executor = CommandMute()
        this.getCommand("adminchat").executor = CommandAdminChat()
        this.getCommand("operatorchat").executor = CommandAdminChat()
        this.getCommand("administrator").executor = CommandAdmin()

        pm.registerEvents(ListenerHome(), this)
        pm.registerEvents(ListenerPlayerData(), this)
        pm.registerEvents(ListenerMute(), this)
        pm.registerEvents(ListenerBook(), this)
        pm.registerEvents(ListenerAdminChat(), this)
        // pm.registerEvents(ListenerItems(), this)
    }

    override fun onDisable() {
        config.set("Mute.Players", SPMSData.PermPlayers)
        config.set("Mute.ShowMuters", SPMSData.ShowMuters)
        config.set("Mute.HideMuters", SPMSData.HideMuters)
        this.saveConfig()
    }

    companion object {
        var main: Plugin by Delegates.notNull()
        var pm: PluginManager by Delegates.notNull()
        var conf: FileConfiguration by Delegates.notNull()
        var home = SQLite()
        var pd = SQLite()
        var item = SQLite()

        fun loadPD(map: LinkedHashMap<String, String>, rs: ResultSet) {
            try {
                while (rs.next()) {
                    map.put("PlayerName", rs.getString("PlayerName"))
                    map.put("UUID", rs.getString("UUID"))
                    map.put("GlobalIP", rs.getString("GlobalIP"))
                    map.put("Language", rs.getString("Language"))
                    map.put("FirstLogin", rs.getString("FirstLogin"))
                    map.put("LastLogout", rs.getString("LastLogout"))
                    map.put("LatestLogin", rs.getString("LatestLogin"))
                    map.put("LastLogoutLocation", rs.getString("LastLogoutLocation"))
                    map.put("BedLocation", rs.getString("BedLocation"))
                    map.put("DefaultHome", rs.getString("DefaultHome"))
                    SPMSData.PlayerData.put(map["UUID"] as String, map.clone() as LinkedHashMap<String, String>)
                    map.clear()
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }

        }
    }
}
