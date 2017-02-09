package com.github.show0611.showPlugins.mserver.listeners;

import com.github.show0611.showPlugins.mserver.Main;
import com.github.show0611.showPlugins.mserver.utilities.SPMSData;
import com.github.show0611.showPlugins.mserver.utilities.SQLite;
import com.github.show0611.showPlugins.mserver.utilities.Utils;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by show0611 on 2017/01/30.
 */
public class ListenerPlayerData implements Listener {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss");
    SQLite sql = Main.pd;
    LinkedHashMap<String, String> data = new LinkedHashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerData(PlayerJoinEvent event) {
        CraftPlayer cp = (CraftPlayer) event.getPlayer();
        try {
            sql.execute("insert into PlayerData values('" + cp.getName() + "', '" + cp.getUniqueId().toString() +
                    "', '" + cp.getAddress().getAddress().toString().substring(1) + "', '" + cp.getHandle().locale +
                    "', '" + sdf.format(new Date(cp.getFirstPlayed())) + "', '" + sdf.format(new Date(cp.getLastPlayed())) +
                    "', '" + sdf.format(new Date()) + "', 'null', 'null', 'null', 'null');");
            data.put("PlayerName", cp.getName());
            data.put("UUID", cp.getUniqueId().toString());
            data.put("GlobalIP", cp.getAddress().getAddress().toString().substring(1));
            data.put("Language", cp.getHandle().locale);
            data.put("FirstLogin", sdf.format(new Date(cp.getFirstPlayed())));
            data.put("LastLogout", sdf.format(new Date(cp.getLastPlayed())));
            data.put("LatestLogin", sdf.format(new Date()));
            data.put("LastLogoutLocation", "null");
            data.put("BedLocation", "null");
            data.put("DefaultHome", "null");
            SPMSData.PlayerData.put(cp.getUniqueId().toString(), (LinkedHashMap<String, String>) data.clone());
            data.clear();
        } catch (SQLException e) {
            try {
                ResultSet rs = sql.executeQuery("select * from PlayerData where UUID='" + cp.getUniqueId().toString() + "';");
                Main.loadPD(new LinkedHashMap<String, String>(), rs);
                LinkedHashMap<String, String> map = SPMSData.PlayerData.get(cp.getUniqueId().toString());

                map.replace("PlayerName", cp.getName());
                map.replace("GlobalIP", cp.getAddress().getAddress().toString());
                map.replace("Language", cp.getHandle().locale);
                map.replace("LastLogout", sdf.format(new Date(cp.getLastPlayed())));
                map.replace("LatestLogin", sdf.format(new Date()));
                SPMSData.PlayerData.replace(cp.getUniqueId().toString(), (LinkedHashMap<String, String>) map.clone());
                map.clear();

                sql.execute("update PlayerData set " +
                        "PlayerName='" + map.get("PlayerName") + "', " +
                        "GlobalIP='" + map.get("GlobalIP") + "', " +
                        "Language='" + map.get("Language") + "', " +
                        "LastLogout='" + map.get("LastLogout") + "', " +
                        "LatestLogin='" + map.get("LatestLogin") + "' " +
                        "where UUID='" + cp.getUniqueId().toString() + "';");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    @EventHandler
    public void playerLogout(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        LinkedHashMap<String, String> map = SPMSData.PlayerData.get(p.getUniqueId().toString());

        map.replace("LatestLogout", sdf.format(new Date()));
        map.replace("LatestLogoutLocation", p.getLocation().toString());
        try {
            sql.execute("update PlayerData set LatestLogoutLocation='" +
                    map.get("LatestLogoutLocation") + "', LatestLogout='" +
                    map.get("LatestLogout") + "' where UUID='" + p.getUniqueId().toString() + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SPMSData.PlayerData.replace(p.getUniqueId().toString(), (LinkedHashMap<String, String>) map.clone());
    }

    @EventHandler
    public void banned(PlayerKickEvent event) {
        if (!event.getPlayer().isBanned()) return;

        Player p = event.getPlayer();
        CraftPlayer cp = (CraftPlayer) p;
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("PlayerName", p.getName());
        map.put("UUID", p.getUniqueId().toString());
        map.put("GlobalIP", p.getAddress().getAddress().toString());
        map.put("Language", cp.getHandle().locale);
        map.put("FirstLogin", sdf.format(new Date(cp.getFirstPlayed())));
        map.put("LastLogout", sdf.format(new Date(cp.getLastPlayed())));
        map.put("LatestLogin", sdf.format(new Date()));
        map.put("LastLogoutLocation", "null");
        map.put("BedLocation", (p.getBedSpawnLocation().toString() != null) ? p.getBedSpawnLocation().toString() : "null");
        map.put("DefaultHome", "null");
        map.put("BanDate", sdf.format(new Date()));
        map.put("BanLocation", p.getLocation().toString());
        map.put("BanReason", event.getReason());
        map.put("PardonDate", "null");

        try {
            sql.execute("insert into BannedPlayers values('" + map.get("PlayerName") + "', '" + map.get("UUID") +
                    "', '" + map.get("GlobalIP") + "', '" + map.get("Language") + "', '" + map.get("FirstLogin") +
                    "', '" + map.get("LastLogout") + "', '" + map.get("LatestLogin") +
                    "', '" + map.get("LastLogoutLocation") + "', '" + map.get("BedLocation") + "', '" + map.get("DefaultHome") +
                    "', '" + map.get("BanDate") + "', '" + map.get("BanLocation") + "', '" + map.get("BanReason") +
                    "', '" + map.get("PardonDate") + "');");
        } catch (SQLException e) {
        }
        SPMSData.BannedPlayers.put(p.getUniqueId().toString(), map);

        try {
            sql.execute("delete from PlayerData where UUID='" + p.getUniqueId().toString() + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void bedin(PlayerBedEnterEvent event) {
        Player p = event.getPlayer();
        SPMSData.PlayerData.get(p.getUniqueId().toString()).replace("BedLocation", p.getBedSpawnLocation().toString());
        try {
            sql.execute("update PlayerData set BedLocation='" +
                    p.getBedSpawnLocation().toString() + "' where UUID='" + p.getUniqueId().toString() + "';");
        } catch (SQLException e) {
        }
    }

    @EventHandler
    public void login(PlayerLoginEvent event) {
        Player p = event.getPlayer();
        if (p.isBanned()) return;

        try {
            ResultSet rs = sql.executeQuery("select PlayerName from BannedPlayers where UUID='" + p.getUniqueId().toString() + "';");
            rs.next();

            if (!rs.next()) sql.execute("delete from BannedPlayers where UUID='" + p.getUniqueId().toString() + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
