@file:Suppress("UNREACHABLE_CODE")

package com.github.show0611.showPlugins.mserver.executors

import com.github.show0611.showPlugins.mserver.MServerException
import com.github.show0611.showPlugins.mserver.Main
import com.github.show0611.showPlugins.mserver.utilities.SPMSData
import com.github.show0611.showPlugins.mserver.utilities.Utils
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.sql.SQLException
import java.util.*
import kotlin.properties.Delegates

/**
 * Created by show0611 on 2017/01/28.
 */
class CommandHome : CommandExecutor {
    private val sql = Main.home
    private val pd = Main.pd
    private val ipl = ArrayList<String>()
    private val counter = HashMap<String, Int>()
    private val delay = SPMSData.conf.getInt("Delay.InviteDelay")

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        val p = sender as Player
        val scheduler = Main.main.server.scheduler
        val map = LinkedHashMap<String, Any>()
        val list = SPMSData.HomeData["${p.uniqueId}"]
        val invite: MutableList<LinkedHashMap<String, Any>>

        when (cmd.name.toLowerCase()) {
            "home" -> {
                if (args.isNotEmpty()) {
                    when (args[0]) {
                        "#invite" -> {
                            val pl = Utils.getOnlinePlayer(args[1])
                            var loc: Location by Delegates.notNull()
                            invite = p.getMetadata("Invites")[0].value() as MutableList<LinkedHashMap<String, Any>>
                            val id = invite.size + 1
                            if (pl == null) return false

                            for (str in ipl) {
                                if (str == p.uniqueId.toString()) {
                                    p.sendMessage("§2[§3ShowPlugin§2] 前回の招待から${delay}秒後に招待を送ってください。")
                                    return true
                                }
                            }
                            val limit = SPMSData.conf.getInt("Limit.InviteStack")
                            if (limit != -1 && invite.size >= limit) {
                                p.sendMessage("§2[§3ShowPlugin§2] §c${args[1]}さんへの招待が上限数に達しているのでしばらく待ってからもう一度招待してください")
                                return true
                            }

                            val pre = TextComponent("§2[§3ShowPlugin§2] §r")
                            val base = TextComponent("${p.name}さんからHomeへの招待が来ています")
                            val tc = TextComponent("§2[§3ShowPlugin§2] §3許可するならこのチャットをクリック")
                            base.color = ChatColor.GREEN
                            tc.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home #accept $id")
                            pre.addExtra(base)

                            p.spigot().sendMessage(pre)
                            p.spigot().sendMessage(tc)

                            for (m in SPMSData.HomeData["${p.uniqueId}"]!!) {
                                if (m["Name"] == args[1]) {
                                    loc = Utils.toLocation(m["Location"].toString())!!
                                    println(loc)
                                    break
                                }
                            }

                            counter.put(p.uniqueId.toString(), delay)
                            map.put("ID", id)
                            map.put("Name", p.name)
                            map.put("Location", loc)
                            invite.add(map.clone() as LinkedHashMap<String, Any>)
                            ipl.add(p.uniqueId.toString())
                            map.clear()

                            object : BukkitRunnable() {
                                override fun run() {
                                    for (m in invite) {
                                        if (m["ID"] == id) {
                                            pl.sendMessage("§2[§3ShowPlugin§2] §r${m["Name"]}さんからの招待が削除されました")
                                            invite.remove(m)
                                        }
                                    }
                                }
                            }.runTaskLater(Main.main, (SPMSData.conf.getInt("Delay.InviteRemoveDelay") * 20).toLong())
                            scheduler.scheduleSyncRepeatingTask(Main.main, object : BukkitRunnable() {
                                internal var count = counter[p.uniqueId.toString()]
                                override fun run() {
                                    if (count!! > 0)
                                        counter.replace(p.uniqueId.toString(), count!!.dec())
                                    else if (count == 0) this.cancel()
                                }
                            }, 0, 20)
                            return true
                        }

                        "#accept" -> {
                            if (args.size != 1) {
                                val iid = Integer.parseInt(args[1])
                                invite = p.getMetadata("Invites")[0].value() as MutableList<LinkedHashMap<String, Any>>
                                for (m in invite) {
                                    if (m["ID"] as Int == iid) {
                                        p.teleport(m["Location"] as Location)
                                        return true
                                    }
                                }
                            }
                            return false
                        }

                        else -> {
                            for (m in SPMSData.HomeData["${p.uniqueId}"]!!) {
                                if (m["Name"] == args[0]) {
                                    p.teleport(Utils.toLocation(m["Location"].toString()))
                                    return true
                                }
                            }
                            return false
                        }
                    }
                } else {
                    try {
                        p.teleport(Utils.toLocation(SPMSData.PlayerData[p.uniqueId.toString()]!!["DefaultHome"]))
                    } catch (e: Exception) {
                        p.sendMessage("§2[§3ShowPlugin§2] §cHomeが設定されていません。/sethomeコマンドを実行してから使用してください。")
                    }

                    return true
                }
                if (args.isNotEmpty()) {
                    when (args[0]) {
                        "#accept" -> {
                            try {
                                if (args[1].equals(p.uniqueId.toString())) {
                                    pd.execute("update PlayerData set DefaultHome='${p.location}' where UUID='${p.uniqueId}';")
                                    SPMSData.PlayerData[p.uniqueId.toString()]!!.replace("DefaultHome", p.location.toString())
                                    return true
                                } else {
                                    sql.execute("update '${p.uniqueId}' set Location='${p.location}' where Name='${args[1]}';")
                                    for (m in SPMSData.HomeData[p.uniqueId.toString()]!!) {
                                        for (entry in m.entries) {
                                            if (entry.key == args[1]) {
                                                m.replace(args[1], p.location.toString())
                                                return true
                                            }
                                        }
                                    }
                                }
                            } catch (e: SQLException) {
                                e.printStackTrace()
                            }

                            return false
                        }

                        else -> {
                            val limit = SPMSData.conf.getInt("Limit.HomeRegister")
                            if (limit != -1 && list!!.size >= limit) {
                                p.sendMessage("§2[§3ShowPlugin§2] §cHomeの設定数が上限に達しているので登録出来ませんでした")
                                return true
                            }
                            try {
                                sql.execute("insert into ${p.uniqueId}(Name, Location) values('${args[0]}', '${p.location}');")

                                map.put("ID", list!!.size + 1)
                                map.put("Name", args[0])
                                map.put("Location", p.location.toString())
                                list.add(map.clone() as LinkedHashMap<String, Any>)
                                map.clear()
                            } catch (e: SQLException) {
                                val base = TextComponent("§2[§3ShowPlugin§2] §3上書きする場合はこのチャットをクリック")

                                base.color = ChatColor.GREEN
                                base.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sethome #accept ${p.uniqueId}")
                                p.spigot().sendMessage(base)
                            }

                            return true
                        }
                    }
                } else {
                    try {
                        val rs = pd.executeQuery("select DefaultHome from PlayerData where UUID='${p.uniqueId}';")
                        rs.next()
                        if (rs.getString(1) == "null") {
                            pd.execute("update PlayerData set DefaultHome='${p.location}' where UUID='${p.uniqueId}';")
                            SPMSData.PlayerData[p.uniqueId.toString()]!!.replace("DefaultHome", p.location.toString())
                        } else {
                            throw MServerException("column DefaultHome is not null. Overwrite it?")
                        }
                    } catch (e: SQLException) {
                        e.printStackTrace()

                    } catch (e: MServerException) {
                        val base = TextComponent("§2[§3ShowPlugin§2] §3上書きする場合はこのチャットをクリック")

                        base.color = ChatColor.GREEN
                        base.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sethome #accept pd")
                        p.spigot().sendMessage(base)
                    }

                }
                return true
            }

            "sethome" -> {
                if (args.isNotEmpty()) {
                    when (args[0]) {
                        "#accept" -> {
                            try {
                                if (args[1] == p.uniqueId.toString()) {
                                    pd.execute("update PlayerData set DefaultHome='${p.location}' where UUID='${p.uniqueId}';")
                                    SPMSData.PlayerData["${p.uniqueId}"]!!.replace("DefaultHome", p.location.toString())
                                    return true
                                } else {
                                    sql.execute("update '${p.uniqueId}' set Location='${p.location}' where Name='${args[1]}';")
                                    for (m in SPMSData.HomeData["${p.uniqueId}"]!!) {
                                        if (m["Name"] == args[1]) {
                                            m.replace("Location", p.location.toString())
                                            return true
                                        }
                                    }
                                }
                            } catch (e: SQLException) {
                                e.printStackTrace()
                            }

                            return false
                        }
                        else -> {
                            val limit = SPMSData.conf.getInt("Limit.HomeRegister")
                            if (limit != -1 && list!!.size >= limit) {
                                p.sendMessage("§2[§3ShowPlugin§2] §cHomeの設定数が上限に達しているので登録出来ませんでした")
                                return true
                            }
                            try {
                                sql.execute("insert into '${p.uniqueId}'(Name, Location) values('${args[0]}', '${p.location}');")
                                map.put("ID", list!!.size + 1)
                                map.put("Name", args[0])
                                map.put("Location", p.location.toString())
                                list.add(map.clone() as LinkedHashMap<String, Any>)
                                map.clear()
                            } catch (e: SQLException) {
                                val base = TextComponent("§2[§3ShowPlugin§2] §3上書きする場合はこのチャットをクリック")
                                base.color = ChatColor.GREEN
                                base.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sethome #accept ${args[0]}")
                                p.spigot().sendMessage(base)
                            }

                            return true
                        }
                    }
                } else {
                    try {
                        if (SPMSData.PlayerData["${p.uniqueId}"]!!["DefaultHome"].equals("null")) {
                            pd.execute("update PlayerData set DefaultHome='${p.location}' where UUID='${p.uniqueId}';")
                            SPMSData.PlayerData[p.uniqueId.toString()]!!.replace("DefaultHome", p.location.toString())
                        } else {
                            throw MServerException("column DefaultHome is not null. Overwrite it?")
                        }
                    } catch (e: SQLException) {
                        e.printStackTrace()
                    } catch (e: MServerException) {
                        val base = TextComponent("§2[§3ShowPlugin§2] §3上書きする場合はこのチャットをクリック")
                        base.color = ChatColor.GREEN
                        base.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sethome #accept ${p.uniqueId}")
                        p.spigot().sendMessage(base)
                    }

                }
                return true
            }

            "deletehome" -> {
                if (args.isNotEmpty()) {
                    when (args[0]) {
                        "#all" -> {
                            try {
                                sql.execute("delete from '${p.uniqueId}';")
                                SPMSData.HomeData["${p.uniqueId}"]!!.clear()
                                return true
                            } catch (e: SQLException) {
                                return false
                            }

                            try {
                                sql.execute("delete from '${p.uniqueId}' where Name='${args[0]}';")
                                for (m in list!!) {
                                    if (m["Name"]!! == args[0]) list.remove(m)
                                }
                                return true
                            } catch (e: SQLException) {
                                return false
                            }

                        }


                        else -> try {
                            sql.execute("delete from '" + p.uniqueId.toString() + "' where Name='" + args[0] + "';")
                            for (m in list!!) {
                                if (m.get("Name")!!.equals(args[0])) list.remove(m)
                            }
                            return true
                        } catch (e: SQLException) {
                            return false
                        }

                    }
                }
                return false
            }

            "listhome" -> {
                val limit = SPMSData.conf.getInt("Limit.HomeListup")
                var count = 0
                var page = 0
                if (args.isNotEmpty()) page = Integer.parseInt(args[0])

                for (i in list!!.indices) {
                    if (args.isNotEmpty()) {
                        if (count == 0) p.sendMessage("---------- §2[§3Home List: page $page§2] §r----------")

                        if (limit * (page - 1) > i) {
                            count++
                            continue
                        }
                        p.sendMessage("・§a" + list.get(i)["Name"])

                        if (count == (limit - 1) * page + (page - 1) || count == list.size - 1) {
                            p.sendMessage("-------------------------------------")
                            return true
                        }
                        count++
                    } else {
                        if (count == 0) p.sendMessage("---------- §2[§3Home List: page 1§2] §r----------")

                        p.sendMessage("・§a" + list.get(i)["Name"])

                        if (count == limit - 1 || count == list.size - 1) {
                            p.sendMessage("-------------------------------------")
                            return true
                        }
                        count++
                    }
                }
                return false
            }

            else -> return false
        }
    }
}
