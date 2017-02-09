package com.github.show0611.showPlugins.mserver.executors;

import com.github.show0611.showPlugins.mserver.Main;
import com.github.show0611.showPlugins.mserver.utilities.SPMSData;
import com.github.show0611.showPlugins.mserver.utilities.SQLite;
import com.github.show0611.showPlugins.mserver.utilities.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by show0611 on 2017/01/28.
 */
public class CommandHome implements CommandExecutor {
    private SQLite sql = Main.home;
    private SQLite pd = Main.pd;
    private List<String> ipl = new ArrayList<>();
    private Map<String, Integer> counter = new HashMap<>();
    private int delay = SPMSData.conf.getInt("Delay.InviteDelay");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        BukkitScheduler scheduler = Main.server.getScheduler();
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        List<LinkedHashMap<String, Object>> list = SPMSData.HomeData.get(p.getUniqueId().toString());
        List<LinkedHashMap<String, Object>> invite = (List<LinkedHashMap<String, Object>>) p.getMetadata("Invites").get(0).value();

        switch (cmd.getName().toLowerCase()) {
        case ("home"):
            if (args.length != 0) {
                switch (args[0]) {
                case ("#invite"):
                    Player pl = Utils.getOnlinePlayer(args[2]);
                    Location loc = null;
                    int id = invite.size() + 1;
                    if (pl == null) return false;

                    for (String str : ipl) {
                        if (str.equals(p.getUniqueId().toString())) {
                            p.sendMessage("§2[§3ShowPlugin§2] 前回の招待から" + delay + "秒後に招待を送ってください。");
                            return true;
                        }
                    }
                    int limit = SPMSData.conf.getInt("Limit.InviteStack");
                    if (limit != -1 && invite.size() < limit) {
                        p.sendMessage("§2[§3ShowPlugin§2] §c" + args[2] + "さんへの招待が上限数に達しているのでしばらく待ってからもう一度招待してください");
                        return true;
                    }

                    TextComponent pre = new TextComponent("§2[§3ShowPlugin§2] §r");
                    TextComponent base = new TextComponent(p.getName() + "さんからHomeへの招待が来ています");
                    TextComponent tc = new TextComponent("§2[§3ShowPlugin§2] §3許可するならこのチャットをクリック");
                    base.setColor(ChatColor.GREEN);
                    tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home #accept " + id));
                    pre.addExtra(base);

                    p.spigot().sendMessage(pre);
                    p.spigot().sendMessage(tc);

                    for (LinkedHashMap<String, Object> m : SPMSData.HomeData.get(p.getUniqueId().toString())) {
                        if (m.get("Name").equals(args[1])) {
                            loc = Utils.toLocation(m.get("Location").toString());
                            break;
                        }
                    }

                    counter.put(p.getUniqueId().toString(), delay);
                    map.put("ID", id);
                    map.put("Name", p.getName());
                    map.put("Location", loc);
                    invite.add((LinkedHashMap<String, Object>) map.clone());
                    ipl.add(p.getUniqueId().toString());
                    map.clear();

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (LinkedHashMap<String, Object> m : invite) {
                                if (m.get("ID").equals(id)) {
                                    pl.sendMessage("§2[§3ShowPlugin§2] §r" + m.get("Name") + "さんからの招待が削除されました");
                                    invite.remove(m);
                                }
                            }
                        }
                    }.runTaskLater(Main.main, SPMSData.conf.getInt("Delay.InviteRemoveDelay") * 20);
                    scheduler.scheduleSyncRepeatingTask(Main.main, new BukkitRunnable() {
                        int count = counter.get(p.getUniqueId().toString());
                        @Override
                        public void run() {
                            if (count > 0) counter.replace(p.getUniqueId().toString(), --count);
                            else if (count == 0) this.cancel();
                        }
                    }, 0, 20);
                    return true;

                case ("#accept"):
                    if (args.length != 1) {
                        int iid = Integer.parseInt(args[1]);
                        for (Map<String, Object> m : invite) {
                            if ((int) m.get("ID") == iid) {
                                p.teleport((Location) m.get("Location"));
                                return true;
                            }
                        }
                    }
                    return false;

                default:
                    for (Map<String, Object> m : SPMSData.HomeData.get(p.getUniqueId().toString())) {
                        if (m.get("Name").equals(args[0])) {
                            p.teleport(Utils.toLocation(m.get("Location").toString()));
                            return true;
                        }
                    }
                    return false;
                }
            } else {
                try {
                    p.teleport(Utils.toLocation(SPMSData.PlayerData.get(p.getUniqueId().toString()).get("DefaultHome")));
                } catch (Exception e) {
                    p.sendMessage("§2[§3ShowPlugin§2] §cHomeが設定されていません。/sethomeコマンドを実行してから使用してください。");
                }
                return true;
            }

        case ("sethome"):
            if (args.length != 0) {
                switch (args[0]) {
                case ("#accept"):
                    try {
                        if (args[2].equals("pd")) {
                            pd.execute("update PlayerData set DefaultHome='" + p.getLocation().toString() + "' where UUID='" + p.getUniqueId().toString() + "';");
                            SPMSData.PlayerData.get(p.getUniqueId().toString()).replace("DefaultHome", p.getLocation().toString());
                            return true;
                        } else {
                            sql.execute("update '" + p.getUniqueId().toString() +
                                    "' set Location='" + p.getLocation().toString() + "' where Name='" + args[1] + "';");
                            for (LinkedHashMap<String, Object> m : SPMSData.HomeData.get(p.getUniqueId().toString())) {
                                for (Map.Entry<String, Object> entry : m.entrySet()) {
                                    if (entry.getKey().equals(args[1])) {
                                        m.replace(args[1], p.getLocation().toString());
                                        return true;
                                    }
                                }
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return false;

                default:
                    int limit = SPMSData.conf.getInt("Limit.HomeRegister");
                    if (limit != -1 && list.size() < limit) {
                        p.sendMessage("§2[§3ShowPlugin§2] §cHomeの設定数が上限に達しているので登録出来ませんでした");
                        return true;
                    }
                    try {
                        sql.execute("insert into " + p.getUniqueId().toString() + "(Name, Location) values('" + args[0] + "', '" + p.getLocation().toString() + "');");

                        map.put("ID", list.size() + 1);
                        map.put("Name", args[0]);
                        map.put("Location", p.getLocation().toString());
                        list.add((LinkedHashMap<String, Object>) map.clone());
                        map.clear();
                    } catch (SQLException e) {
                        TextComponent base = new TextComponent("§2[§3ShowPlugin§2] §3上書きする場合はこのチャットをクリック");

                        base.setColor(ChatColor.GREEN);
                        base.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sethome #accept " + args[0]));
                        p.spigot().sendMessage(base);
                    }
                    return true;
                }
            } else {
                try {
                    ResultSet rs = pd.executeQuery("select DefaultHome from PlayerData where UUID='" + p.getUniqueId().toString() + "';");
                    rs.next();
                    if (rs.getString(1).equals("null")) {
                        pd.execute("update PlayerData set DefaultHome='" + p.getLocation().toString() + "' where UUID='" + p.getUniqueId().toString() + "';");
                        SPMSData.PlayerData.get(p.getUniqueId().toString()).replace("DefaultHome", p.getLocation().toString());
                    } else {
                        throw new SQLException("column DefaultHome is not null. Overwrite it?");
                    }
                } catch (SQLException e) {
                    TextComponent base = new TextComponent("§2[§3ShowPlugin§2] §3上書きする場合はこのチャットをクリック");

                    base.setColor(ChatColor.GREEN);
                    base.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sethome #accept " + args[0] + " pd"));
                    p.spigot().sendMessage(base);
                }
            }
            return true;

        case ("deletehome"):
            if (args.length != 0) {
                switch (args[0]) {
                case ("#all"):
                    try {
                        sql.execute("delete from " + p.getUniqueId().toString() + ";");
                        SPMSData.HomeData.get(p.getUniqueId().toString()).clear();
                        return true;
                    } catch (SQLException e) {
                        return false;
                    }


                default:
                    try {
                        sql.execute("delete from " + p.getUniqueId().toString() + " where Name='" + args[0] + "';");
                        for (LinkedHashMap<String, Object> m : list) {
                            if (m.get("Name").equals(args[0])) list.remove(m);
                        }
                        return true;
                    } catch (SQLException e) {
                        return false;
                    }
                }
            }
            return false;

        case ("listhome"):
            int limit = SPMSData.conf.getInt("Limit.HomeListup");
            int count = 0;
            int page = 0;
            if (args.length != 0) page = Integer.parseInt(args[0]);

            for (int i = 0; i < list.size(); i++) {
                if (args.length != 0) {
                    if (count == 0) p.sendMessage("---------- §2[§3Home List: page " + page + "§2] §r----------");

                    if (limit * (page - 1) > i) {
                        count++;
                        continue;
                    }
                    p.sendMessage("・§a" + list.get(i).get("Name"));

                    if (count == (limit - 1) * page + (page - 1) || count == list.size() - 1) {
                        p.sendMessage("-------------------------------------");
                        return true;
                    }
                    count++;
                } else {
                    if (count == 0) p.sendMessage("---------- §2[§3Home List: page 1§2] §r----------");

                    p.sendMessage("・§a" + list.get(i).get("Name"));

                    if (count == (limit - 1) || count == list.size() - 1) {
                        p.sendMessage("-------------------------------------");
                        return true;
                    }
                    count++;
                }
            }
            return false;

        default:
            return false;
        }
    }
}
