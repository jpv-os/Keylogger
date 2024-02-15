package com.github.jpvos.keylogger.plugin.services

import com.github.jpvos.keylogger.core.Action
import com.github.jpvos.keylogger.core.Counter
import com.github.jpvos.keylogger.plugin.settings.KeyloggerSettings
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service


@Service
class CounterService : Counter.Listener, Disposable {
    private val databaseService = service<DatabaseService>()
    private var _counter = Counter(idleTimeout = KeyloggerSettings.instance.idleTimeout)
    val counter: Counter
        get() = _counter

    init {
        restoreCounter()
    }

    override fun dispose() {
        _counter.unregisterListener(this)
    }

    override fun onAction(action: Action) {
        databaseService.persistAction(action)
    }

    fun restoreCounter() {
        dispose()
        val actions = databaseService.queryActionsMap()
        val (activeTime, idleTime) = databaseService.queryActiveAndIdleTime()
        _counter = Counter(idleTimeout = KeyloggerSettings.instance.idleTimeout)
        _counter.setState(Counter.State(actions, activeTime, idleTime))
        _counter.registerListener(this)
    }

}
