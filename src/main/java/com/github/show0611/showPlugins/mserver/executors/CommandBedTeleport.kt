package com.github.show0611.showPlugins.mserver.executors

import com.github.show0611.showPlugins.mserver.utilities.SPMSData
import com.github.show0611.showPlugins.mserver.utilities.Utils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer

/**
 * Created by show0611 on 17/04/17.
 */
class CommandBedTeleport : CommandExecutor {
    val pd = SPMSData.PlayerData

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        val cp = sender as CraftPlayer

        when (cmd.name.toLowerCase()) {
            "bedtelport" -> {
                cp.teleport(Utils.toLocation(pd[cp.uniqueId.toString()]!!["BedLocation"]))
            }
        }
        return false
    }
}