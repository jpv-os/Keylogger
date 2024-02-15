package com.github.jpvos.keylogger.plugin.services

import com.github.jpvos.keylogger.core.Action
import com.github.jpvos.keylogger.core.Counter
import com.github.jpvos.keylogger.db.DatabaseConnection
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import kotlin.io.path.Path


// TODO: does Disposable work correctly for this service?
@Service
class KeyloggerService : Counter.Listener, Disposable {
    private val filePath = Path(System.getProperty("user.home"), "intellij-plugin-keylogger.sqlite") // TODO: configurable
    private val idleTimeout = 1000L // TODO: configurable
    private val db = DatabaseConnection("jdbc:sqlite:file:$filePath")
    private val logger = thisLogger()
    val counter = Counter(idleTimeout = idleTimeout)

    init {
        initializeDatabase()
        loadInitialState()
        counter.registerListener(this)
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
    }

    private fun initializeDatabase() {
        logger.warn("Initializing Keylogger database")
        db.statement("CREATE TABLE IF NOT EXISTS meta (key TEXT NOT NULL UNIQUE, value TEXT)")
        val pluginVersion = db.query("SELECT value FROM meta WHERE key = 'plugin_version'") {
            if (next()) getString("value") else null
        }
        pluginVersion?.let {
            logger.warn("Plugin database version $it detected")
        } ?: run {
            logger.warn("No plugin database version found, create tables")
            db.statement("INSERT INTO meta (key, value) VALUES ('plugin_version', '1')")
            db.statement("CREATE TABLE IF NOT EXISTS actions (type TEXT NOT NULL, name TEXT NOT NULL, timestamp LONG NOT NULL)")
        }
        logger.warn("Keylogger database initialized")
    }

    private fun loadInitialState() {
        val actions = readActionsFromDatabase()
        val (activeTime, idleTime) = readActiveAndIdleTimeFromDatabase()
        counter.setState(Counter.State(actions, activeTime, idleTime))
    }

    private fun readActionsFromDatabase(): Map<Action, Long> {
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

    private fun readActiveAndIdleTimeFromDatabase(): Pair<Long, Long> {
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
                if (deltaTime < idleTimeout) {
                    activeTime += deltaTime
                } else {
                    idleTime += deltaTime
                }
                currentTime = timestamp
            }
            return@query activeTime to idleTime
        }
    }

}
