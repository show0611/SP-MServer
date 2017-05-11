var Arrays = Java.type("java.util.Arrays");

var Bukkit = Java.type("org.bukkit.Bukkit");
var ChatColor = Java.type("org.bukkit.ChatColor");
var ItemStack = Java.type("org.bukkit.inventory.ItemStack");
var ItemFlag = Java.type("org.bukkit.inventory.ItemFlag");
var Enchantment = Java.type("org.bukkit.enchantments.Enchantment");
var Material = Java.type("org.bukkit.Material");

var Utils = {
    getOnlinePlayer: function (name) {
        var players = Bukkit.getOnlinePlayers();
        for (num in players)
            if (players.get(num).getName() == name) return players.get(num);
        return null;
    },
    getOfflinePlayer: function (name) {
        var players = Bukkit.getOfflinePlayers();
        for (num in players)
            if (players.get(num).getName() == name) return players.get(num).getPlayer();
        return null;
    },
    tacc: function (ch, str) {
        return ChatColor.translateAlternateColorCodes(ch, str);
    },
    listup: function (limit, page, list, prefix, suffix, listPrefix, listSuffix) {
        var str = "";
        var count = 0;

        for (var i in list) {
            if (count == 0) str += prefix + "\n";

            if (limit * (page - 1) > i) {
                count++;
                continue;
            }
            str += listPrefix + list.get(i).toString() + listSuffix + "\n";

            if (count == (limit - 1) * page + (page - 1) || count == list.size() - 1) {
                str += suffix + "\n";
            }
            count++;
        }

        return str;
    },
    listup1: function (limit, page, list, listPrefix) {
        return listup(limit, page, list, "", "", listPrefix, "");
    },
    generateItem: function (material, amount, name, lore, enchs, flags) {
        var item = new ItemStack(material, amount);
        var meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(lore);

        for (var i = 0; i < enchs.lenght; i++) {
            meta.addEnchant(Enchantment.getByName(enchs[i][0]), enchs[i][1], true);
        }

        for (var i = 0; i < flags.lenght; i++) {
            meta.addItemFlags(flags[i]);
        }

        item.setItemMeta(meta);

        return item;
    },
    encrypt: function (str) {
        var messageDigest = java.security.MessageDigest.getInstance("SHA-512");

        messageDigest.reset();
        messageDigest.update(java.lang.String("ShowPlugin").toByteArray());

        var hash = messageDigest.digest(str.toByteArray());
        var builder = java.lang.StringBuilder();
        for (num in hash) {
            var s = String.format("%02x", hash[num]);
            builder.append(s);
        }
        return builder.toString();
    }
};