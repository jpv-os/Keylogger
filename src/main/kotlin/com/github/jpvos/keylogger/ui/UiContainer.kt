package com.github.jpvos.keylogger.ui

import com.intellij.ui.components.JBPanel
import javax.swing.BoxLayout

open class UiContainer : JBPanel<JBPanel<*>>() {
    init {
        apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS).apply {
                alignmentX = LEFT_ALIGNMENT
            }
        }
    }
}
