package com.github.jpvos.keylogger.plugin.toolwindow

import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.toolwindow.components.ActionsComponent
import com.github.jpvos.keylogger.plugin.toolwindow.components.StatisticsComponent
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory


class KeyloggerToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val factory = ContentFactory.getInstance()
        val statisticsTab = factory.createContent(
            StatisticsComponent(),
            KeyloggerBundle.message("toolwindow.statistics"),
            false
        )
        val actionsTab = factory.createContent(
            ActionsComponent(),
            KeyloggerBundle.message("toolwindow.actions"),
            false
        )
        toolWindow.contentManager.addContent(statisticsTab)
        toolWindow.contentManager.addContent(actionsTab)
    }

    override fun shouldBeAvailable(project: Project) = true

}

