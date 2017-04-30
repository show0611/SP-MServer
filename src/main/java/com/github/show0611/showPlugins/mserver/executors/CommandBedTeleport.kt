package com.github.show0611.showPlugins.mserver.executors

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

/**
 * Created by show0611 on 17/04/17.
 */
class CommandBedTeleport : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        when (cmd.name.toLowerCase()) {
            "bedtelport" -> {
            }
        }
        return false
    }
}