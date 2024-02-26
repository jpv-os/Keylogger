package com.github.jpvos.keylogger.plugin.services

import com.intellij.notification.NotificationGroupManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil
import java.nio.file.Path
import javax.xml.crypto.Data
import kotlin.io.path.Path


/**
 * Service to read and manage the settings of the plugin.
 * This is a persistent service, meaning that the settings are stored in a file.
 *
 * Note: this is the only place to interact with the users settings, so more complex logic such as
 * resetting the database or restoring defaults is also handled here.
 */
@State(
    name = "com.github.jpvos.keylogger.plugin.services.SettingsService", storages = [Storage("KeyloggerSettings.xml")]
)
class SettingsService : PersistentStateComponent<SettingsService.State> {
    /**
     * Internal representation of the currently active settings.
     */
    private val state = State()

    /**
     * List of listeners to notify when the settings change.
     */
    private val listeners = mutableListOf<Listener>()

    /**
     * Plain data class to store the settings with default values.
     */
    data class State(
        /**
         * @see [SettingsService.databaseURL].
         */
        var databaseURL: String = DEFAULT_DATABASE_URL,
        /**
         * @see [SettingsService.databaseURLRelative].
         */
        var databaseURLRelative: Boolean = DEFAULT_DATABASE_URL_RELATIVE,
        /**
         * @see [SettingsService.idleTimeout].
         */
        var idleTimeout: Int = DEFAULT_IDLE_TIMEOUT,
        /**
         * @see [SettingsService.historySize].
         */
        var historySize: Int = DEFAULT_HISTORY_SIZE,
        /**
         * @see [SettingsService.ideaVim].
         */
        var ideaVim: Boolean = DEFAULT_IDEA_VIM,
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
        /**
         * @see [SettingsService.databaseURL].
         */
        val DEFAULT_DATABASE_URL = Path(".keylogger-plugin", "db.sqlite").toString()

        /**
         * @see [SettingsService.databaseURLRelative].
         */
        const val DEFAULT_DATABASE_URL_RELATIVE = true

        /**
         * @see [SettingsService.idleTimeout].
         */
        const val DEFAULT_IDLE_TIMEOUT = 1000 // 1 second

        /**
         * @see [SettingsService.historySize].
         */
        const val DEFAULT_HISTORY_SIZE = 25

        /**
         * @see [SettingsService.ideaVim].
         */
        const val DEFAULT_IDEA_VIM = false // TODO: is it possible to detect the presence of another plugin?
    }

    /**
     * The URL of the database file.
     */
    val databaseURL: String
        get() = state.databaseURL

    /**
     * Whether the database URL is relative to the user's home directory.
     */
    val databaseURLRelative: Boolean
        get() = state.databaseURLRelative

    /**
     * The full URL of the database file, after resolving the path using the [databaseURL] and [databaseURLRelative] properties.
     */
    val activeDatabaseUrl: Path
        get() {
            val segments = databaseURL.split("/").toTypedArray()
            val path = if (databaseURLRelative) {
                Path(System.getProperty("user.home"), *segments)
            } else {
                Path(segments.first(), *segments.slice(1 until segments.size).toTypedArray())
            }
            return path
        }

    /**
     * The time in milliseconds after which the user is considered idle.
     */
    val idleTimeout: Int
        get() = state.idleTimeout

    /**
     * The maximum number of actions to display in the history tool window.
     * Note: this is not the maximum number of actions to store in the database.
     */
    val historySize: Int
        get() = state.historySize

    /**
     * IdeaVIM compatibility mode.
     * Note: by enabling, ignore "Shortcuts" custom action of the IdeaVIM plugin,
     * which produces duplicate actions in some cases.
     */
    val ideaVim: Boolean
        get() = state.ideaVim

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
    fun update(
        databaseURL: String? = null,
        databaseURLRelative: Boolean? = null,
        idleTimeout: Int? = null,
        historySize: Int? = null,
        ideaVim: Boolean? = null
    ) {
        databaseURL?.let { state.databaseURL = it }
        databaseURLRelative?.let { state.databaseURLRelative = it }
        idleTimeout?.let { state.idleTimeout = it }
        historySize?.let { state.historySize = it }
        ideaVim?.let { state.ideaVim = it }
        service<DatabaseService>().restoreDatabase()
        service<CounterService>().restoreCounter()
        notifyChangeListeners()
    }

    /**
     * Restore the settings to their default values using the [SettingsService.update] function.
     */
    fun restoreDefaultSettings() {
        update(
            databaseURL = DEFAULT_DATABASE_URL,
            databaseURLRelative = DEFAULT_DATABASE_URL_RELATIVE,
            idleTimeout = DEFAULT_IDLE_TIMEOUT,
            historySize = DEFAULT_HISTORY_SIZE,
            ideaVim = DEFAULT_IDEA_VIM
        )
//        NotificationGroupManager.getInstance()
//            .getNotificationGroup("Keylogger")
//            .createNotification("Settings restored to default values", "The settings have been restored to their default values.", "INFO")
//            .notify(Project)
    }

    /**
     * Clear the database and restore the counter to its initial state.
     */
    fun clearDatabase() {
        service<DatabaseService>().clearDatabase()
        service<CounterService>().restoreCounter()
        notifyChangeListeners()
    }

    fun cleanIdeaVimActions() {
        service<DatabaseService>().cleanIdeaVimActions()
        notifyChangeListeners()
    }

    /**
     * Notify all listeners that the settings have changed.
     */
    private fun notifyChangeListeners() {
        listeners.forEach { it.onSettingsChange() }
    }
}
