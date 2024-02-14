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

class ActionsTable : Container(), Counter.Listener, Disposable {

    private val keyloggerService = service<KeyloggerService>()
    private val table = Table(
        arrayOf(
            KeyloggerBundle.message("actions.table.type"),
            KeyloggerBundle.message("actions.table.name"),
            KeyloggerBundle.message("actions.table.count"),
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
            state.actions
                .map { it.toPair() }
                .sortedByDescending { it.second }
                .map { arrayOf(
                    DisplayFormat.text(it.first.type.toString()),
                    DisplayFormat.text(it.first.name),
                    DisplayFormat.long(it.second)
                ) }
                .toTypedArray()
        )
    }
}
