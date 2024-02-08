package com.github.jpvos.keylogger.util

import com.intellij.openapi.Disposable

// TODO: Replace with some built-in idiomatic Subject/Observable class
class Subject<Event>(event: Event? = null) {
    private var last = event

    init {
        event?.also { notifyListeners(it) }
    }

    private val listeners = mutableListOf<Listener<Event>>()

    fun addListener(fn: (event: Event) -> Unit) {
        val listener = object : Listener<Event>, Disposable {
            override fun onEvent(event: Event) {
                fn(event)
            }

            override fun dispose() {
                removeListener(this)
            }
        }
        listeners.add(listener)
        last?.also { listener.onEvent(it) }
    }

    fun removeListener(listener: Listener<Event>) {
        listeners.remove(listener)
    }

    fun notifyListeners(event: Event) {
        listeners.forEach { it.onEvent(event) }
        last = event
    }
}
