package com.github.jpvos.keylogger.plugin.toolwindow

import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.toolwindow.components.ActionCounterComponent
import com.github.jpvos.keylogger.plugin.toolwindow.components.HistoryComponent
import com.github.jpvos.keylogger.plugin.toolwindow.components.OverviewComponent
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import javax.swing.JComponent

class KeyloggerToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        fun addTab(title: String, component: JComponent?) {
            val tab = ContentFactory.getInstance().createContent(component, title, false)
            toolWindow.contentManager.addContent(tab)
        }
        addTab(KeyloggerBundle.message("toolWindow.overview"), OverviewComponent())
        addTab(KeyloggerBundle.message("toolWindow.actionCounter"), ActionCounterComponent())
        addTab(KeyloggerBundle.message("toolWindow.history"), HistoryComponent())
    }

    override fun shouldBeAvailable(project: Project) = true

}

