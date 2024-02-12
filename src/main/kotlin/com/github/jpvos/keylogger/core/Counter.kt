package com.github.jpvos.keylogger.core



class Counter(
    private var idleTimeout: Long = 1000L,
    private var actions: MutableMap<Action, Long> = mutableMapOf(),
    private var activeTime: Long = 0L,
    private var idleTime: Long = 0L,
    private var lastActionTime: Long? = null,
    private var lastUpdateTime: Long = System.currentTimeMillis(),
) {

    data class State (
        val actions: Map<Action, Long>,
        val activeTime: Long,
        val idleTime: Long
    )

    interface Listener {
        fun onAction(action: Action)
    }

    fun setState(state: State) {
        actions.clear()
        actions.putAll(state.actions)
        activeTime = state.activeTime
        idleTime = state.idleTime
        lastActionTime = null
        lastUpdateTime = System.currentTimeMillis()
        accumulateTime()
    }

    fun getState(): State {
        accumulateTime()
        return State(actions, activeTime, idleTime)
    }

    fun next(action: Action) {
        accumulateTime()
        lastActionTime = System.currentTimeMillis()
        actions[action] = (actions[action] ?: 0L) + 1L
        accumulateTime()
        listeners.forEach { it.onAction(action) }
    }

    private val listeners = mutableListOf<Listener>()

    fun registerListener(listener: Listener) {
        listeners.add(listener)
    }

    fun unregisterListener(listener: Listener) {
        listeners.remove(listener)
    }

    fun accumulateTime() {
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
