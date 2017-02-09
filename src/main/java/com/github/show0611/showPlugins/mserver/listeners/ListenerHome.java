package com.github.show0611.showPlugins.mserver.listeners;

import com.github.show0611.showPlugins.mserver.Main;
import com.github.show0611.showPlugins.mserver.utilities.SPMSData;
import com.github.show0611.showPlugins.mserver.utilities.SQLite;
import com.github.show0611.showPlugins.mserver.utilities.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by show0611 on 2017/01/29.
 */
public class ListenerHome implements Listener {
    SQLite sql = Main.home;

    @EventHandler(priority = EventPriority.HIGH)
    public void home(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        List<LinkedHashMap<String, Object>> list = new LinkedList<>();
        List<LinkedHashMap<String, Object>> invite = new LinkedList<>();

        try {
            sql.execute("create table if not exists '" + p.getUniqueId().toString()
                    + "'(ID integer primary key, Name text unique, Location text unique);");

            ResultSet rs = sql.executeQuery("select * from '" + p.getUniqueId().toString() + "';");
            if (!SPMSData.HomeData.containsKey(p.getUniqueId().toString())) {
                while (rs.next()) {
                    LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                    map.put("ID", rs.getInt("ID"));
                    map.put("Name", rs.getString("Name"));
                    map.put("Location", Utils.toLocation(rs.getString("Location")));
                    list.add(map);
                }
                SPMSData.HomeData.put(p.getUniqueId().toString(), list);
            }
        } catch (SQLException e) {
            SPMSData.HomeData.put(p.getUniqueId().toString(), list);
        }

        MetadataValue mv = new FixedMetadataValue(Main.main, invite);
        p.setMetadata("Invites", mv);
    }
}
