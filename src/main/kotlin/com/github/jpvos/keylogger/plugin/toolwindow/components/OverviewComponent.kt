package com.github.jpvos.keylogger.plugin.toolwindow.components

import com.github.jpvos.keylogger.core.Action
import com.github.jpvos.keylogger.core.Counter
import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.services.CounterService
import com.github.jpvos.keylogger.ui.Container
import com.github.jpvos.keylogger.ui.DisplayFormat
import com.github.jpvos.keylogger.ui.Table
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.service

class OverviewComponent : Container(), Counter.Listener, Disposable {

    private val counterService = service<CounterService>()
    private val table = Table(
        arrayOf(
            KeyloggerBundle.message("overview.table.information"),
            KeyloggerBundle.message("overview.table.value"),
        )
    )

    init {
        add(table)
        updateTableData()
        counterService.counter.registerListener(this)
    }

    override fun onAction(action: Action) {
        updateTableData()
    }

    override fun dispose() {
        counterService.counter.unregisterListener(this)
    }

    private fun updateTableData() {
        val state = counterService.counter.getState()
        table.setTableData(
            arrayOf(
                arrayOf(
                    KeyloggerBundle.message("overview.totalActions"),
                    DisplayFormat.long(state.totalActions),
                ),
                arrayOf(
                    KeyloggerBundle.message("overview.uniqueActions"),
                    DisplayFormat.long(state.uniqueActions)
                ),
                arrayOf(
                    KeyloggerBundle.message("overview.totalTime"),
                    DisplayFormat.duration(state.totalTime)
                ),
                arrayOf(
                    KeyloggerBundle.message("overview.activeTime"),
                    DisplayFormat.duration(state.activeTime)
                ),
                arrayOf(
                    KeyloggerBundle.message("overview.idleTime"),
                    DisplayFormat.duration(state.idleTime)
                ),
                arrayOf(
                    KeyloggerBundle.message("overview.actionsPerMinute"),
                    DisplayFormat.decimal(state.actionsPerMinute)
                )
            )
        )
    }
}
