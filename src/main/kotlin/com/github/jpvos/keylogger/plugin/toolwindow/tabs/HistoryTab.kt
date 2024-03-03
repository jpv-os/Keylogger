package com.github.jpvos.keylogger.plugin.toolwindow.tabs

import com.github.jpvos.keylogger.plugin.model.Action
import com.github.jpvos.keylogger.plugin.model.Counter
import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.services.CounterService
import com.github.jpvos.keylogger.plugin.services.DatabaseService
import com.github.jpvos.keylogger.plugin.services.SettingsService
import com.github.jpvos.keylogger.plugin.util.components.Container
import com.github.jpvos.keylogger.plugin.util.components.Table
import com.github.jpvos.keylogger.plugin.util.components.TableCell
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.service

class HistoryTab : Container(), Counter.Listener, Disposable {
    private val table = Table(
        arrayOf(
            KeyloggerBundle.message("history.table.type"),
            KeyloggerBundle.message("history.table.name"),
            KeyloggerBundle.message("history.table.timestamp"),
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
        val history =
            service<DatabaseService>().connection.queryActionHistory(service<SettingsService>().state.historySize.toLong())
        table.setTableData(
            history
                .map { (action, timestamp) ->
                    arrayOf(
                        TableCell.Label(action.type.toString()),
                        TableCell.Label(action.name),
                        TableCell.Date(timestamp),
                    )
                }
                .toTypedArray()
        )
    }
}
