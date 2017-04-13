package com.github.show0611.showPlugins.mserver.executors

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

/**
 * Created by show0611 on 17/04/13.
 */
class CommandAdmin : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        return false
    }
}