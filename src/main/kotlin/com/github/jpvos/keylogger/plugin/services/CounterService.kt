package com.github.jpvos.keylogger.plugin.services

import com.github.jpvos.keylogger.core.Action
import com.github.jpvos.keylogger.core.Counter
import com.github.jpvos.keylogger.plugin.settings.KeyloggerSettings
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger


@Service
class CounterService : Counter.Listener, Disposable {
    companion object {
        var id = 0
    }
    private val databaseService = service<DatabaseService>()
    val counter = Counter(idleTimeout = KeyloggerSettings.instance.idleTimeout)
    val id = CounterService.id++
    init {
        restoreCounter()
    }

    override fun dispose() {
        counter.unregisterListener(this)
    }

    override fun onAction(action: Action) {
        thisLogger().warn("Counter service persists action $id: $action")
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
