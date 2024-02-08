package com.github.jpvos.keylogger.util

// TODO: Replace with some built-in idiomatic listener class
interface Listener<Event> {
    fun onEvent(event: Event)
}
