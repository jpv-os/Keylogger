package com.github.jpvos.keylogger.plugin.toolwindow.fragments

import com.github.jpvos.keylogger.core.Action
import com.github.jpvos.keylogger.core.Counter
import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.services.KeyloggerService
import com.github.jpvos.keylogger.ui.Container
import com.github.jpvos.keylogger.ui.DisplayFormat
import com.github.jpvos.keylogger.ui.Table
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.service

class StatisticsTable : Container(), Counter.Listener, Disposable {

    private val keyloggerService = service<KeyloggerService>()
    private val table = Table(
        arrayOf(
            KeyloggerBundle.message("statistics.table.information"),
            KeyloggerBundle.message("statistics.table.value"),
        )
    )

    init {
        add(table)
        updateTableData()
        keyloggerService.counter.registerListener(this)
    }

    override fun onAction(action: Action) {
        updateTableData()
    }

    override fun dispose() {
        keyloggerService.counter.unregisterListener(this)
    }

    private fun updateTableData() {
        val state = keyloggerService.counter.getState()
        table.setTableData(
            arrayOf(
                arrayOf(
                    KeyloggerBundle.message("statistics.totalActions"),
                    DisplayFormat.long(state.totalActions),
                ),
                arrayOf(
                    KeyloggerBundle.message("statistics.uniqueActions"),
                    DisplayFormat.long(state.uniqueActions)
                ),
                arrayOf(
                    KeyloggerBundle.message("statistics.totalTime"),
                    DisplayFormat.milliseconds(state.totalTime)
                ),
                arrayOf(
                    KeyloggerBundle.message("statistics.activeTime"),
                    "${DisplayFormat.milliseconds(state.activeTime)} (${DisplayFormat.percent01(state.activeTime / state.totalTime.toDouble())})"
                ),
                arrayOf(
                    KeyloggerBundle.message("statistics.idleTime"),
                    "${DisplayFormat.milliseconds(state.idleTime)} (${DisplayFormat.percent01(state.idleTime / state.totalTime.toDouble())})"
                ),
                arrayOf(
                    KeyloggerBundle.message("statistics.actionsPerMinute"),
                    DisplayFormat.decimal(state.actionsPerMinute)
                )
            )
        )
    }
}
