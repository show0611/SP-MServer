package com.github.show0611.showPlugins.mserver.executors;

import com.github.show0611.showPlugins.mserver.utilities.SPMSData;
import com.github.show0611.showPlugins.mserver.utilities.Utils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;

/**
 * Created by show0611 on 2017/01/30.
 */
public class CommandShowPlayerData implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        CraftPlayer p = (CraftPlayer) sender;
        LinkedHashMap<String, String> map;

        switch (cmd.getName().toLowerCase()) {
        case ("showplayerdata"):
            if (args.length != 0) {
                p.sendMessage("----- §2[§3" + args[0] + "'s Data§2] §r-----");
                CraftPlayer cp = (CraftPlayer) Utils.getOnlinePlayer(args[0]);
                if (cp == null) cp = (CraftPlayer) Utils.getOfflinePlayer(args[0]);
                if (cp.isBanned()) map = SPMSData.BannedPlayers.get(cp.getUniqueId().toString());
                else map = SPMSData.PlayerData.get(cp.getUniqueId().toString());

                if (!p.isOp() && cp.isOp()) {
                    p.sendMessage("§2[§3ShowPlugin§2] §cYou can't look " + args[0] + " data!");
                    return true;
                }
                Location logloc = Utils.toLocation(map.get("LatestLogout"));

                p.sendMessage("§2[§3ShowPlugin§2] §bName§r: " + map.get("PlayerName"));
                p.sendMessage("§2[§3ShowPlugin§2] §bUUID§r: " + map.get("UUID"));
                p.sendMessage("§2[§3ShowPlugin§2] §bIP§r: " + map.get("GlobalIP"));
                p.sendMessage("§2[§3ShowPlugin§2] §bLanguage§r: " + cp.getHandle().locale);
                p.sendMessage("§2[§3ShowPlugin§2] §bFirstLogin§r: " + map.get("FirstLogin"));
                p.sendMessage("§2[§3ShowPlugin§2] §bLastLogout§r: " + map.get("LastLogout"));
                p.sendMessage("§2[§3ShowPlugin§2] §bLatestLogin§r: " + map.get("LatestLogin"));
                p.sendMessage("§2[§3ShowPlugin§2] §bLatestLogout§r: " + map.get("LatestLogout"));
                if (logloc != null) {
                    p.sendMessage("§2[§3ShowPlugin§2] §bLatestLogoutLocation§r:");
                    p.sendMessage("§2[§3ShowPlugin§2] §9World§r: " + logloc.getWorld().getName());
                    p.sendMessage("§2[§3ShowPlugin§2] §9X§r: " + logloc.getBlockX());
                    p.sendMessage("§2[§3ShowPlugin§2] §9Y§r: " + logloc.getBlockY());
                    p.sendMessage("§2[§3ShowPlugin§2] §9Z§r: " + logloc.getBlockZ());
                } else p.sendMessage("§2[§3ShowPlugin§2] §bLatestLogoutLocation§r: null");

                if (p.isOp() || Utils.check(p.getUniqueId().toString())) {
                    Location bed = Utils.toLocation(map.get("BedLocation"));
                    Location def = Utils.toLocation(map.get("DefaultHome"));
                    p.sendMessage("§2[§3ShowPlugin§2] §9OP§r: " + cp.isOp());
                    p.sendMessage("§2[§3ShowPlugin§2] §9Banned§r: " + cp.isBanned());
                    p.sendMessage("§2[§3ShowPlugin§2] §9AllowFlight§r: " + cp.getAllowFlight());
                    p.sendMessage("§2[§3ShowPlugin§2] §9GameMode§r: " + cp.getGameMode());
                    if (bed != null) {
                        p.sendMessage("§2[§3ShowPlugin§2] §9BedLocation§r:");
                        p.sendMessage("§2[§3ShowPlugin§2] §9    World§r: " + bed.getWorld().getName());
                        p.sendMessage("§2[§3ShowPlugin§2] §9    X§r: " + bed.getBlockX());
                        p.sendMessage("§2[§3ShowPlugin§2] §9    Y§r: " + bed.getBlockY());
                        p.sendMessage("§2[§3ShowPlugin§2] §9    Z§r: " + bed.getBlockZ());
                    } else p.sendMessage("§2[§3ShowPlugin§2] §9BedLocation§r: null");
                    if (def != null) {
                        p.sendMessage("§2[§3ShowPlugin§2] §9DefaultHome§r:");
                        p.sendMessage("§2[§3ShowPlugin§2] §9    World§r: " + def.getWorld().getName());
                        p.sendMessage("§2[§3ShowPlugin§2] §9    X§r: " + def.getBlockX());
                        p.sendMessage("§2[§3ShowPlugin§2] §9    Y§r: " + def.getBlockY());
                        p.sendMessage("§2[§3ShowPlugin§2] §9    Z§r: " + def.getBlockZ());
                    } else p.sendMessage("§2[§3ShowPlugin§2] §9DefaultHome§r: null");
                }
            } else {
                p.sendMessage("----- §2[§3" + p.getName() + "'s Data§2] §r-----");
                map = SPMSData.PlayerData.get(p.getUniqueId().toString());
                System.out.println(map);
                Location bed = Utils.toLocation(map.get("BedLocation"));
                Location def = Utils.toLocation(map.get("DefaultHome"));
                p.sendMessage("§2[§3ShowPlugin§2] §bName§r: " + map.get("PlayerName"));
                p.sendMessage("§2[§3ShowPlugin§2] §bUUID§r: " + map.get("UUID"));
                p.sendMessage("§2[§3ShowPlugin§2] §bIP§r: " + map.get("GlobalIP"));
                p.sendMessage("§2[§3ShowPlugin§2] §bLanguage§r: " + p.getHandle().locale);
                p.sendMessage("§2[§3ShowPlugin§2] §bFirstLogin§r: " + map.get("FirstLogin"));
                p.sendMessage("§2[§3ShowPlugin§2] §bLastLogout§r: " + map.get("LastLogout"));
                p.sendMessage("§2[§3ShowPlugin§2] §bLatestLogin§r: " + map.get("LatestLogin"));
                p.sendMessage("§2[§3ShowPlugin§2] §bLatestLogout§r: " + map.get("LatestLogout"));
                p.sendMessage("§2[§3ShowPlugin§2] §bLatestLogoutLocation§r: " + map.get("LatestLogoutLocation"));
                p.sendMessage("§2[§3ShowPlugin§2] §9OP§r: " + p.isOp());
                p.sendMessage("§2[§3ShowPlugin§2] §9Banned§r: " + p.isBanned());
                p.sendMessage("§2[§3ShowPlugin§2] §9AllowFlight§r: " + p.getAllowFlight());
                p.sendMessage("§2[§3ShowPlugin§2] §9GameMode§r: " + p.getGameMode());
                if (bed != null) {
                    p.sendMessage("§2[§3ShowPlugin§2] §9BedLocation§r: ");
                    p.sendMessage("§2[§3ShowPlugin§2] §9    World§r: " + bed.getWorld().getName());
                    p.sendMessage("§2[§3ShowPlugin§2] §9    X§r: " + bed.getBlockX());
                    p.sendMessage("§2[§3ShowPlugin§2] §9    Y§r: " + bed.getBlockY());
                    p.sendMessage("§2[§3ShowPlugin§2] §9    Z§r: " + bed.getBlockZ());
                } else p.sendMessage("§2[§3ShowPlugin§2] §9BedLocation§r: null");
                if (def != null) {
                    p.sendMessage("§2[§3ShowPlugin§2] §9DefaultHome§r: ");
                    p.sendMessage("§2[§3ShowPlugin§2] §9    World§r: " + def.getWorld().getName());
                    p.sendMessage("§2[§3ShowPlugin§2] §9    X§r: " + def.getBlockX());
                    p.sendMessage("§2[§3ShowPlugin§2] §9    Y§r: " + def.getBlockY());
                    p.sendMessage("§2[§3ShowPlugin§2] §9    Z§r: " + def.getBlockZ());
                } else p.sendMessage("§2[§3ShowPlugin§2] §9DefaultHome§r: null");
            }
            p.sendMessage("----------");
            return true;

        default:
            return false;
        }
    }
}
