package com.github.jpvos.keylogger.plugin.toolwindow.components

import com.github.jpvos.keylogger.core.Action
import com.github.jpvos.keylogger.core.Counter
import com.github.jpvos.keylogger.plugin.services.CounterService
import com.github.jpvos.keylogger.plugin.services.DatabaseService
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.service
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import org.knowm.xchart.PieChart
import org.knowm.xchart.PieChartBuilder
import org.knowm.xchart.XChartPanel
import org.knowm.xchart.style.PieStyler


class ChartsComponent :  JBPanel<JBPanel<*>>(), Counter.Listener, Disposable {
    private val actions = service<DatabaseService>().queryActionsMap().toMutableMap()
    private val pieChart: PieChart = PieChartBuilder()
        .title("Actions")
        .width(800)
        .height(600)
        .build()
        .apply {
            styler.apply {
                isLegendVisible = false
                isLabelsVisible = true
                labelsFont = labelsFont.deriveFont(8f)
                labelType = PieStyler.LabelType.NameAndValue
            }
        }
    private val pieChartPanel = XChartPanel(pieChart)

    init {
        actions
            .toList()
            .sortedByDescending { it.second }
            .forEach { (action, count) -> pieChart.addSeries(actionLabel(action), count) }
        add(JBScrollPane(pieChartPanel))
        service<CounterService>().counter.registerListener(this)
    }

    override fun onAction(action: Action) {
        actions[action] = actions.getOrDefault(action, 0) + 1
        pieChart.updatePieSeries(actionLabel(action), actions.getOrDefault(action, 0))
        pieChartPanel.repaint()

    }

    override fun dispose() {
        service<CounterService>().counter.unregisterListener(this)
    }

    private fun actionLabel(action: Action): String {
        return "${action.type} | ${action.name}"
    }
}
