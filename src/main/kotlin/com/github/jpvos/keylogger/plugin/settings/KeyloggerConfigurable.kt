package com.github.jpvos.keylogger.plugin.settings

import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.services.CounterService
import com.github.jpvos.keylogger.plugin.services.DatabaseService
import com.github.jpvos.keylogger.plugin.settings.components.KeyloggerSettingsComponent
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
        apply()
    }

    override fun createComponent(): JComponent {
        return settingsComponent.panel
    }

    override fun isModified(): Boolean {
        val settings = KeyloggerSettings.instance
        val databaseUrlModified = settingsComponent.databaseURL != settings.databaseURL
        val idleTimeoutModified = settingsComponent.idleTimeout != settings.idleTimeout
        return databaseUrlModified or idleTimeoutModified
    }

    override fun apply() {
        val settings = KeyloggerSettings.instance
        settings.databaseURL = settingsComponent.databaseURL ?: ""
        settings.idleTimeout = settingsComponent.idleTimeout
        databaseService.restoreDatabase()
        counterService.restoreCounter()
    }

    override fun reset() {
        val settings = KeyloggerSettings.instance
        settingsComponent.databaseURL = settings.databaseURL
        settingsComponent.idleTimeout = settings.idleTimeout
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