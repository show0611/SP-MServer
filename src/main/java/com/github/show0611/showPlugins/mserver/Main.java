package com.github.show0611.showPlugins.mserver;

import com.github.show0611.showPlugins.mserver.executors.CommandAdminChat;
import com.github.show0611.showPlugins.mserver.executors.CommandHome;
import com.github.show0611.showPlugins.mserver.executors.CommandMute;
import com.github.show0611.showPlugins.mserver.executors.CommandShowPlayerData;
import com.github.show0611.showPlugins.mserver.listeners.*;
import com.github.show0611.showPlugins.mserver.utilities.SPMSData;
import com.github.show0611.showPlugins.mserver.utilities.SQLite;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by show0611 on 2017/01/28.
 */
public class Main extends JavaPlugin {
    public static Plugin main;
    public static Server server;
    public static PluginManager pm;
    public static SQLite home = new SQLite();
    public static SQLite pd = new SQLite();
    LinkedHashMap<String, String> pdm = new LinkedHashMap<>();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        main = this;
        server = this.getServer();
        pm = server.getPluginManager();
        int id = 0;
        int count = 0;
        List<String> array = new ArrayList<>();

        try {
            home.open("jdbc:sqlite:\\" + this.getDataFolder().getAbsolutePath() + "\\Home.sqlite");
            pd.open("jdbc:sqlite:\\" + this.getDataFolder().getAbsolutePath() + "\\PlayerData.sqlite");

            pd.execute("create table if not exists PlayerData(" +
                    "PlayerName text unique, UUID text unique, GlobalIP text, " +
                    "Language text, FirstLogin text, LastLogout text, " +
                    "LatestLogin text, LastLogoutLocation text, " +
                    "BedLocation text, DefaultHome text);");
            pd.execute("create table if not exists BannedPlayers(" +
                    "PlayerName text unique, UUID text unique, GlobalIP text, " +
                    "Language text, FirstLogin text, LastLogout text, " +
                    "LatestLogin text, LastLogoutLocation text, " +
                    "BedLocation text, DefaultHome text, " +
                    "BanDate text, BanLocation text, BanReason text, PardonDate text);");

            ResultSet rs = pd.executeQuery("select * from PlayerData;");
            loadPD(pdm, rs);
            ResultSet rs2 = pd.executeQuery("select * from BannedPlayers;");
            while (rs.next()) {
                pdm.put("PlayerName", rs2.getString("PlayerName"));
                pdm.put("UUID", rs2.getString("UUID"));
                pdm.put("GlobalIP", rs2.getString("GlobalIP"));
                pdm.put("Language", rs2.getString("Language"));
                pdm.put("FirstLogin", rs2.getString("FirstLogin"));
                pdm.put("LastLogout", rs2.getString("LastLogout"));
                pdm.put("LatestLogin", rs2.getString("LatestLogin"));
                pdm.put("LastLogoutLocation", rs2.getString("LatestLogoutLocation"));
                pdm.put("BedLocation", rs2.getString("BedLocation"));
                pdm.put("DefaultHome", rs2.getString("DefaultHome"));
                pdm.put("BanDate", rs2.getString("BanDate"));
                pdm.put("BanLocation", rs2.getString("BanLocation"));
                pdm.put("BanReason", rs2.getString("BanReason"));
                pdm.put("PardonDate", rs2.getString("PardonDate"));
                SPMSData.BannedPlayers.put(pdm.get("PlayerName"), (LinkedHashMap<String, String>) pdm.clone());
                pdm.clear();
            }

            SPMSData.PermPlayers = getConfig().getStringList("Mute.Players");
            SPMSData.ShowMuters = getConfig().getStringList("Mute.ShowMuters");
            SPMSData.HideMuters = getConfig().getStringList("Mute.HideMuters");
        } catch (SQLException | IOException e) {
            if (!e.getMessage().contains("ResultSet closed")) e.printStackTrace();
        }

        this.getCommand("showplugin").setExecutor(new CommandHome());
        this.getCommand("home").setExecutor(new CommandHome());
        this.getCommand("sethome").setExecutor(new CommandHome());
        this.getCommand("listhome").setExecutor(new CommandHome());
        this.getCommand("deletehome").setExecutor(new CommandHome());
        this.getCommand("showplayerdata").setExecutor(new CommandShowPlayerData());
        this.getCommand("mute").setExecutor(new CommandMute());
        this.getCommand("unmute").setExecutor(new CommandMute());
        this.getCommand("mutedshow").setExecutor(new CommandMute());
        this.getCommand("adminchat").setExecutor(new CommandAdminChat());
        this.getCommand("operatorchat").setExecutor(new CommandAdminChat());

        pm.registerEvents(new ListenerHome(), this);
        pm.registerEvents(new ListenerPlayerData(), this);
        pm.registerEvents(new ListenerMute(), this);
        pm.registerEvents(new ListenerBook(), this);
        pm.registerEvents(new ListenerAdminChat(), this);
    }

    @Override
    public void onDisable() {
        getConfig().set("Mute.Players", SPMSData.PermPlayers);
        getConfig().set("Mute.ShowMuters", SPMSData.ShowMuters);
        getConfig().set("Mute.HideMuters", SPMSData.HideMuters);
        this.saveConfig();
    }

    public static void loadPD(LinkedHashMap map, ResultSet rs) {
        try {
            while (rs.next()) {
                map.put("PlayerName", rs.getString("PlayerName"));
                map.put("UUID", rs.getString("UUID"));
                map.put("GlobalIP", rs.getString("GlobalIP"));
                map.put("Language", rs.getString("Language"));
                map.put("FirstLogin", rs.getString("FirstLogin"));
                map.put("LastLogout", rs.getString("LastLogout"));
                map.put("LatestLogin", rs.getString("LatestLogin"));
                map.put("LatestLogout", rs.getString("LatestLogout"));
                map.put("LatestLogoutLocation", rs.getString("LatestLogoutLocation"));
                map.put("BedLocation", rs.getString("BedLocation"));
                map.put("DefaultHome", rs.getString("DefaultHome"));
                SPMSData.PlayerData.put((String) map.get("UUID"), (LinkedHashMap<String, String>) map.clone());
                map.clear();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
