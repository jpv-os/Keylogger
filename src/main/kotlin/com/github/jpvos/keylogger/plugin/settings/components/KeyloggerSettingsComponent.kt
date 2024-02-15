package com.github.jpvos.keylogger.plugin.settings.components

import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.intellij.ide.ui.UINumericRange
import com.intellij.ui.JBIntSpinner
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel


class KeyloggerSettingsComponent {
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
        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(
                JBLabel(KeyloggerBundle.message("settings.form.databaseURL.label")),
                inputDatabaseURL,
                1,
                true
            )
            .addVerticalGap(8)
            .addLabeledComponent(
                JBLabel(KeyloggerBundle.message("settings.form.idleTimeout.label")),
                inputIdleTimeout,
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
