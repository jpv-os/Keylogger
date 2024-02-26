package com.github.jpvos.keylogger.plugin.services

import com.github.jpvos.keylogger.core.Action
import com.github.jpvos.keylogger.plugin.db.SqliteDatabaseConnection
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger

@Service
class DatabaseService : Disposable {
    private var db = SqliteDatabaseConnection()

    init {
        restoreDatabase()
    }

    override fun dispose() {
        db.close()
    }

    fun restoreDatabase() {
        dispose()
        connect()
        db.statement("CREATE TABLE IF NOT EXISTS actions (type TEXT NOT NULL, name TEXT NOT NULL, timestamp LONG NOT NULL)")
    }

    fun clearDatabase() {
        verifyDatabaseConnection()
        db.statement("DELETE FROM actions")
    }

    fun queryActionsHistory(size: Long): List<Pair<Action, Long>> {
        verifyDatabaseConnection()
        return db.preparedQuery(
            "SELECT type, name, timestamp FROM actions ORDER BY timestamp DESC LIMIT ?",
            { setLong(1, size) }
        ) {
            val result = mutableListOf<Pair<Action, Long>>()
            while (next()) {
                val type = Action.Type.parse(getString("type"))
                val name = getString("name")
                val timestamp = getLong("timestamp")
                result.add(Action(type, name) to timestamp)
            }
            return@preparedQuery result
        }
    }

    fun queryActionsMap(): Map<Action, Long> {
        verifyDatabaseConnection()
        return db.query("SELECT type, name, count(*) as count FROM actions GROUP BY type, name") {
            val result = mutableMapOf<Action, Long>()
            while (next()) {
                val type = Action.Type.parse(getString("type"))
                val name = getString("name")
                val count = getLong("count")
                result[Action(type, name)] = count
            }
            return@query result
        }
    }

    fun queryActiveAndIdleTime(): Pair<Long, Long> {
        verifyDatabaseConnection()
        // this implementation is not efficient, but it is good enough for now. will it stand the test of time?
        return db.query("SELECT timestamp FROM actions ORDER BY timestamp ASC") {
            val idleTimeout = service<SettingsService>().idleTimeout
            val timerStart = System.currentTimeMillis()
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
            val timerDuration = System.currentTimeMillis() - timerStart
            thisLogger().warn("Restoring active/idle time took $timerDuration ms")
            return@query activeTime to idleTime
        }
    }

    fun persistAction(action: Action) {
        verifyDatabaseConnection()
        db.preparedStatement("INSERT INTO actions (type, name, timestamp) VALUES (?, ?, ?)") {
            setString(1, action.type.toString())
            setString(2, action.name)
            setLong(3, System.currentTimeMillis())
        }
    }

    fun cleanIdeaVimActions() {
        verifyDatabaseConnection()
        db.statement("DELETE FROM actions WHERE name = 'Shortcuts' AND type = 'EditorAction'")
    }

    private fun connect() {
        if (db.open) db.close()
        db.connect(service<SettingsService>().activeDatabaseUrl)
    }

    private fun verifyDatabaseConnection() {
        if (!db.open) throw Error("Database is not open, initialize database first")
    }
}
