package com.github.show0611.showPlugins.mserver.listeners

import com.github.show0611.showPlugins.mserver.utilities.Utils
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.meta.BookMeta
import javax.script.ScriptEngineManager
import javax.script.ScriptException

/**
 * Created by show0611 on 2017/02/02.
 */
/*
 * 仮実装。
 * なんとなく、JavaScriptが本で実行出来たらいいなぁって思って実装(使い方によってはとんでもなく危険w
 * まぁ、普通の人にゃ使えないから大丈夫
 * 場合によっては削除する
 */
class ListenerBook : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun bookInteract(event: PlayerInteractEvent) {
        if (event.item != null) {
            val `is` = if (event.item.type == Material.WRITTEN_BOOK) event.item else null
            val bm = if (`is` != null && `is`.itemMeta is BookMeta) `is`.itemMeta as BookMeta else null
            if (bm != null && Utils.check(Utils.getOnlinePlayer(bm.author)!!.uniqueId.toString())) {
                if (bm.pages[0].contains("[JavaScript]")) {
                    var js = ""
                    val sem = ScriptEngineManager()
                    val se = sem.getEngineByName("js")

                    for (str in bm.pages) {
                        js += str.replace("§0".toRegex(), "").replace("\\[JavaScript\\]".toRegex(), "") + " "
                    }
                    try {
                        se.eval(js)
                    } catch (e: ScriptException) {
                        e.printStackTrace()
                    }

                }
            }
        }
    }
}
