package com.github.jpvos.keylogger.plugin.configurable

import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.services.SettingsService
import com.github.jpvos.keylogger.plugin.util.Form
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.service
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import javax.swing.JComponent

class KeyloggerConfigurable : SearchableConfigurable, SettingsService.Listener, Disposable {

    /**
     * The fields of the settings form.
     */
    enum class SettingsFormFields {
        DATABASE_URL,
        DATABASE_URL_RELATIVE,
        IDLE_TIMEOUT,
        HISTORY_SIZE,
        IDEA_VIM
    }

    /**
     * @see [SearchableConfigurable.createComponent].
     */
    override fun createComponent(): JComponent = form.layout.build()

    /**
     * @see [SearchableConfigurable.isModified].
     */
    override fun isModified(): Boolean = form.model.modified

    /**
     * @see [SearchableConfigurable.apply].
     */
    override fun apply() = service<SettingsService>().update(
        databaseURL = form.model.get<String>(SettingsFormFields.DATABASE_URL).value,
        databaseURLRelative = form.model.get<Boolean>(SettingsFormFields.DATABASE_URL_RELATIVE).value,
        idleTimeout = form.model.get<Int>(SettingsFormFields.IDLE_TIMEOUT).value,
        historySize = form.model.get<Int>(SettingsFormFields.HISTORY_SIZE).value,
        ideaVim = form.model.get<Boolean>(SettingsFormFields.IDEA_VIM).value
    )

    override fun getDisplayName() = KeyloggerBundle.message("settings.displayName")

    /**
     * @see [SearchableConfigurable.getId].
     */
    override fun getId() = "com.github.jpvos.keylogger.plugin.configurable.KeyloggerConfigurable"

    /**
     * @see [SearchableConfigurable.getPreferredFocusedComponent].
     */
    override fun getPreferredFocusedComponent() = form.model.get<Any?>(SettingsFormFields.DATABASE_URL).component

    /**
     * A label to display the active database URL that is used throughout the form.
     */
    private val labelActiveDatabaseUrl = JBLabel()

    /**
     * The form used to display and edit the settings.
     */
    private val form = Form.create<SettingsFormFields> {
        val settingsService = service<SettingsService>()
        section(KeyloggerBundle.message("settings.form.header.actionDatabase")) {
            textField(
                SettingsFormFields.DATABASE_URL,
                KeyloggerBundle.message("settings.form.databaseURL.label")
            ) { settingsService.databaseURL }
            checkbox(
                SettingsFormFields.DATABASE_URL_RELATIVE,
                KeyloggerBundle.message("settings.form.databaseURLRelative.checkbox")
            ) { settingsService.databaseURLRelative }
            component(labelActiveDatabaseUrl)
            labelGroup {
                default(KeyloggerBundle.message("settings.form.databaseURLRelative.default"))
                note(KeyloggerBundle.message("settings.form.databaseURLRelative.note"))
                hint(KeyloggerBundle.message("settings.form.databaseURLRelative.hint"))
                example(KeyloggerBundle.message("settings.form.databaseURLRelative.example"))
            }
        }
        section(KeyloggerBundle.message("settings.form.header.displaySettings")) {
            numberField(
                SettingsFormFields.IDLE_TIMEOUT,
                KeyloggerBundle.message("settings.form.idleTimeout.label")
            ) { settingsService.idleTimeout.toInt() }
            labelGroup {
                default(KeyloggerBundle.message("settings.form.idleTimeout.default"))
                note(KeyloggerBundle.message("settings.form.idleTimeout.note"))
                hint(KeyloggerBundle.message("settings.form.idleTimeout.hint"))
            }
            gap()
            numberField(
                SettingsFormFields.HISTORY_SIZE,
                KeyloggerBundle.message("settings.form.historySize.label")
            ) { settingsService.historySize.toInt() }
            labelGroup {
                default(KeyloggerBundle.message("settings.form.historySize.default"))
                hint(KeyloggerBundle.message("settings.form.historySize.hint"))
            }
        }
        section(KeyloggerBundle.message("settings.form.header.ideaVim")) {
            checkbox(
                SettingsFormFields.IDEA_VIM,
                KeyloggerBundle.message("settings.form.ideaVim.checkbox")
            ) { settingsService.ideaVim }
            labelGroup {
                default(KeyloggerBundle.message("settings.form.ideaVim.default"))
                note(KeyloggerBundle.message("settings.form.ideaVim.note"))
            }
        }
        section(KeyloggerBundle.message("settings.dangerZone.label"), JBColor.RED) {
            labelGroup {
                warning(KeyloggerBundle.message("settings.dangerZone.warning"))
            }
            component(labelActiveDatabaseUrl)
            gap()
            button(KeyloggerBundle.message("settings.restoreDefaults"), JBColor.RED) {
                settingsService.restoreDefaultSettings()
                reset()
            }
            button(KeyloggerBundle.message("settings.form.ideaVim.button"), JBColor.RED) {
                settingsService.cleanIdeaVimActions()
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

    override fun dispose() {
        service<SettingsService>().unregisterListener(this)
    }

    override fun onSettingsChange() {
        reset()
    }

    override fun reset() {
        form.model.reset()
        labelActiveDatabaseUrl.text =
            "<html>${
                KeyloggerBundle.message(
                    "settings.form.databaseURL.current",
                    service<SettingsService>().activeDatabaseUrl
                )
            }</html>"
    }

}
