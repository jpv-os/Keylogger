package com.github.jpvos.keylogger.plugin.util

class Form<T : Enum<T>>(val model: FormModel<T>, val layout: FormBuilder<T>) {
    companion object {
        fun <T : Enum<T>> create(
            build: FormBuilder<T>.() -> Unit,
        ): Form<T> {
            val model = FormModel<T>()
            val layout = FormBuilder(model).apply(build)
            return Form(model, layout)
        }
    }
}
