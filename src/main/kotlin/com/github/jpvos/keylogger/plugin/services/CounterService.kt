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
    val counter = Counter(idleTimeout = KeyloggerSettings.instance.idleTimeout)

    init {
        restoreCounter()
    }

    override fun dispose() {
        counter.unregisterListener(this)
    }

    override fun onAction(action: Action) {
        databaseService.persistAction(action)
    }

    fun restoreCounter() {
        dispose()
        val actions = databaseService.queryActionsMap()
        val (activeTime, idleTime) = databaseService.queryActiveAndIdleTime()
        counter.setIdleTimeout(KeyloggerSettings.instance.idleTimeout)
        counter.setState(Counter.State(actions, activeTime, idleTime))
        counter.registerListener(this)
    }

}
