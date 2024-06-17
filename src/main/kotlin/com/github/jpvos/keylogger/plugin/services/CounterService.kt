package com.github.jpvos.keylogger.plugin.services

import com.github.jpvos.keylogger.plugin.model.Action
import com.github.jpvos.keylogger.plugin.model.Counter
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import kotlinx.coroutines.*
import java.time.Duration
import java.time.Instant


/**
 * This service is responsible for managing the counter:
 * - restoring the counter from the database
 * - providing the counter to the rest of the application
 * - listening to the counter and persisting actions to the database
 */
@Service
internal class CounterService : Counter.Listener, Disposable {
    /**
     * The action counter.
     */
    val counter = Counter(idleTimeout = service<SettingsService>().state.idleTimeout.toLong())
    private val actionBatch = mutableListOf<Action>()
    private var lastBatchInsertTime = Instant.now()
    private var insertInterval = service<SettingsService>().state.insertInterval.toLong()
    private val batchInsertJob: Job


    init {
        restoreCounter()
        batchInsertJob = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                delay(service<SettingsService>().state.insertInterval.toLong())
                val currentTime = Instant.now()
                val updateDue = Duration.between(lastBatchInsertTime, currentTime)
                    .toMillis() > insertInterval
                if (updateDue && actionBatch.isNotEmpty()) {
                    service<DatabaseService>().connection.batchPersist(actionBatch)
                    actionBatch.clear()
                    lastBatchInsertTime = currentTime
                }
            }
        }
    }

    /**
     * @see [Disposable.dispose]
     */
    override fun dispose() {
        counter.unregisterListener(this)
        stopBatchInsertJob()
    }

    private fun stopBatchInsertJob() {
        batchInsertJob.cancel()
        if (actionBatch.isNotEmpty()) {
            service<DatabaseService>().connection.batchPersist(actionBatch)
        }
    }

    /**
     * Each time the counter registers an action, persist it to the database.
     */
    override fun onAction(action: Action) {
        service<DatabaseService>().connection.persist(action)
    }

    /**
     * Restore the counter from the database.
     */
    fun restoreCounter() {
        // unregister this service as a listener to avoid being notified of the actions that are restored
        counter.unregisterListener(this)

        // read the actions and active/idle time from the database
        val databaseService = service<DatabaseService>()
        val idleTimeout = service<SettingsService>().state.idleTimeout.toLong()
        val actions = databaseService.connection.queryActionMap()
        val (activeTime, idleTime) = databaseService.connection.queryActiveAndIdleTime(idleTimeout)

        // restore the counter
        counter.setIdleTimeout(idleTimeout)
        counter.state = Counter.State(actions, activeTime, idleTime)

        // re-register this service as a listener after restoring the counter
        counter.registerListener(this)
    }

}
