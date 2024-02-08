package com.github.jpvos.keylogger.toolWindow.components

import com.github.jpvos.keylogger.KeyloggerBundle
import com.github.jpvos.keylogger.services.StatisticsService
import com.github.jpvos.keylogger.ui.UiContainer
import com.github.jpvos.keylogger.ui.UiTable
import com.intellij.openapi.components.service

class ActionsComponent : UiContainer() {

    private val statisticsService = service<StatisticsService>()

    init {
        add(
            UiTable(
            arrayOf(
                KeyloggerBundle.message("actions.table.type"),
                KeyloggerBundle.message("actions.table.name"),
                KeyloggerBundle.message("actions.table.count"),
            )
        ).apply {
            statisticsService.subject.addListener { event ->
                setTableData(event.actions
                    .map { it.toPair() }
                    .sortedByDescending { it.second }
                    .map { arrayOf(it.first.type, it.first.name, it.second) }
                    .toTypedArray()
                )
            }
        })
    }
}
