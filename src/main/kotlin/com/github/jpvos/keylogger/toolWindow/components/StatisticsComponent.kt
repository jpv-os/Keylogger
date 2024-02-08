package com.github.jpvos.keylogger.toolWindow.components

import com.github.jpvos.keylogger.KeyloggerBundle
import com.github.jpvos.keylogger.services.StatisticsService
import com.github.jpvos.keylogger.ui.UiContainer
import com.github.jpvos.keylogger.ui.UiTable
import com.intellij.openapi.components.service

class StatisticsComponent : UiContainer() {

    private val statisticsService = service<StatisticsService>()

    init {
        add(
            UiTable(arrayOf(
            KeyloggerBundle.message("statistics.table.information"),
            KeyloggerBundle.message("statistics.table.value"),
        )).apply {
            statisticsService.subject.addListener { event ->
                setTableData(
                    arrayOf(
                        arrayOf(KeyloggerBundle.message("statistics.totalActions"), event.totalActions),
                        arrayOf(KeyloggerBundle.message("statistics.uniqueActions"), event.uniqueActions),
                        arrayOf(KeyloggerBundle.message("statistics.totalTime"), event.totalTime),
                        arrayOf(KeyloggerBundle.message("statistics.activeTime"), event.totalActiveTime),
                        arrayOf(KeyloggerBundle.message("statistics.idleTime"), event.totalIdleTime),
                        arrayOf(KeyloggerBundle.message("statistics.actionsPerMinute"), event.apm),
                    )
                )
            }
        })
    }
}
