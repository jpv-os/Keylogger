package com.github.jpvos.keylogger.listeners

import com.github.jpvos.keylogger.KeyloggerBundle
import com.github.jpvos.keylogger.event.ActionEvent
import com.github.jpvos.keylogger.services.CounterService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.AnActionResult
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.ex.AnActionListener
import com.intellij.openapi.components.service

internal class KeyboardListener : AnActionListener {
    private val counterService = service<CounterService>()
    override fun afterActionPerformed(action: AnAction, event: AnActionEvent, result: AnActionResult) {
        super.afterActionPerformed(action, event, result)
        counterService.countAction(
            ActionEvent(
                ActionEvent.Type.EditorAction,
                action.templatePresentation.text?.toString() ?: KeyloggerBundle.message("general.unknown")
            )
        )
    }

    override fun afterEditorTyping(c: Char, dataContext: DataContext) {
        super.afterEditorTyping(c, dataContext)
        counterService.countAction(
            ActionEvent(
                ActionEvent.Type.Keystroke,
                "<$c>"
            )
        )
    }
}
