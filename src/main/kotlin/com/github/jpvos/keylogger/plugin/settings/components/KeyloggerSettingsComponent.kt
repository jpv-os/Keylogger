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

    init {
        val gapSize = 8
        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(
                JBLabel(KeyloggerBundle.message("settings.form.databaseURL.label")),
                inputDatabaseURL,
                1,
                true
            )
            .addVerticalGap(gapSize)
            .addLabeledComponent(
                JBLabel(KeyloggerBundle.message("settings.form.idleTimeout.label")),
                inputIdleTimeout,
                1,
                true
            )
            .addVerticalGap(gapSize)
            .addLabeledComponent(
                JBLabel(KeyloggerBundle.message("settings.dangerZone")),
                Container().apply {
                    val restoreButton = JButton(KeyloggerBundle.message("settings.restoreDefaults")).apply {
                        isEnabled = false
                        addActionListener {
                            restoreDefaultsCallback()
                        }
                    }
                    val clearButton = JButton(KeyloggerBundle.message("settings.clearDatabase")).apply {
                        isEnabled = false
                        addActionListener {
                            clearDatabaseCallback()
                        }
                    }
                    val checkbox = JBCheckBox(KeyloggerBundle.message("settings.enableDangerZone")).apply {
                        addChangeListener {
                            restoreButton.isEnabled = isSelected
                            clearButton.isEnabled = isSelected
                        }
                    }
                    add(checkbox)
                    add(Container().apply {
                        add(restoreButton)
                        add(clearButton)
                    })
                },
                1,
                true
            )
            .addComponentFillVertically(JPanel(), 0)
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
}
