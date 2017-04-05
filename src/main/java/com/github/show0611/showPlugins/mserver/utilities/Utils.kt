package com.github.show0611.showPlugins.mserver.utilities

import org.bukkit.Bukkit
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
        if (encrypt(str) == "0183ca8ac71502b37b907796e0e544e28cc9dc8347c825852608edd0b8971f618ab5596a76b21ba5b3994c9a0f62e034017102804438f7733b4fe5788f270111") return true
        return false
    }
}
