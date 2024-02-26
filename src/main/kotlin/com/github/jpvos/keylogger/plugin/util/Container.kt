package com.github.jpvos.keylogger.plugin.util

import com.intellij.ui.components.JBPanel
import javax.swing.BoxLayout

open class Container(vertical: Boolean = true) : JBPanel<JBPanel<*>>() {
    init {
        apply {
            layout = BoxLayout(this, if (vertical) BoxLayout.Y_AXIS else BoxLayout.X_AXIS).apply {
                alignmentX = LEFT_ALIGNMENT
            }
        }
    }
}
