package com.github.jpvos.keylogger.plugin.settings

import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import kotlin.io.path.Path


@State(
    name = "com.github.jpvos.keylogger.plugin.settings.KeyloggerSettings",
    storages = [Storage("Keylogger.xml")]
)
internal class KeyloggerSettings : PersistentStateComponent<KeyloggerSettings> {
    companion object {
        val instance: KeyloggerSettings
            get() = ApplicationManager.getApplication().getService(KeyloggerSettings::class.java)
        val defaultDatabaseURL = Path(
            System.getProperty("user.home"),
            *KeyloggerBundle.message("settings.form.databaseURL.defaultValue").split("/").toTypedArray()
        ).toString()
        val defaultIdleTimeout = KeyloggerBundle.message("settings.form.idleTimeout.defaultValue").toLong()
        val defaultHistorySize = KeyloggerBundle.message("settings.form.historySize.defaultValue").toLong()
        val defaultIdeaVim = KeyloggerBundle.message("settings.form.ideaVim.defaultValue").toBoolean()
    }

    /**
     * The URL of the database file.
     */
    var databaseURL = defaultDatabaseURL

    /**
     * The time in milliseconds after which the user is considered idle.
     */
    var idleTimeout = defaultIdleTimeout

    /**
     * The maximum number of actions to display in the history tab.
     * Note that this is not the maximum number of actions to store in the database.
     */
    var historySize = defaultHistorySize

    /**
     * Compatibility mode for the IdeaVim plugin.
     * When using IdeaVim in insert mode, the backspace key triggers both the "Backspace" and "Shortcuts" actions.
     * To avoid counting the same action twice, we ignore the "Shortcuts" action.
     */
    var ideaVim = defaultIdeaVim

    override fun getState(): KeyloggerSettings {
        return this
    }

    override fun loadState(state: KeyloggerSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

}
