package com.github.jpvos.keylogger.plugin.model

import java.io.File
import java.nio.file.Path
import java.sql.Connection
import java.sql.Date
import java.sql.DriverManager.getConnection
import java.sql.SQLException

/**
 * A database that stores the actions that the user has performed.
 */
class Database(path: Path) {

    private val connection: Connection = connectToSQLiteFile(path)

    companion object {
        private fun connectToSQLiteFile(path: Path): Connection {
            try {
                Class.forName("org.sqlite.JDBC")
            } catch (e: ClassNotFoundException) {
                throw Error("Error while loading JDBC driver", e)
            }
            try {
                File(path.toString()).let { sqliteFile ->
                    if (!sqliteFile.exists()) {
                        sqliteFile.parentFile.mkdirs()
                    }
                }
            } catch (e: Exception) {
                throw Error("Error while creating database file", e)
            }
            try {
                return getConnection("jdbc:sqlite:file:$path")
            } catch (e: SQLException) {
                throw Error("Error while establishing connection to database", e)
            }
        }
    }

    init {
        createDatabaseSchemaIfNotExists()
        validateDatabaseSchema()
    }

    fun deleteAllActions() {
        connection.createStatement().execute("DELETE FROM actions")
    }

    fun deleteActionsByName(name: String) {
        connection.prepareStatement("DELETE FROM actions WHERE name = ?").apply {
            setString(1, name)
            execute()
        }
    }

    fun queryActionHistory(size: Int): List<Pair<Action, Long>> {
        connection
            .prepareStatement("SELECT type, name, timestamp FROM actions ORDER BY timestamp DESC LIMIT ?")
            .apply { setInt(1, size) }
            .executeQuery()
            .apply {
                val list = mutableListOf<Pair<Action, Long>>()
                while (next()) {
                    val type = Action.Type.parse(getString("type"))
                    val name = getString("name")
                    val timestamp = getLong("timestamp")
                    list.add(Action(type, name) to timestamp)
                }
                return list
            }
    }

    fun queryActionMap(): Map<Action, Long> {
        connection
            .createStatement()
            .executeQuery("SELECT type, name, count(*) as count FROM actions GROUP BY type, name")
            .apply {
                val map = mutableMapOf<Action, Long>()
                while (next()) {
                    val type = Action.Type.parse(getString("type"))
                    val name = getString("name")
                    val count = getLong("count")
                    map[Action(type, name)] = count
                }
                return map
            }
    }

    fun queryActionsByWeekdayAndTime(timeIntervalHours: Int): List<Triple<Int, Int, Long>> {
        require(timeIntervalHours in 1..24)
        connection
            .prepareStatement(
                """
                    SELECT
                        strftime('%w', datetime(timestamp / 1000, 'unixepoch')) AS day_of_week,
                        CAST(strftime('%H', datetime(timestamp / 1000, 'unixepoch')) AS INTEGER) / ? AS time_interval,
                        COUNT(*) AS number_of_actions
                    FROM
                        actions
                    GROUP BY
                        day_of_week,
                        time_interval
                    ORDER BY
                        day_of_week,
                        time_interval
            """
            ).apply {
                setInt(1, timeIntervalHours)
            }
            .executeQuery()
            .apply {
                val result = mutableListOf<Triple<Int, Int, Long>>()
                while (next()) {
                    val dayOfWeek = getInt("day_of_week")
                    val timeInterval = getInt("time_interval")
                    val numberOfActions = getLong("number_of_actions")
                    result.add(Triple(dayOfWeek, timeInterval, numberOfActions))
                }
                return result
            }
    }

    fun queryActivityPerDayAndType(): List<Triple<Date, String, Long>> {
        connection
            .createStatement()
            .executeQuery(
                """
                    SELECT 
                        date(timestamp / 1000, 'unixepoch') AS day, 
                        type,
                        COUNT(*) AS number_of_actions 
                    FROM 
                        actions 
                    GROUP BY 
                        day, 
                        type
                    ORDER BY 
                        day
                    """
            )
            .apply {
                val result = mutableListOf<Triple<Date, String, Long>>()
                while (next()) {
                    val dayString = getString("day")
                    val day = Date.valueOf(dayString)
                    val type = getString("type")
                    val numberOfActions = getLong("number_of_actions")
                    result.add(Triple(day, type, numberOfActions))
                }
                return result
            }
    }

    fun queryActivityPerDay(): List<Pair<Date, Long>> {
        connection
            .createStatement()
            .executeQuery(
                """
                    SELECT 
                        date(timestamp / 1000, 'unixepoch') AS day, 
                        COUNT(*) AS number_of_actions 
                    FROM 
                        actions 
                    GROUP BY 
                        day
                    ORDER BY 
                        day
                    """
            )
            .apply {
                val result = mutableListOf<Pair<Date, Long>>()
                while (next()) {
                    val dayString = getString("day")
                    val day = Date.valueOf(dayString)
                    val numberOfActions = getLong("number_of_actions")
                    result.add(day to numberOfActions)
                }
                return result
            }
    }

    fun queryActiveAndIdleTime(idleTimeout: Long): Pair<Long, Long> {
        connection
            .createStatement()
            .executeQuery("SELECT timestamp FROM actions ORDER BY timestamp ASC")
            .apply {
                // this implementation is not efficient, but it is good enough for now. will it stand the test of time?
                var currentTime: Long? = null
                var activeTime = 0L
                var idleTime = 0L
                while (next()) {
                    val timestamp = getLong("timestamp")
                    if (currentTime == null) {
                        currentTime = timestamp
                    }
                    val deltaTime = timestamp - currentTime
                    if (deltaTime < idleTimeout) {
                        activeTime += deltaTime
                    } else {
                        idleTime += deltaTime
                    }
                    currentTime = timestamp
                }
                return activeTime to idleTime
            }
    }

    fun batchPersist(actions: Iterable<Action>) {
        connection.prepareStatement("INSERT INTO actions (type, name, timestamp) VALUES (?, ?, ?)").apply {
            for (action in actions) {
                setString(1, action.type.toString())
                setString(2, action.name)
                setLong(3, System.currentTimeMillis())
                addBatch()
            }
            executeBatch()
        }
    }

    fun persist(action: Action) {
        connection.prepareStatement("INSERT INTO actions (type, name, timestamp) VALUES (?, ?, ?)").apply {
            setString(1, action.type.toString())
            setString(2, action.name)
            setLong(3, System.currentTimeMillis())
            execute()
        }
    }

    fun close() {
        if (connection.isClosed) {
            throw Error("Connection to database is already closed")
        }
        try {
            connection.close()
        } catch (e: SQLException) {
            throw Error("Error while closing connection to database", e)
        }
    }

    private fun createDatabaseSchemaIfNotExists() {
        connection.createStatement()
            .execute("CREATE TABLE IF NOT EXISTS actions (type TEXT NOT NULL, name TEXT NOT NULL, timestamp LONG NOT NULL)")
    }

    private fun validateDatabaseSchema() {
        val expectedSchema = mapOf(
            "type" to "TEXT",
            "name" to "TEXT",
            "timestamp" to "LONG"
        )
        val actualSchema = connection.createStatement().executeQuery("PRAGMA table_info(actions)").let { result ->
            val schema = mutableMapOf<String, String>()
            while (result.next()) {
                val columnName = result.getString("name")
                val columnType = result.getString("type")
                schema[columnName] = columnType
            }
            schema
        }
        if (actualSchema != expectedSchema) {
            throw Error("Table schema does not match expected schema")
        }
    }

}
