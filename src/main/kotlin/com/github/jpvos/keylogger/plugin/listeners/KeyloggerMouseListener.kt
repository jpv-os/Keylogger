package com.github.jpvos.keylogger.plugin.listeners

import com.github.jpvos.keylogger.core.Action
import com.github.jpvos.keylogger.plugin.services.CounterService
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.event.EditorMouseEvent
import com.intellij.openapi.editor.event.EditorMouseListener

internal class KeyloggerMouseListener : EditorMouseListener {
    private val counterService = service<CounterService>()

    override fun mousePressed(event: EditorMouseEvent) {
        super.mousePressed(event)
        counterService.counter.next(
            Action(
                Action.Type.Mouse,
                when (event.mouseEvent.button) {
                    1 -> "Left Click"
                    2 -> "Middle Click"
                    3 -> "Right Click"
                    else -> event.mouseEvent.button.toString()
                }
            )
        )
    }
}
