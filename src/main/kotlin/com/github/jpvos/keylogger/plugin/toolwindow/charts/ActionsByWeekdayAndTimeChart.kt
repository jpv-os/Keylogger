package com.github.jpvos.keylogger.plugin.toolwindow.charts

import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.services.DatabaseService
import com.github.jpvos.keylogger.plugin.util.components.UpdatablePanel
import com.intellij.openapi.components.service
import com.intellij.ui.JBColor
import org.knowm.xchart.HeatMapChart
import org.knowm.xchart.HeatMapChartBuilder
import org.knowm.xchart.XChartPanel

class ActionsByWeekdayAndTimeChart : UpdatablePanel() {
    private val heatMapAxisX = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    private val heatMapAxisY = listOf("00-04h", "04-08h", "08-12h", "12-16h", "16-20h", "20-24h")
    private val heatMapChart: HeatMapChart = HeatMapChartBuilder()
        .title(KeyloggerBundle.message("charts.heatmap.title"))
        .width(200)
        .height(200)
        .build()
        .apply {
            styler.apply {
                isLegendVisible = false
                isPlotBorderVisible = false
                isChartTitleBoxVisible = false
                isChartTitleVisible = true
                isShowValue = true
                chartBackgroundColor = JBColor.background()
                plotBackgroundColor = JBColor.background()
                axisTickLabelsColor = JBColor.foreground()
                chartFontColor = JBColor.foreground()
                valueFont = valueFont.deriveFont(10f)
            }
        }
    override val panel = XChartPanel(heatMapChart)

    override fun update() {
        val db = service<DatabaseService>().connection
        val actionsPerWeekdayAnd4hTime = db.queryActionsByWeekdayAndTime(4)
        val seriesName = KeyloggerBundle.message("charts.heatmap.series")
        val heatData = actionsPerWeekdayAnd4hTime.map { arrayOf(it.first, it.second, it.third.toInt()) }
        if (heatMapChart.seriesMap.containsKey(seriesName)) {
            heatMapChart.updateSeries(seriesName, heatMapAxisX, heatMapAxisY, heatData)
        } else {
            heatMapChart.addSeries(seriesName, heatMapAxisX, heatMapAxisY, heatData)
        }
        panel.repaint()
    }
}
