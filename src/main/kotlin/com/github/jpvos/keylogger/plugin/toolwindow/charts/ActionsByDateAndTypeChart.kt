package com.github.jpvos.keylogger.plugin.toolwindow.charts

import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.services.DatabaseService
import com.github.jpvos.keylogger.plugin.util.components.UpdatablePanel
import com.intellij.openapi.components.service
import com.intellij.ui.JBColor
import org.knowm.xchart.XChartPanel
import org.knowm.xchart.XYChart
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.Styler
import java.awt.Component

class ActionsByDateAndTypeChart : UpdatablePanel() {

    private val chart: XYChart = XYChartBuilder()
        .title(KeyloggerBundle.message("charts.activity.title"))
        .xAxisTitle(KeyloggerBundle.message("charts.activity.axis.x"))
        .yAxisTitle(KeyloggerBundle.message("charts.activity.axis.y"))
        .width(200)
        .height(200)
        .build()
        .apply {
            styler.apply {
                isLegendVisible = true
                legendPosition = Styler.LegendPosition.OutsideE
                legendBackgroundColor = JBColor.background()
                isPlotBorderVisible = false
                isChartTitleBoxVisible = false
                chartBackgroundColor = JBColor.background()
                plotBackgroundColor = JBColor.background()
                chartFontColor = JBColor.foreground()
                axisTickLabelsColor = JBColor.foreground()
            }
        }

    override val panel: Component = XChartPanel(chart)

    override fun update() {
        val db = service<DatabaseService>().connection
        val activityPerDay = db.queryActivityPerDay()
        val activityPerDayAndType = db.queryActivityPerDayAndType()

        val totalSeriesName = KeyloggerBundle.message("charts.activity.series.total")
        val totalXData = activityPerDay.map { it.first }
        val totalYData = activityPerDay.map { it.second }
        if (chart.seriesMap.containsKey(totalSeriesName)) {
            chart.updateXYSeries(totalSeriesName, totalXData, totalYData, totalYData.map { 0 })
        } else {
            chart.addSeries(totalSeriesName, totalXData, totalYData)
        }

        activityPerDayAndType
            .groupBy { it.second }
            .map { entry -> entry.key to entry.value.map { it.first to it.third } }
            .forEach { (seriesName, data) ->
                val seriesXData = data.map { it.first }
                val seriesYData = data.map { it.second }
                if (chart.seriesMap.containsKey(seriesName)) {
                    chart.updateXYSeries(seriesName, seriesXData, seriesYData, seriesYData.map { 0 })
                } else {
                    chart.addSeries(seriesName, seriesXData, seriesYData)
                }
            }
        panel.repaint()
    }
}
