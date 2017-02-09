package com.github.show0611.showPlugins.mserver.listeners;

import com.github.show0611.showPlugins.mserver.utilities.SPMSData;
import com.github.show0611.showPlugins.mserver.utilities.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Created by show0611 on 2017/02/02.
 */
public class ListenerMute implements Listener {
    @EventHandler
    public void muteChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();

        for (String muter : SPMSData.ShowMuters) {
            if (!muter.equals(p.getName())) continue;

            for (String str : SPMSData.PermPlayers) {
                Player pp = Utils.getOnlinePlayer(str);
                if (pp == null) continue;

                pp.sendMessage("§2[§3MuterChat§2] §r" + p.getName() + ": " + event.getMessage());
            }
            event.setCancelled(true);
            break;
        }
    }

    @EventHandler
    public void muteCmd(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();

        for (String muter : SPMSData.Muters) {
            if (muter.equals(p.getName())) {
                event.setCancelled(true);
                break;
            }
        }
    }
}
