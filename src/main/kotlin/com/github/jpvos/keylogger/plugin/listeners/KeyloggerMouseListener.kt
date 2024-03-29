package com.github.jpvos.keylogger.plugin.listeners

import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.model.Action
import com.github.jpvos.keylogger.plugin.services.CounterService
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.event.EditorMouseEvent
import com.intellij.openapi.editor.event.EditorMouseListener

/**
 * This class listens to mouse events in the editor.
 * It forwards the mouse events to the [CounterService].
 */
internal class KeyloggerMouseListener : EditorMouseListener {
    /**
     * This method is called after a mouse button has been pressed.
     * It forwards the mouse button to the [CounterService].
     */
    override fun mousePressed(event: EditorMouseEvent) {
        super.mousePressed(event)
        service<CounterService>().counter.next(
            Action(
                Action.Type.Mouse,
                when (event.mouseEvent.button) {
                    1 -> KeyloggerBundle.message("action.mouse.left")
                    2 -> KeyloggerBundle.message("action.mouse.middle")
                    3 -> KeyloggerBundle.message("action.mouse.right")
                    else -> event.mouseEvent.button.toString()
                }
            )
        )
    }
}
