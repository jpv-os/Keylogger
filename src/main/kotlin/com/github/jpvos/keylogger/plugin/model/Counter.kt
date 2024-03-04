package com.github.jpvos.keylogger.plugin.model


/**
 * A counter that keeps track of the number of times an action has been performed.
 */
class Counter(
    /**
     * The time in milliseconds after which the user is considered idle.
     */
    private var idleTimeout: Long = 1000L,
    /**
     * The actions and the number of times they have been performed.
     */
    private var actions: MutableMap<Action, Long> = mutableMapOf(),
    /**
     * The time in milliseconds that the user has been active.
     */
    private var activeTime: Long = 0L,
    /**
     * The time in milliseconds that the user has been idle.
     */
    private var idleTime: Long = 0L,
    /**
     * The last time an action was performed.
     */
    private var lastActionTime: Long? = null,
    /**
     * The last time the counter was updated.
     */
    private var lastUpdateTime: Long = System.currentTimeMillis(),
) {

    /**
     * The state of the counter.
     */
    data class State(
        /**
         * The actions and the number of times they have been performed.
         */
        val actions: Map<Action, Long>,
        /**
         * The time in milliseconds that the user has been active.
         */
        val activeTime: Long,
        /**
         * The time in milliseconds that the user has been idle.
         */
        val idleTime: Long
    ) {
        /**
         * The total time in milliseconds that the user has been active or idle.
         */
        val totalTime by lazy {
            activeTime + idleTime
        }

        /**
         * The number of actions per minute.
         */
        val actionsPerMinute by lazy {
            if (activeTime == 0L) {
                0.0
            } else {
                val minutes = activeTime / 60000.0
                totalActions / minutes
            }
        }

        /**
         * The number of unique actions.
         */
        val uniqueActions by lazy {
            actions.size.toLong()
        }

        /**
         * The total number of actions.
         */
        val totalActions by lazy {
            actions.toList().sumOf { it.second }
        }
    }

    /**
     * A listener that is called whenever an action is performed.
     */
    interface Listener {
        /**
         * Called whenever an action is performed.
         */
        fun onAction(action: Action)
    }

    /**
     * The listeners that are called whenever an action is performed.
     */
    private val listeners = mutableListOf<Listener>()

    /**
     * Set the state of the counter.
     */
    fun setState(state: State) {
        actions.clear()
        actions.putAll(state.actions)
        activeTime = state.activeTime
        idleTime = state.idleTime
        lastActionTime = null
        lastUpdateTime = System.currentTimeMillis()
    }

    /**
     * Get the state of the counter.
     */
    fun getState(): State {
        accumulateTime()
        return State(actions, activeTime, idleTime)
    }

    /**
     * Perform an action.
     */
    fun next(action: Action) {
        accumulateTime()
        lastActionTime = System.currentTimeMillis()
        actions[action] = (actions[action] ?: 0L) + 1L
        accumulateTime()
        listeners.forEach { it.onAction(action) }
    }

    /**
     * Set the idle timeout.
     */
    fun setIdleTimeout(idleTimeout: Long) {
        this.idleTimeout = idleTimeout
    }

    /**
     * Register a listener that is called whenever an action is performed.
     */
    fun registerListener(listener: Listener) {
        listeners.add(listener)
    }

    /**
     * Unregister a listener.
     */
    fun unregisterListener(listener: Listener) {
        listeners.remove(listener)
    }

    /**
     * Accumulate the time since the last update.
     */
    private fun accumulateTime() {
        val now = System.currentTimeMillis()
        val timeSinceLastUpdate = now - lastUpdateTime
        val last = lastActionTime
        val isActive = last != null && (now - last) < idleTimeout
        if (isActive) {
            activeTime += timeSinceLastUpdate
        } else {
            idleTime += timeSinceLastUpdate
        }
        lastUpdateTime = now
    }
}
