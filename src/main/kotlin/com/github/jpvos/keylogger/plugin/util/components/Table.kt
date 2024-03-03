package com.github.jpvos.keylogger.plugin.util.components

import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import java.awt.Font
import javax.swing.ListSelectionModel
import javax.swing.SwingConstants
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.DefaultTableModel
import kotlin.math.max


class Table(private val columnHeaders: Array<String>) : Container() {

    private val tableModel = object : DefaultTableModel(columnHeaders, 0) {
        override fun isCellEditable(row: Int, column: Int): Boolean {
            return false
        }
    }
    private val tableCellRenderer = object : DefaultTableCellRenderer() {
        override fun setValue(value: Any?) {
            text = value.toString()
            if (value !is TableCell.Label) {
                horizontalAlignment = SwingConstants.RIGHT
            } else {
                horizontalAlignment = SwingConstants.LEFT
            }
            font = Font(Font.MONOSPACED, Font.PLAIN, max(font.size - 2, 1))
        }
    }
    private val table = JBTable(tableModel).apply {
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)
        fillsViewportHeight = false
        setDefaultRenderer(Any::class.java, tableCellRenderer)
    }

    init {
        add(JBScrollPane(table))
    }

    fun setTableData(data: Array<Array<TableCell>>) {
        require(data.all { it.size == columnHeaders.size })
        tableModel.setDataVector(data, columnHeaders)
    }
}
