package com.github.jpvos.keylogger.plugin.settings.components

import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.ui.Container
import com.intellij.ide.ui.UINumericRange
import com.intellij.ui.JBIntSpinner
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel


class KeyloggerSettingsComponent(
    restoreDefaultsCallback: () -> Unit,
    clearDatabaseCallback: () -> Unit
) {
    val panel: JPanel
    private val inputDatabaseURL = JBTextField()
    private val inputIdleTimeout = JBIntSpinner(
        UINumericRange(
            KeyloggerBundle.message("settings.form.idleTimeout.defaultValue").toInt(),
            0,
            KeyloggerBundle.message("settings.form.idleTimeout.maxValue").toInt(),
        )
    )
    private val inputHistorySize = JBIntSpinner(
        UINumericRange(
            KeyloggerBundle.message("settings.form.historySize.defaultValue").toInt(),
            0,
            KeyloggerBundle.message("settings.form.historySize.maxValue").toInt(),
        )
    )
    private val inputIdeaVim = JBCheckBox(
        KeyloggerBundle.message("settings.form.ideaVim.label")
    )

    private val checkboxEnableDangerZone = JBCheckBox(KeyloggerBundle.message("settings.dangerZone.checkbox"))
    private val buttonRestoreDefaultSettings = JButton(KeyloggerBundle.message("settings.restoreDefaults")).apply {
        isEnabled = false
        checkboxEnableDangerZone.addChangeListener {
            isEnabled = checkboxEnableDangerZone.isSelected
        }
        addActionListener {
            restoreDefaultsCallback()
        }
    }
    private val buttonClearDatabase = JButton(KeyloggerBundle.message("settings.clearDatabase")).apply {
        isEnabled = false
        checkboxEnableDangerZone.addChangeListener {
            isEnabled = checkboxEnableDangerZone.isSelected
        }
        addActionListener {
            clearDatabaseCallback()
        }
    }


    init {
        val topInset = 1
        val gapSize = 8
        val labelOnTop = false
        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(
                JBLabel(KeyloggerBundle.message("settings.form.databaseURL.label")),
                inputDatabaseURL,
                topInset,
                labelOnTop
            )
            .addVerticalGap(gapSize)
            .addLabeledComponent(
                JBLabel(KeyloggerBundle.message("settings.form.idleTimeout.label")),
                inputIdleTimeout,
                topInset,
                labelOnTop
            )
            .addVerticalGap(gapSize)
            .addLabeledComponent(
                JBLabel(KeyloggerBundle.message("settings.form.historySize.label")),
                inputHistorySize,
                topInset,
                labelOnTop
            )
            .addVerticalGap(gapSize)
            .addComponent(
                inputIdeaVim,
                topInset,
            )
            .addVerticalGap(gapSize)
            .addComponentFillVertically(JPanel(), topInset)
            .addComponent(JBLabel(KeyloggerBundle.message("settings.dangerZone.label")).apply {
                font = font.let { it.deriveFont(it.style or java.awt.Font.BOLD) }
            })
            .addVerticalGap(1)
            .addSeparator(topInset)
            .addVerticalGap(gapSize)
            .addComponent(JBLabel(KeyloggerBundle.message("settings.dangerZone.warning")), topInset)
            .addVerticalGap(gapSize)
            .addComponent(checkboxEnableDangerZone, topInset)
            .addVerticalGap(gapSize)
            .addComponent(buttonRestoreDefaultSettings, topInset)
            .addComponent(buttonClearDatabase, topInset)
            .panel
    }

    val preferredFocusedComponent: JComponent
        get() = inputDatabaseURL

    var databaseURL: String?
        get() = inputDatabaseURL.text
        set(value) {
            inputDatabaseURL.text = value
        }

    var idleTimeout: Long
        get() = inputIdleTimeout.number.toLong()
        set(value) {
            inputIdleTimeout.number = value.toInt()
        }

    var historySize: Long
        get() = inputHistorySize.number.toLong()
        set(value) {
            inputHistorySize.number = value.toInt()
        }

    var ideaVim: Boolean
        get() = inputIdeaVim.isSelected
        set(value) {
            inputIdeaVim.isSelected = value
        }
}
