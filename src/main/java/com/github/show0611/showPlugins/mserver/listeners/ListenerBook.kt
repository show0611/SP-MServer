package com.github.show0611.showPlugins.mserver.listeners

import com.github.show0611.showPlugins.mserver.Main
import com.github.show0611.showPlugins.mserver.utilities.SPMSData
import com.github.show0611.showPlugins.mserver.utilities.Utils
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import java.io.*
import java.sql.SQLException
import java.util.regex.Pattern
import javax.script.ScriptEngineManager
import javax.script.ScriptException
import kotlin.properties.Delegates

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
    val pat = Pattern.compile("(\\[.* .*])")

    @EventHandler(priority = EventPriority.HIGHEST)
    fun bookInteract(event: PlayerInteractEvent) {
        val p = event.player

        if (event.item != null) {
            val item = if (event.item.type == Material.WRITTEN_BOOK) event.item else null
            val bm = if (item != null && item.itemMeta is BookMeta) item.itemMeta as BookMeta else null

            if (bm != null && Utils.check(Utils.getOnlinePlayer(bm.author)!!.uniqueId.toString())) {
                if (bm.pages[0].contains("[JavaScript]")) {
                    var js = ""
                    val sem = ScriptEngineManager()
                    val se = sem.getEngineByName("js")

                    for (str in bm.pages) {
                        js += str.replace("§0".toRegex(), "").replace("\\[JavaScript]".toRegex(), "").replace("\\[Interactor]".toRegex(), "Utils.getOnlinePlayer(\"${p.name}\")") + "\n"
                    }
                    try {
                        val m = pat.matcher(js)
                        m.find()
                        val mr = m.toMatchResult()

                        for (i in 0..mr.groupCount() - 1) {
                            val kw = mr.group(i)
                            when {
                                kw.contains("[lib ") -> {
                                    js = js.replace(kw, inputStreamToString(Utils.javaClass.classLoader.getResourceAsStream("LibJS/${kw.substring(6, kw.length - 2)}.js")))
                                }

                                kw.contains("[load ") -> {
                                    try {
                                        val rs = Main.pd.executeQuery("select * from Scripts where Name='${kw.substring(7, kw.length - 2)}';")
                                        js = js.replace(kw, rs.getString("Script"))
                                    } catch (e: SQLException) {
                                        p.sendMessage("§2[§3ShowPlugin§2] §c${e.localizedMessage}")
                                    }
                                }

                                kw.contains("[save ") -> {
                                    try {
                                        Main.pd.execute("insert into Scripts values('${kw.substring(7, kw.length - 2)}', '${js.replace(kw, "").replace("\n", " ")}');")
                                        p.sendMessage("§2[§3ShowPlugin§2] §3Save success.")
                                        p.inventory.itemInMainHand = ItemStack(Material.AIR)
                                    } catch (e: SQLException) {
                                        p.sendMessage("§2[§3ShowPlugin§2] §c${e.localizedMessage}")
                                    }
                                }

                                kw.contains("[delete ") -> {
                                    try {
                                        Main.pd.execute("delete from Scripts where Name='${kw.substring(9, kw.length - 2)}';")
                                    } catch (e: SQLException) {
                                        p.sendMessage("§2[§3ShowPlugin§2] §c${e.localizedMessage}")
                                    }
                                }
                            }
                        }
                        se.eval(js)
                    } catch (e: ScriptException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun inputStreamToString(input: InputStream): String {
        var str: String? = null
        val sb = StringBuilder()
        val reader = input.bufferedReader()

        str = reader.readLine()
        while (str != null) {
            sb.append(str)
            str = reader.readLine()
        }
        return sb.toString()
    }
}
