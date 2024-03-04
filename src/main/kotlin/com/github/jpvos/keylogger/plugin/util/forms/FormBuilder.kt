package com.github.jpvos.keylogger.plugin.util.forms

import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import java.awt.Color
import java.awt.Font
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel

class FormBuilder<T : Enum<T>>(private val formModel: FormModel<T>) {
    private val fb = com.intellij.util.ui.FormBuilder()
    private var sections = 0

    companion object {
        private const val SCALING = 16
    }

    class LabelsBuilder(private val fb: com.intellij.util.ui.FormBuilder) {
        fun default(text: String) = keyValue(KeyloggerBundle.message("settings.form.builder.default"), text)
        fun note(text: String) = keyValue(KeyloggerBundle.message("settings.form.builder.note"), text)
        fun hint(text: String) = keyValue(KeyloggerBundle.message("settings.form.builder.hint"), text)
        fun example(text: String) = keyValue(KeyloggerBundle.message("settings.form.builder.example"), text)
        fun warning(text: String) = keyValue(KeyloggerBundle.message("settings.form.builder.warning"), text)
        private fun keyValue(title: String, text: String) {
            fb.addComponent(JBLabel("$title:").apply {
                foreground = JBColor.GRAY
                font = font.deriveFont(font.style or Font.BOLD)
            }, SCALING)
            fb.addComponent(JBLabel(text).apply {
                foreground = JBColor.GRAY
            })
        }
    }

    class SectionBuilder<T : Enum<T>>(
        private val fb: com.intellij.util.ui.FormBuilder,
        private val formModel: FormModel<T>
    ) {

        fun button(text: String, color: Color? = null, action: () -> Unit) = JButton(text).apply {
            addActionListener {
                DialogBuilder().apply {
                    setTitle(KeyloggerBundle.message("settings.dialog.title"))
                    setOkOperation {
                        action()
                        dialogWrapper.close(0)
                    }
                    setCancelOperation {
                        dialogWrapper.close(1)
                    }
                    setCenterPanel(JBLabel(KeyloggerBundle.message("settings.dialog.message")))
                    show()
                }
            }
            if (color != null) {
                foreground = color
            }
            fb.addComponent(this)
        }

        fun labelGroup(body: LabelsBuilder.() -> Unit) = LabelsBuilder(fb).apply(body)

        private fun input(
            input: T,
            label: String? = null,
        ): JComponent {
            val component = formModel.get<Any?>(input).component
            if (label != null) {
                fb.addLabeledComponent(JBLabel(label), component, SCALING, true)
            } else {
                fb.addComponent(component, SCALING)
            }
            return component
        }

        fun textField(field: T, label: String? = null, getDefaultValue: () -> String): JComponent {
            formModel.addText(field, getDefaultValue)
            return input(field, label)
        }

        fun pathField() {
            // TODO https://plugins.jetbrains.com/docs/intellij/file-and-class-choosers.html#via-textfield
        }

        fun numberField(field: T, label: String? = null, getDefaultValue: () -> Int): JComponent {
            formModel.addInteger(field, getDefaultValue)
            return input(field, label)
        }

        fun checkbox(field: T, label: String, getDefaultValue: () -> Boolean): JComponent {
            formModel.addBoolean(field, label, getDefaultValue)
            return input(field)
        }
    }

    fun section(title: String, color: Color? = null, body: SectionBuilder<T>.() -> Unit): JBLabel {
        val titleLabel = JBLabel(title).apply {
            font = font.deriveFont(SCALING * 1.2f)
            if (color != null) {
                foreground = color
            }
        }
        if (sections++ > 0) {
            fb.addSeparator(SCALING)
        }
        fb.addComponent(titleLabel, SCALING)
        SectionBuilder(fb, formModel).apply(body)
        return titleLabel
    }

    fun gap(): Unit = fb.run { addVerticalGap(SCALING) }
    fun getPanel(): JPanel = fb.panel
}

