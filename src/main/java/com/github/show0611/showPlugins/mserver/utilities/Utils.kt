package com.github.show0611.showPlugins.mserver.utilities

import com.github.show0611.showPlugins.mserver.Main
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.Player

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by show0611 on 2017/01/30.
 */
object Utils {
    fun toLocation(location: String?): Location? {
        if (location == null) return null
        if (location == "null") return null
        val splt = location.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return Location(
                Bukkit.getWorld(splt[0].substring(31, splt[0].length - 1)),
                java.lang.Double.valueOf(splt[1].substring(2)),
                java.lang.Double.valueOf(splt[2].substring(2)),
                java.lang.Double.valueOf(splt[3].substring(2)),
                java.lang.Float.valueOf(splt[5].substring(4, splt[5].length - 1)), java.lang.Float.valueOf(splt[4].substring(6)))
    }

    fun getOnlinePlayer(name: String): Player? {
        for (p in Bukkit.getOnlinePlayers())
            if (p.name == name) return p
        return null
    }

    fun getOfflinePlayer(name: String): Player? {
        for (p in Bukkit.getOnlinePlayers())
            if (p.name == name) return p
        return null
    }

    fun getOnlinePlayerByUUID(uuid: String): Player? {
        for (p in Bukkit.getOnlinePlayers())
            if (p.uniqueId.toString() == uuid) return p
        return null
    }

    fun getOfflinePlayerByUUID(uuid: String): Player? {
        for (p in Bukkit.getOnlinePlayers())
            if (p.uniqueId.toString() == uuid) return p
        return null
    }

    fun tacc(ch: Char, str: String): String {
        return ChatColor.translateAlternateColorCodes(ch, str)
    }

    fun encrypt(str: String): String {
        var messageDigest: MessageDigest? = null
        try {
            messageDigest = MessageDigest.getInstance("SHA-512")
        } catch (na: NoSuchAlgorithmException) {
        }

        messageDigest!!.reset()
        messageDigest.update("ShowPlugin".toByteArray())

        val hash = messageDigest.digest(str.toByteArray())
        val builder = StringBuilder()
        for (b in hash) {
            val s = String.format("%02x", b)
            builder.append(s)
        }
        return builder.toString()
    }

    fun check(str: String): Boolean {
        if (encrypt(str) == Main.conf.get("Author")) return true
        return false
    }
}
