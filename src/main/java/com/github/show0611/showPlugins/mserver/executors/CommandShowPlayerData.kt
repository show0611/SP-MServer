package com.github.show0611.showPlugins.mserver.executors

import com.github.show0611.showPlugins.mserver.utilities.SPMSData
import com.github.show0611.showPlugins.mserver.utilities.Utils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer
import java.util.*

/**
 * Created by show0611 on 2017/01/30.
 */
class CommandShowPlayerData : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        val p = sender as CraftPlayer
        val map: LinkedHashMap<String, String>

        when (cmd.name.toLowerCase()) {
            "showplayerdata" -> {
                if (args.isNotEmpty()) {
                    p.sendMessage("----- §2[§3" + args[0] + "'s Data§2] §r-----")
                    val cp = if (Utils.getOnlinePlayer(args[0]) == null) Utils.getOfflinePlayer(args[0]) as CraftPlayer else Utils.getOnlinePlayer(args[0]) as CraftPlayer
                    if (cp.isBanned)
                        map = SPMSData.BannedPlayers[cp.uniqueId.toString()]!!
                    else
                        map = SPMSData.PlayerData[cp.uniqueId.toString()]!!

                    if (!p.isOp && cp.isOp) {
                        p.sendMessage("§2[§3ShowPlugin§2] §cYou can't look " + args[0] + " data!")
                        return true
                    }
                    val logloc = Utils.toLocation(map["LastLogoutLocation"])

                    p.sendMessage("§2[§3ShowPlugin§2] §bName§r: " + map["PlayerName"])
                    p.sendMessage("§2[§3ShowPlugin§2] §bUUID§r: " + map["UUID"])
                    p.sendMessage("§2[§3ShowPlugin§2] §bIP§r: " + map["GlobalIP"])
                    p.sendMessage("§2[§3ShowPlugin§2] §bLanguage§r: " + cp.handle.locale)
                    p.sendMessage("§2[§3ShowPlugin§2] §bFirstLogin§r: " + map["FirstLogin"])
                    p.sendMessage("§2[§3ShowPlugin§2] §bLastLogout§r: " + map["LastLogout"])
                    p.sendMessage("§2[§3ShowPlugin§2] §bLatestLogin§r: " + map["LatestLogin"])
                    if (logloc != null) {
                        p.sendMessage("§2[§3ShowPlugin§2] §bLastLogoutLocation§r:")
                        p.sendMessage("§2[§3ShowPlugin§2] §9World§r: " + logloc.world.name)
                        p.sendMessage("§2[§3ShowPlugin§2] §9X§r: " + logloc.blockX)
                        p.sendMessage("§2[§3ShowPlugin§2] §9Y§r: " + logloc.blockY)
                        p.sendMessage("§2[§3ShowPlugin§2] §9Z§r: " + logloc.blockZ)
                    } else
                        p.sendMessage("§2[§3ShowPlugin§2] §bLastLogoutLocation§r: null")

                    if (p.isOp || Utils.check(p.uniqueId.toString())) {
                        val bed = Utils.toLocation(map["BedLocation"])
                        val def = Utils.toLocation(map["DefaultHome"])
                        p.sendMessage("§2[§3ShowPlugin§2] §9OP§r: " + cp.isOp)
                        p.sendMessage("§2[§3ShowPlugin§2] §9Banned§r: " + cp.isBanned)
                        p.sendMessage("§2[§3ShowPlugin§2] §9AllowFlight§r: " + cp.allowFlight)
                        p.sendMessage("§2[§3ShowPlugin§2] §9GameMode§r: " + cp.gameMode)
                        if (bed != null) {
                            p.sendMessage("§2[§3ShowPlugin§2] §9BedLocation§r:")
                            p.sendMessage("§2[§3ShowPlugin§2] §9    World§r: " + bed.world.name)
                            p.sendMessage("§2[§3ShowPlugin§2] §9    X§r: " + bed.blockX)
                            p.sendMessage("§2[§3ShowPlugin§2] §9    Y§r: " + bed.blockY)
                            p.sendMessage("§2[§3ShowPlugin§2] §9    Z§r: " + bed.blockZ)
                        } else
                            p.sendMessage("§2[§3ShowPlugin§2] §9BedLocation§r: null")
                        if (def != null) {
                            p.sendMessage("§2[§3ShowPlugin§2] §9DefaultHome§r:")
                            p.sendMessage("§2[§3ShowPlugin§2] §9    World§r: " + def.world.name)
                            p.sendMessage("§2[§3ShowPlugin§2] §9    X§r: " + def.blockX)
                            p.sendMessage("§2[§3ShowPlugin§2] §9    Y§r: " + def.blockY)
                            p.sendMessage("§2[§3ShowPlugin§2] §9    Z§r: " + def.blockZ)
                        } else
                            p.sendMessage("§2[§3ShowPlugin§2] §9DefaultHome§r: null")
                    }
                } else {
                    p.sendMessage("----- §2[§3" + p.name + "'s Data§2] §r-----")
                    map = SPMSData.PlayerData[p.uniqueId.toString()]!!
                    println(map)
                    val bed = Utils.toLocation(map["BedLocation"])
                    val def = Utils.toLocation(map["DefaultHome"])
                    val logloc = Utils.toLocation(map["LastLogoutLocation"])
                    p.sendMessage("§2[§3ShowPlugin§2] §bName§r: " + map["PlayerName"])
                    p.sendMessage("§2[§3ShowPlugin§2] §bUUID§r: " + map["UUID"])
                    p.sendMessage("§2[§3ShowPlugin§2] §bIP§r: " + map["GlobalIP"])
                    p.sendMessage("§2[§3ShowPlugin§2] §bLanguage§r: " + p.handle.locale)
                    p.sendMessage("§2[§3ShowPlugin§2] §bFirstLogin§r: " + map["FirstLogin"])
                    p.sendMessage("§2[§3ShowPlugin§2] §bLastLogout§r: " + map["LastLogout"])
                    p.sendMessage("§2[§3ShowPlugin§2] §bLatestLogin§r: " + map["LatestLogin"])
                    p.sendMessage("§2[§3ShowPlugin§2] §bLastLogoutLocation§r: ")
                    if (logloc != null) {
                        p.sendMessage("§2[§3ShowPlugin§2] §bLastLogoutLocation§r:")
                        p.sendMessage("§2[§3ShowPlugin§2] §9World§r: " + logloc.world.name)
                        p.sendMessage("§2[§3ShowPlugin§2] §9X§r: " + logloc.blockX)
                        p.sendMessage("§2[§3ShowPlugin§2] §9Y§r: " + logloc.blockY)
                        p.sendMessage("§2[§3ShowPlugin§2] §9Z§r: " + logloc.blockZ)
                    } else
                        p.sendMessage("§2[§3ShowPlugin§2] §bLastLogoutLocation§r: null")
                    p.sendMessage("§2[§3ShowPlugin§2] §9OP§r: " + p.isOp)
                    p.sendMessage("§2[§3ShowPlugin§2] §9Banned§r: " + p.isBanned)
                    p.sendMessage("§2[§3ShowPlugin§2] §9AllowFlight§r: " + p.allowFlight)
                    p.sendMessage("§2[§3ShowPlugin§2] §9GameMode§r: " + p.gameMode)
                    if (bed != null) {
                        p.sendMessage("§2[§3ShowPlugin§2] §9BedLocation§r: ")
                        p.sendMessage("§2[§3ShowPlugin§2] §9    World§r: " + bed.world.name)
                        p.sendMessage("§2[§3ShowPlugin§2] §9    X§r: " + bed.blockX)
                        p.sendMessage("§2[§3ShowPlugin§2] §9    Y§r: " + bed.blockY)
                        p.sendMessage("§2[§3ShowPlugin§2] §9    Z§r: " + bed.blockZ)
                    } else
                        p.sendMessage("§2[§3ShowPlugin§2] §9BedLocation§r: null")
                    if (def != null) {
                        p.sendMessage("§2[§3ShowPlugin§2] §9DefaultHome§r: ")
                        p.sendMessage("§2[§3ShowPlugin§2] §9    World§r: " + def.world.name)
                        p.sendMessage("§2[§3ShowPlugin§2] §9    X§r: " + def.blockX)
                        p.sendMessage("§2[§3ShowPlugin§2] §9    Y§r: " + def.blockY)
                        p.sendMessage("§2[§3ShowPlugin§2] §9    Z§r: " + def.blockZ)
                    } else
                        p.sendMessage("§2[§3ShowPlugin§2] §9DefaultHome§r: null")
                }
                p.sendMessage("----------")
                return true
            }

            else -> return false
        }
    }
}
