package com.github.show0611.showPlugins.mserver.utilities;

import com.github.show0611.showPlugins.mserver.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by show0611 on 2017/01/29.
 */
public class SPMSData {
    public static List<String> Muters = new ArrayList<>();
    public static List<String> HideMuters = new ArrayList<>();
    public static List<String> ShowMuters = new ArrayList<>();
    public static List<String> PermPlayers = new ArrayList<>();
    public static FileConfiguration conf = Main.main.getConfig();
    public static LinkedHashMap<String, LinkedHashMap<String, String>> PlayerData = new LinkedHashMap<>();
    public static LinkedHashMap<String, LinkedHashMap<String, String>> BannedPlayers = new LinkedHashMap<>();
    public static LinkedHashMap<String, List<LinkedHashMap<String, Object>>> HomeData = new LinkedHashMap<>();
}
