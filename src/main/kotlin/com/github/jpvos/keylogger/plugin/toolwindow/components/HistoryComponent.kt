package com.github.jpvos.keylogger.plugin.toolwindow.components

import com.github.jpvos.keylogger.core.Action
import com.github.jpvos.keylogger.core.Counter
import com.github.jpvos.keylogger.core.DisplayFormat
import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.services.CounterService
import com.github.jpvos.keylogger.plugin.services.DatabaseService
import com.github.jpvos.keylogger.plugin.services.SettingsService
import com.github.jpvos.keylogger.plugin.util.Container
import com.github.jpvos.keylogger.plugin.util.Table
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.service

class HistoryComponent : Container(), Counter.Listener, Disposable {
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
        // TODO: query history only once and then keep updating, add a restore function
        val history = service<DatabaseService>().queryActionsHistory(service<SettingsService>().historySize.toLong())
        table.setTableData(
            history
                .map { (action, timestamp) ->
                    arrayOf(
                        action.type.toString(),
                        action.name,
                        DisplayFormat.date(timestamp),
                    )
                }
                .toTypedArray()
        )
    }
}
