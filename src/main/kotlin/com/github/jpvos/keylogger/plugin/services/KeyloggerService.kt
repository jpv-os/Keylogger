package com.github.jpvos.keylogger.plugin.services

import com.github.jpvos.keylogger.core.Action
import com.github.jpvos.keylogger.core.Counter
import com.intellij.openapi.components.Service


@Service
class KeyloggerService : Counter.Listener {
    val counter = Counter()

    init {
        // TODO: read from storage
        val initialState = Counter.State(
            activeTime = 0,
            idleTime = 0,
            actions = emptyMap()
        )
        counter.setState(initialState)
    }

    override fun onAction(action: Action) {
        // TODO: save action/state to storage
    }

}
