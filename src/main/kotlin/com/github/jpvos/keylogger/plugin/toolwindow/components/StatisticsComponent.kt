package com.github.jpvos.keylogger.plugin.toolwindow.components

import com.github.jpvos.keylogger.core.Action
import com.github.jpvos.keylogger.core.Counter
import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.services.CounterService
import com.github.jpvos.keylogger.plugin.settings.KeyloggerSettings
import com.github.jpvos.keylogger.ui.Container
import com.github.jpvos.keylogger.ui.DisplayFormat
import com.github.jpvos.keylogger.ui.Table
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.service

class StatisticsComponent : Container(), Counter.Listener, Disposable {

    private val counterService = service<CounterService>()
    private val table = Table(
        arrayOf(
            KeyloggerBundle.message("statistics.table.information"),
            KeyloggerBundle.message("statistics.table.value"),
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
                    DisplayFormat.duration(state.totalTime)
                ),
                arrayOf(
                    KeyloggerBundle.message("statistics.activeTime"),
                    DisplayFormat.duration(state.activeTime)
                ),
                arrayOf(
                    KeyloggerBundle.message("statistics.idleTime"),
                    DisplayFormat.duration(state.idleTime)
                ),
                arrayOf(
                    KeyloggerBundle.message("statistics.idleTimeout"),
                    DisplayFormat.milliseconds(KeyloggerSettings.instance.idleTimeout)

                ),
                arrayOf(
                    KeyloggerBundle.message("statistics.actionsPerMinute"),
                    DisplayFormat.decimal(state.actionsPerMinute)
                )
            )
        )
    }
}
