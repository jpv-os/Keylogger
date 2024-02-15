package com.github.jpvos.keylogger.plugin.services

import com.github.jpvos.keylogger.core.Action
import com.github.jpvos.keylogger.db.SqliteDatabaseConnection
import com.github.jpvos.keylogger.plugin.settings.KeyloggerSettings
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service

@Service
class DatabaseService : Disposable {
    private var db = SqliteDatabaseConnection()

    init {
        restoreDatabase()
    }

    override fun dispose() {
        db.close()
    }

    fun clearDatabase() {
        verifyDatabaseConnection()
        db.statement("DELETE FROM actions")
    }

    fun restoreDatabase() {
        dispose()
        db.connect(KeyloggerSettings.instance.databaseURL)
        db.statement("CREATE TABLE IF NOT EXISTS actions (type TEXT NOT NULL, name TEXT NOT NULL, timestamp LONG NOT NULL)")
    }

    fun queryActionsMap(): Map<Action, Long> {
        verifyDatabaseConnection()
        return db.query("SELECT type, name, count(*) as count FROM actions GROUP BY type, name") {
            val result = mutableMapOf<Action, Long>()
            while (next()) {
                val type = getString("type").let {
                    when (it) {
                        "EditorAction" -> Action.Type.EditorAction
                        "Keystroke" -> Action.Type.Keystroke
                        "Mouse" -> Action.Type.Mouse
                        else -> throw Error("Could not read action type: $it")
                    }
                }
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
            var currentTime: Long? = null
            var activeTime = 0L
            var idleTime = 0L
            while (next()) {
                val timestamp = getLong("timestamp")
                if (currentTime == null) {
                    currentTime = timestamp
                }
                val deltaTime = timestamp - currentTime
                if (deltaTime < KeyloggerSettings.instance.idleTimeout) {
                    activeTime += deltaTime
                } else {
                    idleTime += deltaTime
                }
                currentTime = timestamp
            }
            return@query activeTime to idleTime
        }
    }

    fun persistAction(action: Action) {
        verifyDatabaseConnection()
        db.preparedStatement("INSERT INTO actions (type, name, timestamp) VALUES (?, ?, ?)") {
            setString(1, action.type.name)
            setString(2, action.name)
            setLong(3, System.currentTimeMillis())
            execute()
        }
    }

    private fun verifyDatabaseConnection() {
        if (!db.open) throw Error("Database is not open, initialize database first")
    }

}
