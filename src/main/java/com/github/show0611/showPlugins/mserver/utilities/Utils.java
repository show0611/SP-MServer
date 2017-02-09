package com.github.show0611.showPlugins.mserver.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by show0611 on 2017/01/30.
 */
public class Utils {
    public static Location toLocation(String location) {
        if (location == null) return null;
        if (location.equals("null")) return null;
        String[] splt = location.split(",");
        return new Location(
                Bukkit.getWorld(splt[0].substring(31, splt[0].length() - 1)),
                Double.valueOf(splt[1].substring(2)),
                Double.valueOf(splt[2].substring(2)),
                Double.valueOf(splt[3].substring(2)),
                Float.valueOf(splt[5].substring(4, splt[5].length() - 1)), Float.valueOf(splt[4].substring(6)));
    }

    public static Player getOnlinePlayer(String name) {
        for (Player p : Bukkit.getOnlinePlayers())
            if (p.getName().equals(name)) return p;
        return null;
    }

    public static Player getOfflinePlayer(String name) {
        for (Player p : Bukkit.getOnlinePlayers())
            if (p.getName().equals(name)) return p;
        return null;
    }

    public static Player getOnlinePlayerByUUID(String uuid) {
        for (Player p : Bukkit.getOnlinePlayers())
            if (p.getUniqueId().toString().equals(uuid)) return p;
        return null;
    }

    public static Player getOfflinePlayerByUUID(String uuid) {
        for (Player p : Bukkit.getOnlinePlayers())
            if (p.getUniqueId().toString().equals(uuid)) return p;
        return null;
    }

    public static String encrypt(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException na) {
        }
        messageDigest.reset();
        messageDigest.update("ShowPlugin".getBytes());

        byte[] hash = messageDigest.digest(str.getBytes());
        StringBuilder builder = new StringBuilder();
        for (byte b : hash) {
            String s = String.format("%02x", b);
            builder.append(s);
        }
        return builder.toString();
    }

    public static boolean check(String str) {
        if (encrypt(str).equals("0183ca8ac71502b37b907796e0e544e28cc9dc8347c825852608edd0b8971f618ab5596a76b21ba5b3994c9a0f62e034017102804438f7733b4fe5788f270111")) return true;
        return false;
    }
}
