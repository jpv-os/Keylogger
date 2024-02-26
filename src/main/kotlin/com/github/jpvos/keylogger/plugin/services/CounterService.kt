package com.github.jpvos.keylogger.plugin.services

import com.github.jpvos.keylogger.core.Action
import com.github.jpvos.keylogger.core.Counter
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service


@Service
class CounterService : Counter.Listener, Disposable {
    val counter = Counter(idleTimeout = service<SettingsService>().idleTimeout.toLong())

    init {
        restoreCounter()
    }

    override fun dispose() {
        counter.unregisterListener(this)
    }

    override fun onAction(action: Action) {
        service<DatabaseService>().persistAction(action)
    }

    fun restoreCounter() {
        dispose()
        val databaseService = service<DatabaseService>()
        val actions = databaseService.queryActionsMap()
        val (activeTime, idleTime) = databaseService.queryActiveAndIdleTime()
        counter.setIdleTimeout(service<SettingsService>().idleTimeout.toLong())
        counter.setState(Counter.State(actions, activeTime, idleTime))
        counter.registerListener(this)
    }

}
