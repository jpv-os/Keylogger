package com.github.jpvos.keylogger.services

import com.github.jpvos.keylogger.event.ActionEvent
import com.github.jpvos.keylogger.event.CounterEvent
import com.github.jpvos.keylogger.util.Counter
import com.github.jpvos.keylogger.util.Subject
import com.intellij.openapi.components.Service


@Service
class CounterService {
    private val counter = Counter<ActionEvent>()
    val subject = Subject<CounterEvent>()

    init {
        publishEvent()
    }

    fun countAction(action: ActionEvent) {
        counter.next(action)
        publishEvent()
    }

    private fun publishEvent() {
        subject.notifyListeners(
            CounterEvent(
                counter.getActions(),
                counter.getTotalActiveTime(),
                counter.getTotalIdleTime()
            )
        )
    }

}
