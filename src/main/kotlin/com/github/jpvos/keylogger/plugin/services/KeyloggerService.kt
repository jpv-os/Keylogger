package com.github.jpvos.keylogger.plugin.services

import com.github.jpvos.keylogger.core.Action
import com.github.jpvos.keylogger.core.Counter
import com.github.jpvos.keylogger.db.DatabaseConnection
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger


@Service
class KeyloggerService : Counter.Listener, Disposable { // TODO: disposable is not correct for service?
    val counter = Counter()
    private val db = DatabaseConnection("jdbc:sqlite:file:/tmp/keylogger.db") // TODO: configurable
//    private val db = DatabaseConnection("jdbc:sqlite:keylogger.db")

    init {
        initializeDatabase()
        loadInitialState()
        counter.registerListener(this)
    }

    private fun loadInitialState() {
        val actions = db.statement {
            val resultSet = executeQuery("SELECT type, name, count(*) as count FROM actions GROUP BY type, name")
            val result = mutableMapOf<Action, Long>()
            while (resultSet.next()) {
                val type = resultSet.getString("type").let {
                    when (it) {
                        "EditorAction" -> Action.Type.EditorAction
                        "Keystroke" -> Action.Type.Keystroke
                        "Mouse" -> Action.Type.Mouse
                        else -> throw Error("Could not read action type: $it")
                    }
                }
                val name = resultSet.getString("name")
                val count = resultSet.getLong("count")
                val action = Action(type, name)
                result[action] = count
            }
            return@statement result
        }

        val initialState = Counter.State(
            activeTime = 0, // TODO
            idleTime = 0,   // TODO
            actions = actions
        )
        counter.setState(initialState)
    }

    override fun dispose() {
        counter.unregisterListener(this)
        db.close()
    }

    override fun onAction(action: Action) {
        db.preparedStatement("INSERT INTO actions (type, name, timestamp) VALUES (?, ?, ?)") {
            setString(1, action.type.name)
            setString(2, action.name)
            setLong(3, System.currentTimeMillis())
            execute()
        }
        // TODO doesnt work?
    }

    private fun initializeDatabase() {
        thisLogger().warn("Initializing Keylogger database")
        db.statement {
            execute("CREATE TABLE IF NOT EXISTS meta (key TEXT NOT NULL UNIQUE, value TEXT)")
        }
        val pluginVersion = db.statement {
            val resultSet = executeQuery("SELECT value FROM meta WHERE key = 'plugin_version'")
            return@statement if (resultSet.next()) resultSet.getString("value") else null
        }
        pluginVersion?.let {
            thisLogger().warn("Plugin database version $it detected")
        } ?: run {
            thisLogger().warn("No plugin database version found, create tables")
            db.statement {
                execute("INSERT INTO meta (key, value) VALUES ('plugin_version', '1')")
                execute("CREATE TABLE IF NOT EXISTS actions (type TEXT NOT NULL, name TEXT NOT NULL, timestamp LONG NOT NULL)")
            }
        }
        thisLogger().warn("Keylogger database initialized")
    }
}
