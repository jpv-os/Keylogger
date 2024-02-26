package com.github.jpvos.keylogger.plugin.util

import com.intellij.ide.ui.UINumericRange
import com.intellij.ui.JBIntSpinner
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBTextField
import javax.swing.JComponent

sealed class FormControl<T>(val getPristineValue: () -> T) {
    abstract var value: T
    abstract val component: JComponent
    val modified: Boolean
        get() = getPristineValue() != value

    fun reset() {
        value = getPristineValue()
    }

    class Text(getPristineValue: () -> String) : FormControl<String>(getPristineValue) {
        override val component = JBTextField()
        override var value: String
            get() = component.text ?: ""
            set(value) {
                component.text = value
            }
    }

    class Integer(getPristineValue: () -> Int) : FormControl<Int>(getPristineValue) {
        override val component = JBIntSpinner(UINumericRange(0, 0, Int.MAX_VALUE))
        override var value: Int
            get() = component.number
            set(value) {
                component.number = value
            }
    }

    class Checkbox(label: String, getPristineValue: () -> Boolean) : FormControl<Boolean>(getPristineValue) {
        override val component = JBCheckBox(label)
        override var value: Boolean
            get() = component.isSelected
            set(value) {
                component.isSelected = value
            }
    }
}
