package com.github.jpvos.keylogger.plugin.toolwindow.tabs

import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.services.CounterService
import com.github.jpvos.keylogger.plugin.util.components.Container
import com.github.jpvos.keylogger.plugin.util.components.Table
import com.github.jpvos.keylogger.plugin.util.components.TableCell
import com.github.jpvos.keylogger.plugin.util.components.UpdatablePanel
import com.intellij.openapi.components.service

internal class ActionCounterTab : UpdatablePanel() {

    private val table = Table(
        arrayOf(
            KeyloggerBundle.message("actionCounter.table.type"),
            KeyloggerBundle.message("actionCounter.table.name"),
            KeyloggerBundle.message("actionCounter.table.count"),
            KeyloggerBundle.message("actionCounter.table.share"),
        )
    )

    override val panel = Container().apply {
        add(table)
    }

    override fun update() {
        val counterState = service<CounterService>().counter.state
        table.setTableData(
            counterState.actions
                .map { it.toPair() }
                .sortedByDescending { it.second }
                .map {
                    arrayOf(
                        TableCell.Label(it.first.type.toString()),
                        TableCell.Label(it.first.name),
                        TableCell.Long(it.second),
                        TableCell.Percent(it.second.toDouble() / counterState.totalActions.toDouble() * 100.0)
                    )
                }
                .toTypedArray()
        )
    }
}
