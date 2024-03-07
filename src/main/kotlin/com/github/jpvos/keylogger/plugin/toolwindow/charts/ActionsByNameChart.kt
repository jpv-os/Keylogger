package com.github.jpvos.keylogger.plugin.toolwindow.charts

import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.services.DatabaseService
import com.github.jpvos.keylogger.plugin.util.components.UpdatablePanel
import com.intellij.openapi.components.service
import com.intellij.ui.JBColor
import org.knowm.xchart.PieChart
import org.knowm.xchart.PieChartBuilder
import org.knowm.xchart.XChartPanel
import org.knowm.xchart.style.PieStyler

@Suppress("unused")
class ActionsByNameChart : UpdatablePanel() {
    private val chart: PieChart = PieChartBuilder()
        .title(KeyloggerBundle.message("charts.pieName.title"))
        .width(200)
        .height(200)
        .build()
        .apply {
            styler.apply {
                isLegendVisible = false
                isPlotBorderVisible = false
                isChartTitleBoxVisible = false
                isLabelsVisible = true
                labelType = PieStyler.LabelType.NameAndValue
                chartBackgroundColor = JBColor.background()
                plotBackgroundColor = JBColor.background()
                chartFontColor = JBColor.foreground()
            }
        }
    override val panel = XChartPanel(chart)

    override fun update() {
        val db = service<DatabaseService>().connection
        val actions = db.queryActionMap()
        actions.toList().map { it.first }.forEach {
            val seriesName = "${it.type} | ${it.name}"
            if (!chart.seriesMap.containsKey(seriesName)) {
                chart.addSeries(seriesName, 0)
            }
            chart.updatePieSeries(seriesName, actions.getOrDefault(it, 0))
        }
        panel.repaint()
    }
}
