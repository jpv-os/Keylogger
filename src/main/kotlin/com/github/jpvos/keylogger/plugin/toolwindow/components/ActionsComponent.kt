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

class ActionsComponent : Container(), Counter.Listener, Disposable {

    private val counterService = service<CounterService>()
    private val table = Table(
        arrayOf(
            KeyloggerBundle.message("actions.table.type"),
            KeyloggerBundle.message("actions.table.name"),
            KeyloggerBundle.message("actions.table.count"),
            KeyloggerBundle.message("actions.table.share"),
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
            state.actions
                .map { it.toPair() }
                .sortedByDescending { it.second }
                .map {
                    arrayOf(
                        DisplayFormat.text(it.first.type.toString()),
                        DisplayFormat.text(it.first.name),
                        DisplayFormat.long(it.second),
                        DisplayFormat.percent01(it.second.toDouble() / state.totalActions.toDouble())
                    )
                }
                .toTypedArray()
        )
    }
}
