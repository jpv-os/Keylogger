package com.github.jpvos.keylogger.plugin.toolwindow.tabs

import com.github.jpvos.keylogger.plugin.toolwindow.charts.ActionsByDateAndTypeChart
import com.github.jpvos.keylogger.plugin.toolwindow.charts.ActionsByNameChart
import com.github.jpvos.keylogger.plugin.toolwindow.charts.ActionsByTypeChart
import com.github.jpvos.keylogger.plugin.toolwindow.charts.ActionsByWeekdayAndTimeChart
import com.github.jpvos.keylogger.plugin.util.components.Container
import com.github.jpvos.keylogger.plugin.util.components.UpdatablePanel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import javax.swing.BoxLayout

internal class ChartsTab : UpdatablePanel() {
    private val actionsByDateAndTypeChart = ActionsByDateAndTypeChart()
    private val actionsByTypeChart = ActionsByTypeChart()
    private val actionsByNameChart = ActionsByNameChart()
    private val actionsByWeekdayAndTimeChart = ActionsByWeekdayAndTimeChart()

    private val container = JBPanel<JBPanel<*>>().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(actionsByDateAndTypeChart.panel)
        add(actionsByWeekdayAndTimeChart.panel)
        add(JBPanel<JBPanel<*>>().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            add(actionsByTypeChart.panel)
            add(actionsByNameChart.panel)
        })
    }
    private val scrollPane = JBScrollPane(container).apply {
        border = null
    }

    override val panel = Container().apply {
        add(scrollPane)
    }

    override fun update() {
        actionsByDateAndTypeChart.update()
        actionsByTypeChart.update()
        actionsByNameChart.update()
        actionsByWeekdayAndTimeChart.update()
    }
}
