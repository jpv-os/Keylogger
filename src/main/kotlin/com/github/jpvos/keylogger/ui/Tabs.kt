package com.github.jpvos.keylogger.ui

import com.intellij.ui.components.JBTabbedPane
import java.awt.event.KeyEvent
import javax.swing.JComponent

class Tabs : Container() {

    private val tabbedPane = JBTabbedPane()
    private var size = 0

    init {
        add(tabbedPane)
    }

    fun addTab(title: String, content: JComponent) {
        tabbedPane.addTab(title, null, content, null)
        tabbedPane.setMnemonicAt(size, KeyEvent.VK_1 + size) // TODO: doesnt seem to work
        size++
    }
}
