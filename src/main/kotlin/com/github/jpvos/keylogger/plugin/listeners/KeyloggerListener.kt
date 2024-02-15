package com.github.jpvos.keylogger.plugin.listeners

import com.github.jpvos.keylogger.core.Action
import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.github.jpvos.keylogger.plugin.services.CounterService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.AnActionResult
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.ex.AnActionListener
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.event.EditorMouseEvent
import com.intellij.openapi.editor.event.EditorMouseListener

internal class KeyloggerListener : AnActionListener, EditorMouseListener {
    private val counterService = service<CounterService>()
    override fun afterActionPerformed(action: AnAction, event: AnActionEvent, result: AnActionResult) {
        super.afterActionPerformed(action, event, result)
        counterService.counter.next(
            Action(
                Action.Type.EditorAction,
                action.templatePresentation.text?.toString() ?: KeyloggerBundle.message("general.unknown")
            )
        )
    }

    override fun afterEditorTyping(c: Char, dataContext: DataContext) {
        super.afterEditorTyping(c, dataContext)
        counterService.counter.next(
            Action(
                Action.Type.Keystroke,
                "<$c>"
            )
        )
    }

    override fun mousePressed(event: EditorMouseEvent) {
        super.mousePressed(event)
        counterService.counter.next(
            Action(
                Action.Type.Mouse,
                KeyloggerBundle.message("general.click")
            )
        )
    }
}
