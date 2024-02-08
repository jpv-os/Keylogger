package com.github.jpvos.keylogger.ui

import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import javax.swing.JTable
import javax.swing.ListSelectionModel
import javax.swing.table.DefaultTableModel


class UiTable(private val columnHeaders: Array<String>) : UiContainer() {
    private val tableModel = DefaultTableModel(columnHeaders, 0)
    private val table = JBTable(tableModel).apply {
        autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)
        fillsViewportHeight = false
    }

    init {
        add(JBScrollPane(table))
    }

    fun setTableData(data: Array<Array<*>>) {
        require(data.all { it.size == columnHeaders.size })
        tableModel.setDataVector(data, columnHeaders)
    }
}
