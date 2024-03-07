package com.github.jpvos.keylogger.plugin.services

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.util.xmlb.XmlSerializerUtil
import java.nio.file.Path
import kotlin.io.path.Path


/**
 * This service is responsible for managing the settings:
 * - providing the settings to the rest of the application
 * - persisting the settings to the file system
 * - notifying listeners when the settings change
 * - restoring the internal state of the application when the settings change
 */
@State(
    name = "com.github.jpvos.keylogger.plugin.services.SettingsService",
    storages = [Storage("KeyloggerSettings.xml")]
)
internal class SettingsService : PersistentStateComponent<SettingsService.State> {

    // TODO: rename "database url" and "database url relative" to "database path" and "database path relative", but doing so breaks the current users settings

    /**
     * Internal representation of the currently active settings.
     */
    private var state = State()

    /**
     * List of listeners to notify when the settings change.
     */
    private val listeners = mutableListOf<Listener>()

    /**
     * The serializable state of the settings.
     */
    data class State(
        /**
         * The URL of the database file.
         */
        val databaseURL: String = DEFAULT_DATABASE_URL,
        /**
         * Whether the database URL is relative to the user's home directory.
         */
        val databaseURLRelative: Boolean = DEFAULT_DATABASE_URL_RELATIVE,
        /**
         * The time in milliseconds after which the user is considered idle.
         */
        val idleTimeout: Int = DEFAULT_IDLE_TIMEOUT,
        /**
         * The maximum number of actions to display in the history tool window.
         * Note: this is not the maximum number of actions to store in the database.
         */
        val historySize: Int = DEFAULT_HISTORY_SIZE,
        /**
         * IdeaVIM compatibility mode.
         * Note: by enabling, ignore "Shortcuts" custom action of the IdeaVIM plugin,
         * which produces duplicate actions in some cases.
         */
        val ideaVim: Boolean = DEFAULT_IDEA_VIM,
    )

    /**
     * Listener interface to be notified when the settings change.
     */
    interface Listener {
        /**
         * Called when the settings change.
         */
        fun onSettingsChange()
    }

    /**
     * Companion object to store the default values of the settings.
     */
    companion object {
        val DEFAULT_DATABASE_URL = Path(".keylogger-plugin", "db.sqlite").toString()

        const val DEFAULT_DATABASE_URL_RELATIVE = true

        const val DEFAULT_IDLE_TIMEOUT = 1000 // 1 second

        const val DEFAULT_HISTORY_SIZE = 25

        const val DEFAULT_IDEA_VIM = false

        const val IDEA_VIM_ACTION_NAME = "Shortcuts"
    }

    /**
     * @see [PersistentStateComponent.getState].
     */
    override fun getState() = state

    /**
     * @see [PersistentStateComponent.loadState].
     */
    override fun loadState(state: State) {
        XmlSerializerUtil.copyBean(state, this.state)
        notifyChangeListeners()
    }

    /**
     * Register a listener to be notified when the settings change.
     */
    fun registerListener(listener: Listener) {
        listeners.add(listener)
    }

    /**
     * Unregister a listener to stop being notified when the settings change.
     */
    fun unregisterListener(listener: Listener) {
        listeners.remove(listener)
    }

    /**
     * Update the settings with the given values.
     * If a value is not given, it will not be updated.
     * After updating the settings, the internal state of the plugin such as the database and counter are restored.
     */
    fun update(state: State) {
        this.state = state
        service<DatabaseService>().restoreDatabase()
        service<CounterService>().restoreCounter()
        notifyChangeListeners()
    }

    /**
     * Get the path to the database file from the user settings.
     * @see [SettingsService.databaseURL]
     * @see [SettingsService.databaseURLRelative]
     */
    fun getDatabaseFilePath(): Path {
        val segments = state.databaseURL.split("/").toTypedArray()
        val path = if (state.databaseURLRelative) {
            Path(System.getProperty("user.home"), *segments)
        } else {
            Path(segments.first(), *segments.slice(1 until segments.size).toTypedArray())
        }
        return path
    }

    /**
     * Restore the settings to their default values using the [SettingsService.update] function.
     */
    fun restoreDefaultSettings() {
        update(State())
    }

    /**
     * Clear the database and restore the counter to its initial state.
     */
    fun clearDatabase() {
        service<DatabaseService>().connection.deleteAllActions()
        service<CounterService>().restoreCounter()
        notifyChangeListeners()
    }

    /**
     * Clean the database from actions that are created by the IdeaVIM plugin.
     */
    fun cleanIdeaVimActions() {
        service<DatabaseService>().connection.deleteActionsByName(IDEA_VIM_ACTION_NAME)
        notifyChangeListeners()
    }

    /**
     * Notify all listeners that the settings have changed.
     */
    private fun notifyChangeListeners() {
        listeners.forEach { it.onSettingsChange() }
    }
}
