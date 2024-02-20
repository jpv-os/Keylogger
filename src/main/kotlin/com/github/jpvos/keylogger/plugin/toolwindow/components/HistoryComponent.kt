package com.github.jpvos.keylogger.plugin.toolwindow.components

import com.github.jpvos.keylogger.core.Action
import com.github.jpvos.keylogger.core.Counter
import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.services.CounterService
import com.github.jpvos.keylogger.plugin.services.DatabaseService
import com.github.jpvos.keylogger.plugin.settings.KeyloggerSettings
import com.github.jpvos.keylogger.ui.Container
import com.github.jpvos.keylogger.ui.Table
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.service

class HistoryComponent : Container(), Counter.Listener, Disposable {

    private val counterService = service<CounterService>()
    private val databaseService = service<DatabaseService>()
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
        counterService.counter.registerListener(this)
    }

    override fun onAction(action: Action) {
        updateTableData()
    }

    override fun dispose() {
        counterService.counter.unregisterListener(this)
    }

    private fun updateTableData() {
        val history = databaseService.queryActionsHistory(KeyloggerSettings.instance.historySize)
        table.setTableData(
            history
                .map { (action, timestamp) ->
                    arrayOf(
                        action.type.toString(),
                        action.name,
                        timestamp
                    )
                }
                .toTypedArray()
        )
    }
}
