package com.github.jpvos.keylogger.listeners

import com.github.jpvos.keylogger.KeyloggerBundle
import com.github.jpvos.keylogger.event.ActionEvent
import com.github.jpvos.keylogger.services.CounterService
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.event.EditorMouseEvent
import com.intellij.openapi.editor.event.EditorMouseListener

internal class MouseListener : EditorMouseListener {
    private val counterService = service<CounterService>()
    override fun mousePressed(event: EditorMouseEvent) {
        super.mousePressed(event)
        counterService.countAction(
            ActionEvent(
                ActionEvent.Type.Mouse,
                KeyloggerBundle.message("general.click")
            )
        )
    }
}
