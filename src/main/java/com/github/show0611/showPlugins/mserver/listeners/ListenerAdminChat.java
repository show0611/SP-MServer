package com.github.show0611.showPlugins.mserver.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by show0611 on 2017/02/03.
 */
public class ListenerAdminChat implements Listener {
    @EventHandler
    public void adminChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        String msg = event.getMessage();

        if (msg.startsWith("/operatorchat ")) {
            if (!p.hasPermission("sp.mserver.operatorchat")) return;
            msg = msg.substring(14);
            Bukkit.broadcast("§2[§3OpChat§2] §r" + p.getName() + ": " + msg, "sp.mserver.operatorchat");

        } else if (msg.startsWith("/adminchat ")) {
            if (!p.hasPermission("sp.mserver.adminchat")) return;
            msg = msg.substring(11);
            Bukkit.broadcast("§2[§3AdminChat§2] §r" + p.getName() + ": " + msg, "sp.mserver.adminchat");
        }
    }
}
