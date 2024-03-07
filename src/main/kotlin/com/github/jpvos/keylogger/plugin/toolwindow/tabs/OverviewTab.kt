package com.github.jpvos.keylogger.plugin.toolwindow.tabs

import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.services.CounterService
import com.github.jpvos.keylogger.plugin.util.components.Container
import com.github.jpvos.keylogger.plugin.util.components.Table
import com.github.jpvos.keylogger.plugin.util.components.TableCell
import com.github.jpvos.keylogger.plugin.util.components.UpdatablePanel
import com.intellij.openapi.components.service

internal class OverviewTab : UpdatablePanel() {

    private val table = Table(
        arrayOf(
            KeyloggerBundle.message("overview.table.information"),
            KeyloggerBundle.message("overview.table.value"),
        )
    )

    override val panel = Container().apply {
        add(table)
    }

    override fun update() {
        val counterState = service<CounterService>().counter.state
        table.setTableData(
            arrayOf(
                arrayOf(
                    TableCell.Label(KeyloggerBundle.message("overview.totalActions")),
                    TableCell.Long(counterState.totalActions),
                ),
                arrayOf(
                    TableCell.Label(KeyloggerBundle.message("overview.uniqueActions")),
                    TableCell.Long(counterState.uniqueActions)
                ),
                arrayOf(
                    TableCell.Label(KeyloggerBundle.message("overview.totalTime")),
                    TableCell.Duration(counterState.totalTime)
                ),
                arrayOf(
                    TableCell.Label(KeyloggerBundle.message("overview.activeTime")),
                    TableCell.Duration(counterState.activeTime)
                ),
                arrayOf(
                    TableCell.Label(KeyloggerBundle.message("overview.idleTime")),
                    TableCell.Duration(counterState.idleTime)
                ),
                arrayOf(
                    TableCell.Label(KeyloggerBundle.message("overview.actionsPerMinute")),
                    TableCell.Decimal(counterState.actionsPerMinute)
                )
            )
        )
    }
}
