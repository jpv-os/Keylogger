package com.github.jpvos.keylogger.toolWindow

import com.github.jpvos.keylogger.KeyloggerBundle
import com.github.jpvos.keylogger.toolWindow.components.ActionsComponent
import com.github.jpvos.keylogger.toolWindow.components.StatisticsComponent
import com.github.jpvos.keylogger.ui.UiContainer
import com.github.jpvos.keylogger.ui.UiTabs

class PluginToolWindow : UiContainer() {

    init {
        add(UiTabs().apply {
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
}
