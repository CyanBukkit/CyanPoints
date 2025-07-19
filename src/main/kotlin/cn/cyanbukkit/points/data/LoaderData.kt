package cn.cyanbukkit.points.data

import cn.cyanbukkit.points.CyanPoints
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

object LoaderData {

    fun get(uuid: String): Int {
        //SELECT points FROM cyanpoints_points WHERE uuid = ?   // 从数据库中获取玩家的点数
        var points = 0
        val conn = dataSource.connection
        val stmt = conn.prepareStatement("SELECT points FROM cyanpoints_points WHERE uuid = ?")
        stmt.setString(1, uuid)
        val rs = stmt.executeQuery()
        if (rs.next()) {
            points = rs.getInt("points")
        }
        conn.close()
        return points
    }


    fun set(uuid: String, amount: Int) {
        val conn = dataSource.connection
        val stmt =
            conn.prepareStatement("INSERT INTO cyanpoints_points (uuid, points) VALUES (?, ?) ON DUPLICATE KEY UPDATE points = ?")
        stmt.setString(1, uuid)
        stmt.setInt(2, amount)
        stmt.setInt(3, amount)
        stmt.execute()
    }


    fun inTable(uuid: String): Boolean {
        var value = false
        val conn = dataSource.connection
        val stmt = conn.prepareStatement("SELECT * FROM cyanpoints_points WHERE uuid = ?")
        stmt.setString(1, uuid)
        val rs = stmt.executeQuery()
        if (rs.next()) {
            value = true
        }
        conn.close()
        return value
    }


    fun give(uuid: String, amount: Int) { // 给玩家增加点数
        dataSource.connection.use {
           it.prepareStatement("UPDATE cyanpoints_points SET points = points + $amount WHERE uuid = '$uuid'").execute()
        }
    }

    fun setAll(amount: Int) {
        val conn = dataSource.connection
        val stmt = conn.prepareStatement("UPDATE cyanpoints_points SET points = ?")
        stmt.setInt(1, amount)
        stmt.execute()
        conn.close()
    }

    fun giveAll(amount: Int) {
        val conn = dataSource.connection
        val stmt = conn.prepareStatement("UPDATE cyanpoints_points SET points = points + ?")
        stmt.setInt(1, amount)
        stmt.execute()
        conn.close()
    }


    fun isDeadbeat(uuid: String): Boolean {
        var value = false
        val conn = dataSource.connection
        val stmt = conn.prepareStatement("SELECT deadbeat FROM points_username WHERE uuid = ?")
        stmt.setString(1, uuid)
        val rs = stmt.executeQuery()
        if (rs.next()) {
            value = rs.getBoolean("deadbeat")
        }
        conn.close()
        return value
    }


    fun isPayLimit(uuid: String): Boolean {
        var value = false
        val conn = dataSource.connection
        val stmt = conn.prepareStatement("SELECT paylimit FROM points_username WHERE uuid = ?")
        stmt.setString(1, uuid)
        val rs = stmt.executeQuery()
        if (rs.next()) {
            value = rs.getBoolean("paylimit")
        }
        conn.close()
        return value
    }


    // 获取全表points最少的
    fun setDeadbeat(uuid: String, deadbeat: Boolean) {
        val conn = dataSource.connection
        // 如果没有就插入有就更新
        val stmt =
            conn.prepareStatement("INSERT INTO points_username (uuid, deadbeat, paylimit) VALUES (?, ?, 0) ON DUPLICATE KEY UPDATE deadbeat = ?")
        stmt.setString(1, uuid)
        stmt.setBoolean(2, deadbeat)
        stmt.setBoolean(3, deadbeat)
        stmt.execute()
        conn.close()
    }


    fun setPayLimit(uuid: String, payLimit: Boolean) {
        val conn = dataSource.connection
        // 如果没有就插入有就更新
        val stmt =
            conn.prepareStatement("INSERT INTO points_username (uuid, deadbeat, paylimit) VALUES (?, 0, ?) ON DUPLICATE KEY UPDATE paylimit = ?")
        stmt.setString(1, uuid)
        stmt.setBoolean(2, payLimit)
        stmt.setBoolean(3, payLimit)
        stmt.execute()
        conn.close()
    }


    private lateinit var dataSource: DataSource

    fun initialize() {
        val config = HikariConfig()
        val url = CyanPoints.instance.config.getString("MySQL.Url")
        val user = CyanPoints.instance.config.getString("MySQL.Username")
        val password = CyanPoints.instance.config.getString("MySQL.Password")
        config.jdbcUrl = url
        config.username = user
        config.password = password
        config.driverClassName = "com.mysql.cj.jdbc.Driver"
        config.maximumPoolSize = 20
        config.connectionTimeout = 120000 // 120s
        dataSource = HikariDataSource(config)
    }


    fun link() {
        println("正在连接数据库")
        initialize()

        val create = """
            CREATE TABLE IF NOT EXISTS cyanpoints_points (
                `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
                `uuid` VARCHAR(36) NOT NULL,
                `points` INTEGER NOT NULL,
                UNIQUE (uuid)
            );
        """.trimIndent()


        dataSource.connection.use { conn -> conn.prepareStatement(create).execute() }

        // deadbeat
        val cache = """
            CREATE TABLE IF NOT EXISTS points_username (
                uuid VARCHAR(36) NOT NULL,
                deadbeat TINYINT(1) NOT NULL DEFAULT 0,
                paylimit TINYINT(1) NOT NULL DEFAULT 0,
                UNIQUE (uuid)
            );
         """.trimIndent()

        dataSource.connection.use { conn -> conn.prepareStatement(cache).execute() }


    }


}