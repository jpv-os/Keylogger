package com.github.jpvos.keylogger.services

import com.github.jpvos.keylogger.event.StatisticsEvent
import com.github.jpvos.keylogger.util.Subject
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service


// TODO: decouple services and events, so that instead there is completely separate handling for persistence, UI and statistics caching
@Service
class StatisticsService {
    val subject = Subject<StatisticsEvent>()
    private val counterService = service<CounterService>()
    init {
        counterService.subject.addListener { event ->
            val statisticsEvent = StatisticsEvent(
                event.actions,
                event.totalActiveTime,
                event.totalIdleTime,
            )
            subject.notifyListeners(statisticsEvent)
        }
    }
}
