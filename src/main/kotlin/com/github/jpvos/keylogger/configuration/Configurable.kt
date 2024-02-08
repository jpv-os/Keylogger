package com.github.jpvos.keylogger.configuration

import com.github.jpvos.keylogger.ui.UiContainer
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.ui.components.JBCheckBox
import javax.swing.JComponent

// TODO config implementieen
class Configurable : SearchableConfigurable {

    private var modified = false
    private val enabled = JBCheckBox("Enable Keylogger").apply { // TODO label
        addActionListener {
            modified = true
        }
    }

    private val component by lazy {
        UiContainer().apply {
            add(enabled)
        }
    }

    override fun createComponent(): JComponent {
        return component
    }

    override fun isModified(): Boolean {
        return modified
    }

    override fun apply() {
    }

    override fun getDisplayName(): String {
        return "Keylogger"
    }

    override fun getId(): String {
        return "com.github.jpvos.keylogger.configuration.Configurable" // TODO ???
    }

}
