package com.github.show0611.showPlugins.mserver.listeners;

import com.github.show0611.showPlugins.mserver.utilities.Utils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by show0611 on 2017/02/02.
 */
/*
 * 仮実装。
 * なんとなく、JavaScriptが本で実行出来たらいいなぁって思って実装(使い方によってはとんでもなく危険w
 * まぁ、普通の人にゃ使えないから大丈夫
 * 場合によっては削除する
 */
public class ListenerBook implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void bookInteract(PlayerInteractEvent event) {
        if (event.getItem() != null) {
            ItemStack is = (event.getItem().getType() == Material.WRITTEN_BOOK) ? (event.getItem()) : (null);
            BookMeta bm = (is != null && is.getItemMeta() instanceof BookMeta) ? (BookMeta) is.getItemMeta() : null;
            if (bm != null && Utils.check(Utils.getOnlinePlayer(bm.getAuthor()).getUniqueId().toString())) {
                if (bm.getPages().get(0).contains("[JavaScript]")) {
                    String js = "";
                    ScriptEngineManager sem = new ScriptEngineManager();
                    ScriptEngine se = sem.getEngineByName("js");

                    for (String str : bm.getPages()) {
                        js += str.replaceAll("§0", "").replaceAll("\\[JavaScript\\]", "") + " ";
                    }
                    try {
                        se.eval(js);
                    } catch (ScriptException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
