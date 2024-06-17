package com.github.jpvos.keylogger.plugin.configurable

import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.services.CounterService
import com.github.jpvos.keylogger.plugin.services.SettingsService
import com.github.jpvos.keylogger.plugin.util.forms.Form
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.service
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.ui.JBColor
import javax.swing.JComponent

/**
 * The configurable for the Keylogger plugin.
 */
internal class KeyloggerConfigurable : SearchableConfigurable, SettingsService.Listener, Disposable {

    /**
     * The fields of the settings form.
     */
    enum class SettingsFormField {
        DATABASE_URL,
        DATABASE_URL_RELATIVE,
        IDLE_TIMEOUT,
        HISTORY_SIZE,
        IDEA_VIM
    }

    /**
     * The form used to display and edit the settings.
     */
    private val form = Form.create<SettingsFormField> {
        val settingsService = service<SettingsService>()
        val counterService = service<CounterService>()
        section(KeyloggerBundle.message("settings.form.header.actionDatabase")) {
            pathField(
                SettingsFormField.DATABASE_URL,
                KeyloggerBundle.message("settings.form.databaseURL.label")
            ) {
                settingsService.state.databaseURL
            }
            checkbox(
                SettingsFormField.DATABASE_URL_RELATIVE,
                KeyloggerBundle.message("settings.form.databaseURLRelative.checkbox")
            ) {
                settingsService.state.databaseURLRelative
            }
            labelGroup {
                default(KeyloggerBundle.message("settings.form.databaseURLRelative.default"))
                note(KeyloggerBundle.message("settings.form.databaseURLRelative.note"))
                hint(KeyloggerBundle.message("settings.form.databaseURLRelative.hint"))
                example(KeyloggerBundle.message("settings.form.databaseURLRelative.example"))
            }
            gap()
            numberField(
                SettingsFormField.IDLE_TIMEOUT,
                KeyloggerBundle.message("settings.form.idleTimeout.label")
            ) {
                settingsService.state.idleTimeout
            }
            labelGroup {
                default(KeyloggerBundle.message("settings.form.idleTimeout.default"))
                note(KeyloggerBundle.message("settings.form.idleTimeout.note"))
                hint(KeyloggerBundle.message("settings.form.idleTimeout.hint"))
            }
        }
        section(KeyloggerBundle.message("settings.form.header.toolWindow")) {
            numberField(
                SettingsFormField.HISTORY_SIZE,
                KeyloggerBundle.message("settings.form.historySize.label")
            ) {
                settingsService.state.historySize
            }
            labelGroup {
                default(KeyloggerBundle.message("settings.form.historySize.default"))
                hint(KeyloggerBundle.message("settings.form.historySize.hint"))
            }
        }
        section(KeyloggerBundle.message("settings.form.header.ideaVim")) {
            checkbox(
                SettingsFormField.IDEA_VIM,
                KeyloggerBundle.message("settings.form.ideaVim.checkbox")
            ) {
                settingsService.state.ideaVim
            }
            labelGroup {
                default(KeyloggerBundle.message("settings.form.ideaVim.default"))
                note(KeyloggerBundle.message("settings.form.ideaVim.note"))
            }
        }
        section(KeyloggerBundle.message("settings.dangerZone.label"), JBColor.RED) {
            labelGroup {
                warning(KeyloggerBundle.message("settings.dangerZone.warning"))
            }
            gap()
            button(KeyloggerBundle.message("settings.restoreDefaults"), JBColor.RED) {
                settingsService.restoreDefaultSettings()
                reset()
            }
            button(KeyloggerBundle.message("settings.form.ideaVim.button"), JBColor.RED) {
                counterService.restoreCounter()
            }
            button(KeyloggerBundle.message("settings.clearDatabase"), JBColor.RED) {
                settingsService.clearDatabase()
            }
        }
        gap()
    }

    init {
        reset()
        service<SettingsService>().registerListener(this)
    }

    /**
     * @see [SearchableConfigurable.apply].
     */
    override fun apply() {
        val formState = SettingsService.State(
            databaseURL = form.model.get<String>(SettingsFormField.DATABASE_URL).value,
            databaseURLRelative = form.model.get<Boolean>(SettingsFormField.DATABASE_URL_RELATIVE).value,
            idleTimeout = form.model.get<Int>(SettingsFormField.IDLE_TIMEOUT).value,
            historySize = form.model.get<Int>(SettingsFormField.HISTORY_SIZE).value,
            ideaVim = form.model.get<Boolean>(SettingsFormField.IDEA_VIM).value,
        )
        service<SettingsService>().update(formState)
    }

    /**
     * @see [SearchableConfigurable.createComponent].
     */
    override fun createComponent(): JComponent = form.layout.getPanel()

    /**
     * @see [SearchableConfigurable.isModified].
     */
    override fun isModified(): Boolean = form.model.modified

    /**
     * @see [SearchableConfigurable.getDisplayName].
     */
    override fun getDisplayName() = KeyloggerBundle.message("settings.displayName")

    /**
     * @see [SearchableConfigurable.getId].
     */
    override fun getId() = "com.github.jpvos.keylogger.plugin.configurable.KeyloggerConfigurable"

    /**
     * @see [SearchableConfigurable.getPreferredFocusedComponent].
     */
    override fun getPreferredFocusedComponent() = form.model.getComponent(SettingsFormField.DATABASE_URL)

    /**
     * @see [Disposable.dispose].
     */
    override fun dispose() {
        service<SettingsService>().unregisterListener(this)
    }

    /**
     * @see [SettingsService.Listener.onSettingsChange].
     */
    override fun onSettingsChange() {
        reset()
    }

    /**
     * @see [SearchableConfigurable.reset].
     */
    override fun reset() {
        form.model.reset()
    }

}
