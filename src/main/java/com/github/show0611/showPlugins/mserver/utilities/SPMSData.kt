package com.github.show0611.showPlugins.mserver.utilities

import com.github.show0611.showPlugins.mserver.Main

import java.util.ArrayList
import java.util.LinkedHashMap

/**
 * Created by show0611 on 2017/01/29.
 */
object SPMSData {
    var Muters: MutableList<String> = ArrayList()
    var HideMuters: MutableList<String> = ArrayList()
    var ShowMuters: MutableList<String> = ArrayList()
    var PermPlayers: MutableList<String> = ArrayList()
    var conf = Main.main.config
    var PlayerData = LinkedHashMap<String, LinkedHashMap<String, String>>()
    var BannedPlayers = LinkedHashMap<String, LinkedHashMap<String, String>>()
    var HomeData = LinkedHashMap<String, MutableList<LinkedHashMap<String, Any>>>()
    var Memory = mutableListOf<String>()
}
