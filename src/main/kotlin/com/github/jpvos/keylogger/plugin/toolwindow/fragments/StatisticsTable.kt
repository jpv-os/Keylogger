package com.github.jpvos.keylogger.plugin.toolwindow.fragments

import com.github.jpvos.keylogger.core.Action
import com.github.jpvos.keylogger.core.Counter
import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.services.KeyloggerService
import com.github.jpvos.keylogger.ui.Container
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
                    state.actions.toList().sumOf { it.second }),
                arrayOf(KeyloggerBundle.message("statistics.uniqueActions"), state.actions.size),
                arrayOf(
                    KeyloggerBundle.message("statistics.totalTime"),
                    state.activeTime + state.idleTime
                ),
                arrayOf(KeyloggerBundle.message("statistics.activeTime"), state.activeTime),
                arrayOf(KeyloggerBundle.message("statistics.idleTime"), state.idleTime),
                arrayOf(
                    KeyloggerBundle.message("statistics.actionsPerMinute"),
                    state.actions.toList().sumOf { it.second }
                        .let { if (it == 0L) 0.0 else state.activeTime / it.toDouble() }),
            )
        )
    }
}
