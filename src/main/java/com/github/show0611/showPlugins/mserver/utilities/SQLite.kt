package com.github.show0611.showPlugins.mserver.utilities

import java.io.File
import java.io.IOException
import java.sql.*

/**
 * Created by show0611 on 2017/01/28.
 */
class SQLite {
    private var con: Connection? = null
    private var stat: Statement? = null
    private var db: File? = null

    @Throws(SQLException::class, IOException::class)
    fun open(path: String) {
        db = File(path)
        con = DriverManager.getConnection(path)
        stat = con!!.createStatement()
    }

    @Throws(SQLException::class)
    fun executeQuery(cmd: String): ResultSet {
        return stat!!.executeQuery(cmd)
    }

    @Throws(SQLException::class)
    fun execute(cmd: String): Boolean {
        return stat!!.execute(cmd)
    }

    companion object {
        private var JDBC: Class<*>? = null

        init {
            try {
                JDBC = Class.forName("org.sqlite.JDBC")
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
        }
    }
}
