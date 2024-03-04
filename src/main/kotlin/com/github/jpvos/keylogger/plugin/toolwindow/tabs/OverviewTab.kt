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

internal class OverviewTab : Container(), Counter.Listener, Disposable {

    private val table = Table(
        arrayOf(
            KeyloggerBundle.message("overview.table.information"),
            KeyloggerBundle.message("overview.table.value"),
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
            arrayOf(
                arrayOf(
                    TableCell.Label(KeyloggerBundle.message("overview.totalActions")),
                    TableCell.Long(state.totalActions),
                ),
                arrayOf(
                    TableCell.Label(KeyloggerBundle.message("overview.uniqueActions")),
                    TableCell.Long(state.uniqueActions)
                ),
                arrayOf(
                    TableCell.Label(KeyloggerBundle.message("overview.totalTime")),
                    TableCell.Duration(state.totalTime)
                ),
                arrayOf(
                    TableCell.Label(KeyloggerBundle.message("overview.activeTime")),
                    TableCell.Duration(state.activeTime)
                ),
                arrayOf(
                    TableCell.Label(KeyloggerBundle.message("overview.idleTime")),
                    TableCell.Duration(state.idleTime)
                ),
                arrayOf(
                    TableCell.Label(KeyloggerBundle.message("overview.actionsPerMinute")),
                    TableCell.Decimal(state.actionsPerMinute)
                )
            )
        )
    }
}
