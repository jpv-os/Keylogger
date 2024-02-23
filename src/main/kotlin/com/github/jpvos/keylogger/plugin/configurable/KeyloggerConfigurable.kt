package com.github.jpvos.keylogger.plugin.configurable

import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.services.CounterService
import com.github.jpvos.keylogger.plugin.services.DatabaseService
import com.github.jpvos.keylogger.plugin.services.KeyloggerSettings
import com.github.jpvos.keylogger.plugin.configurable.components.KeyloggerSettingsComponent
import com.intellij.openapi.components.service
import com.intellij.openapi.options.SearchableConfigurable
import javax.swing.JComponent

class KeyloggerConfigurable : SearchableConfigurable {

    private val databaseService = service<DatabaseService>()
    private val counterService = service<CounterService>()
    private var settingsComponent: KeyloggerSettingsComponent = KeyloggerSettingsComponent(
        restoreDefaultsCallback = {
            restoreDefaults()
        },
        clearDatabaseCallback = {
            databaseService.clearDatabase()
            counterService.restoreCounter()
        }
    )

    private fun restoreDefaults() {
        settingsComponent.databaseURL = KeyloggerSettings.defaultDatabaseURL
        settingsComponent.idleTimeout = KeyloggerSettings.defaultIdleTimeout
        settingsComponent.historySize = KeyloggerSettings.defaultHistorySize
        settingsComponent.ideaVim = KeyloggerSettings.defaultIdeaVim
        apply()
    }

    override fun createComponent(): JComponent {
        return settingsComponent.panel
    }

    override fun isModified(): Boolean {
        val settings = KeyloggerSettings.instance
        val databaseUrlModified = settingsComponent.databaseURL != settings.databaseURL
        val idleTimeoutModified = settingsComponent.idleTimeout != settings.idleTimeout
        val historySizeModified = settingsComponent.historySize != settings.historySize
        val ideaVimModified = settingsComponent.ideaVim != settings.ideaVim
        return databaseUrlModified or idleTimeoutModified or historySizeModified or ideaVimModified
    }

    override fun apply() {
        val settings = KeyloggerSettings.instance
        settings.databaseURL = settingsComponent.databaseURL ?: ""
        settings.idleTimeout = settingsComponent.idleTimeout
        settings.historySize = settingsComponent.historySize
        settings.ideaVim = settingsComponent.ideaVim
        databaseService.restoreDatabase()
        counterService.restoreCounter()
    }

    override fun reset() {
        val settings = KeyloggerSettings.instance
        settingsComponent.databaseURL = settings.databaseURL
        settingsComponent.idleTimeout = settings.idleTimeout
        settingsComponent.historySize = settings.historySize
        settingsComponent.ideaVim = settings.ideaVim
    }

    override fun getDisplayName(): String {
        return KeyloggerBundle.message("settings.displayName")
    }

    override fun getId(): String {
        return "com.github.jpvos.keylogger.plugin.settings.KeyloggerConfigurable"
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return settingsComponent.preferredFocusedComponent
    }

}
