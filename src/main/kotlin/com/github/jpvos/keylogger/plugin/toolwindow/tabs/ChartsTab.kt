package com.github.jpvos.keylogger.plugin.toolwindow.tabs

import com.github.jpvos.keylogger.plugin.model.Action
import com.github.jpvos.keylogger.plugin.model.Counter
import com.github.jpvos.keylogger.plugin.services.CounterService
import com.github.jpvos.keylogger.plugin.services.DatabaseService
import com.github.jpvos.keylogger.plugin.util.components.Container
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.service
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import org.knowm.xchart.*
import org.knowm.xchart.style.PieStyler
import org.knowm.xchart.style.Styler
import javax.swing.BoxLayout

internal class ChartsTab : Container(), Counter.Listener, Disposable {
    private val actions = service<DatabaseService>().connection.queryActionHistogram().toMutableMap()
    private val columnWidth = 300
    private val rowHeight = 200
    private val activityChart: XYChart = XYChartBuilder()
        .title("Actions by date and type")
        .xAxisTitle("Date (UTC)")
        .yAxisTitle("Actions")
        .width(columnWidth * 2)
        .height(rowHeight)
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
                // setSeriesColors(arrayOf(JBColor.foreground()))
            }
        }
    private val activityChartPanel = XChartPanel(activityChart)
    private val pieChartActionsByName: PieChart = PieChartBuilder()
        .title("Actions by name")
        .width(columnWidth)
        .height(rowHeight)
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
    private val pieChartActionsByNamePanel = XChartPanel(pieChartActionsByName)
    private val pieChartActionsByType: PieChart = PieChartBuilder()
        .title("Actions by type")
        .width(columnWidth)
        .height(rowHeight)
        .build()
        .apply {
            styler.apply {
                isLegendVisible = false
                isPlotBorderVisible = false
                isChartTitleBoxVisible = false
                isLabelsVisible = true
                labelType = PieStyler.LabelType.NameAndValue
                isForceAllLabelsVisible = true
                chartBackgroundColor = JBColor.background()
                plotBackgroundColor = JBColor.background()
                chartFontColor = JBColor.foreground()
            }
        }
    private val pieChartActionsByTypePanel = XChartPanel(pieChartActionsByType)
    private val heatMapChart: HeatMapChart = HeatMapChartBuilder()
        .title("Actions per weekday and time (UTC)")
        .width(columnWidth * 2)
        .height(rowHeight)
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
    private val heatMapChartPanel = XChartPanel(heatMapChart)
    private val container = JBPanel<JBPanel<*>>().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(activityChartPanel)
        add(JBPanel<JBPanel<*>>().apply {
            add(pieChartActionsByNamePanel)
            add(pieChartActionsByTypePanel)
        })
        add(heatMapChartPanel)
    }
    private val scrollPane = JBScrollPane(container).apply {
        border = null
    }

    init {
        // init activity chart
        service<DatabaseService>()
            .connection
            .queryActivityPerDayAndType()
            .groupBy { it.second }
            .map { entry -> entry.key to entry.value.map { it.first to it.third } }
            .forEach { (type, data) ->
                activityChart.addSeries(type, data.map { it.first }, data.map { it.second })
            }
        service<DatabaseService>()
            .connection
            .queryActivityPerDay()
            .let { data ->
                activityChart.addSeries(
                    "Total",
                    data.map { it.first },
                    data.map { it.second }
                )
            }

        // init pie chart for actions by name
        actions
            .toList()
            .sortedByDescending { it.second }
            .forEach { (action, count) ->
                pieChartActionsByName.addSeries(actionLabel(action), count)
            }

        // init pie chart for actions by type
        for (type in Action.Type.values()) {
            pieChartActionsByType.addSeries(type.toString(), 0)
            pieChartActionsByType.updatePieSeries(type.toString(), actions.filter { it.key.type == type }.values.sum())
        }

        // init heat map for actions per day and time
        val heatMapData = service<DatabaseService>().connection.queryActionsByDayAndTime(4)
        heatMapChart.addSeries(
            "Actions",
            listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"),
            listOf("00-04h", "04-08h", "08-12h", "12-16h", "16-20h", "20-24h"),
            heatMapData.map { arrayOf(it.first, it.second, it.third.toInt()) }
        )

        add(scrollPane)
        service<CounterService>().counter.registerListener(this)
    }

    override fun onAction(action: Action) {
        actions[action] = actions.getOrDefault(action, 0) + 1
        val label = actionLabel(action)
        if (!pieChartActionsByName.seriesMap.containsKey(actionLabel(action))) {
            pieChartActionsByName.addSeries(label, 0)
        }
        pieChartActionsByName.updatePieSeries(actionLabel(action), actions.getOrDefault(action, 0))
        pieChartActionsByNamePanel.repaint()

        val type = action.type.toString()
        pieChartActionsByType.updatePieSeries(type, actions.filter { it.key.type == action.type }.values.sum())
        pieChartActionsByTypePanel.repaint()

        // TODO repaint remaining charts
    }

    override fun dispose() {
        service<CounterService>().counter.unregisterListener(this)
    }

    private fun actionLabel(action: Action): String {
        return "${action.type} | ${action.name}"
    }
}
