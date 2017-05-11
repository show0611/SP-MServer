var SQLite = {
    val: {
        db: null,
        con: null,
        stat: null
    },
    open: function (path) {
        val.db = new java.io.File(path);
        val.con = java.sql.DriverManager.getConnection(path);
        val.stat = val.con.createStatement();
    },
    execute: function (cmd) {
        return val.stat.execute(cmd);
    },
    executeQuery: function (cmd) {
        return val.stat.executeQuery(cmd);
    }
};