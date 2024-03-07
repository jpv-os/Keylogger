package com.github.jpvos.keylogger.plugin.toolwindow.tabs

import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.services.DatabaseService
import com.github.jpvos.keylogger.plugin.services.SettingsService
import com.github.jpvos.keylogger.plugin.util.components.Container
import com.github.jpvos.keylogger.plugin.util.components.Table
import com.github.jpvos.keylogger.plugin.util.components.TableCell
import com.github.jpvos.keylogger.plugin.util.components.UpdatablePanel
import com.intellij.openapi.components.service

internal class HistoryTab : UpdatablePanel() {

    private val table = Table(
        arrayOf(
            KeyloggerBundle.message("history.table.type"),
            KeyloggerBundle.message("history.table.name"),
            KeyloggerBundle.message("history.table.timestamp"),
        )
    )

    override val panel = Container().apply {
        add(table)
    }

    override fun update() {
        val db = service<DatabaseService>().connection
        val history = db.queryActionHistory(service<SettingsService>().state.historySize)
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
