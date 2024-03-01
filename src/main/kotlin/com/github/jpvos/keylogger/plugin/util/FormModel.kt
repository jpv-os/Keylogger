package com.github.jpvos.keylogger.plugin.util

class FormModel<T : Enum<T>> {

    private val formControls = mutableMapOf<T, FormControl<*>>()
    val modified: Boolean
        get() = formControls.values.any { it.modified }

    fun reset() {
        formControls.values.forEach { it.reset() }
    }

    fun addText(field: T, getDefaultValue: () -> String) =
        FormControl.Text(getDefaultValue).also { add(field, it) }

    fun addInteger(field: T, getDefaultValue: () -> Int) =
        FormControl.Integer(getDefaultValue).also { add(field, it) }

    fun addBoolean(field: T, label: String, getDefaultValue: () -> Boolean) =
        FormControl.Checkbox(label, getDefaultValue).also { add(field, it) }

    fun <R> get(field: T): FormControl<R> =
        formControls[field]?.let {
            @Suppress("UNCHECKED_CAST")
            it as FormControl<R>
        } ?: throw NoSuchElementException("Input not found: $field")

    fun getComponent(field: T) = get<Any?>(field).component

    private fun add(name: T, input: FormControl<*>) {
        formControls[name] = input
    }
}
