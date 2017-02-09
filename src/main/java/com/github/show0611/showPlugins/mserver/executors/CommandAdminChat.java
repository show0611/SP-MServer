package com.github.show0611.showPlugins.mserver.executors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by show0611 on 2017/02/02.
 */
public class CommandAdminChat implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        switch (cmd.getName().toLowerCase()) {
        case ("adminchat"):
            if (args.length == 0) return false;
            return true;

        case ("operatorchat"):
            if (args.length == 0 || sender.isOp()) return false;
            return true;

        default:
            return false;
        }
    }
}
