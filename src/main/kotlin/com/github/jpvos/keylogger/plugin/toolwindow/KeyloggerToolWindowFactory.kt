package com.github.jpvos.keylogger.plugin.toolwindow

import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.model.Action
import com.github.jpvos.keylogger.plugin.model.Counter
import com.github.jpvos.keylogger.plugin.services.CounterService
import com.github.jpvos.keylogger.plugin.services.SettingsService
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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import javax.swing.JComponent

@OptIn(FlowPreview::class)
internal class KeyloggerToolWindowFactory : ToolWindowFactory, Counter.Listener, SettingsService.Listener, Disposable {

    private val scope = CoroutineScope(Dispatchers.Default)
    private val tabUpdateTrigger = MutableSharedFlow<Unit>()
    private var tabUpdateJob: Job

    private val overviewTab = OverviewTab()
    private val actionCounterTab = ActionCounterTab()
    private val historyTab = HistoryTab()
    private val chartsTab = ChartsTab()


    init {
        service<CounterService>().counter.registerListener(this)
        service<SettingsService>().registerListener(this)
        tabUpdateJob = createTabUpdateJob()
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

        triggerNextUpdate()
    }

    override fun shouldBeAvailable(project: Project) = true

    override fun onAction(action: Action) {
        triggerNextUpdate()
    }

    override fun onSettingsChange() {
        if (tabUpdateJob.isActive) {
            tabUpdateJob.cancel()
        }
        tabUpdateJob = createTabUpdateJob()
    }

    override fun dispose() {
        if (tabUpdateJob.isActive) {
            tabUpdateJob.cancel()
        }
        service<CounterService>().counter.unregisterListener(this)
        service<SettingsService>().unregisterListener(this)
    }

    private fun triggerNextUpdate() {
        scope.launch {
            tabUpdateTrigger.emit(Unit)
        }
    }

    private fun createTabUpdateJob() = scope.launch {
        tabUpdateTrigger
            .debounce(service<SettingsService>().state.updateInterval.toLong())
            .collect {
                overviewTab.update()
                actionCounterTab.update()
                historyTab.update()
                chartsTab.update()
            }
    }

}

