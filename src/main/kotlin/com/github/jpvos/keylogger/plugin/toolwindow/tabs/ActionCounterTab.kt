package com.github.jpvos.keylogger.plugin.toolwindow.tabs

import com.github.jpvos.keylogger.plugin.model.Action
import com.github.jpvos.keylogger.plugin.model.Counter
import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.services.CounterService
import com.github.jpvos.keylogger.plugin.util.components.Container
import com.github.jpvos.keylogger.plugin.util.components.Table
import com.github.jpvos.keylogger.plugin.util.components.TableCell
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.service

class ActionCounterTab : Container(), Counter.Listener, Disposable {

    private val table = Table(
        arrayOf(
            KeyloggerBundle.message("actionCounter.table.type"),
            KeyloggerBundle.message("actionCounter.table.name"),
            KeyloggerBundle.message("actionCounter.table.count"),
            KeyloggerBundle.message("actionCounter.table.share"),
        )
    )

    init {
        add(table)
        updateTableData()
        service<CounterService>().counter.registerListener(this)
    }

    override fun onAction(action: Action) {
        updateTableData()
    }

    override fun dispose() {
        service<CounterService>().counter.unregisterListener(this)
    }

    private fun updateTableData() {
        val state = service<CounterService>().counter.getState()
        table.setTableData(
            state.actions
                .map { it.toPair() }
                .sortedByDescending { it.second }
                .map {
                    arrayOf(
                        TableCell.Label(it.first.type.toString()),
                        TableCell.Label(it.first.name),
                        TableCell.Long(it.second),
                        TableCell.Percent(it.second.toDouble() / state.totalActions.toDouble() * 100.0)
                    )
                }
                .toTypedArray()
        )
    }
}
