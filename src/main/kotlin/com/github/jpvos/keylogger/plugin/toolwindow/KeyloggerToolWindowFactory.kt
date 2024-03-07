package com.github.jpvos.keylogger.plugin.toolwindow

import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.model.Action
import com.github.jpvos.keylogger.plugin.model.Counter
import com.github.jpvos.keylogger.plugin.services.CounterService
import com.github.jpvos.keylogger.plugin.toolwindow.tabs.ActionCounterTab
import com.github.jpvos.keylogger.plugin.toolwindow.tabs.ChartsTab
import com.github.jpvos.keylogger.plugin.toolwindow.tabs.HistoryTab
import com.github.jpvos.keylogger.plugin.toolwindow.tabs.OverviewTab
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.swing.JComponent

@OptIn(FlowPreview::class)
internal class KeyloggerToolWindowFactory : ToolWindowFactory, Counter.Listener, Disposable {

    private val tabUpdateFlow = MutableSharedFlow<Unit>()

    private val overviewTab = OverviewTab()
    private val actionCounterTab = ActionCounterTab()
    private val historyTab = HistoryTab()
    private val chartsTab = ChartsTab()

    init {
        service<CounterService>().counter.registerListener(this)

        CoroutineScope(Dispatchers.Default).apply {
            launch {
                tabUpdateFlow.collect {
                    overviewTab.update()
                    actionCounterTab.update()
                }
            }
            launch {
                tabUpdateFlow.debounce(1000).collect {
                    historyTab.update()
                    chartsTab.update()
                }
            }
        }
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        fun addTab(title: String, component: JComponent?) {
            val tab = ContentFactory.getInstance().createContent(component, title, false)
            toolWindow.contentManager.addContent(tab)
        }
        addTab(KeyloggerBundle.message("toolWindow.overview"), overviewTab.panel)
        addTab(KeyloggerBundle.message("toolWindow.actionCounter"), actionCounterTab.panel)
        addTab(KeyloggerBundle.message("toolWindow.history"), historyTab.panel)
        addTab(KeyloggerBundle.message("toolWindow.charts"), chartsTab.panel)

        startTabUpdate()
    }

    override fun shouldBeAvailable(project: Project) = true

    override fun onAction(action: Action) {
        startTabUpdate()
    }

    override fun dispose() {
        service<CounterService>().counter.unregisterListener(this)
    }

    private fun startTabUpdate() {
        CoroutineScope(Dispatchers.Default).launch {
            tabUpdateFlow.emit(Unit)
        }
    }

}

