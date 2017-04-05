package com.github.show0611.showPlugins.mserver.executors

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

/**
 * Created by show0611 on 2017/02/02.
 */
class CommandAdminChat : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        when (cmd.name.toLowerCase()) {
            "adminchat" -> {
                if (args.isEmpty()) return false
                return true
            }

            "operatorchat" -> {
                if (args.isEmpty() && sender.isOp) return false
                return true
            }

            else -> return false
        }
    }
}
