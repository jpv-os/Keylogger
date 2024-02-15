package com.github.jpvos.keylogger.plugin.toolwindow

import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.toolwindow.components.ActionsComponent
import com.github.jpvos.keylogger.plugin.toolwindow.components.StatisticsComponent
import com.github.jpvos.keylogger.ui.Container
import com.github.jpvos.keylogger.ui.Tabs
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory


class KeyloggerToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val keyloggerToolWindow = Container().apply {
            add(Tabs().apply {
                addTab(
                    KeyloggerBundle.message("toolwindow.statistics"),
                    StatisticsComponent()
                )
                addTab(
                    KeyloggerBundle.message("toolwindow.actions"),
                    ActionsComponent()
                )
            })
        }
        val content = ContentFactory.getInstance().createContent(keyloggerToolWindow, null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

}

