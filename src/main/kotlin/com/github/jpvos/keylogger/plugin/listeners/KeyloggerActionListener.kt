package com.github.jpvos.keylogger.plugin.listeners

import com.github.jpvos.keylogger.core.Action
import com.github.jpvos.keylogger.plugin.services.CounterService
import com.github.jpvos.keylogger.plugin.services.KeyloggerSettings
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.AnActionResult
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.ex.AnActionListener
import com.intellij.openapi.components.service

internal class KeyloggerActionListener : AnActionListener {
    private val counterService = service<CounterService>()

    override fun afterActionPerformed(action: AnAction, event: AnActionEvent, result: AnActionResult) {
        super.afterActionPerformed(action, event, result)
        val actionName = action.templatePresentation.text?.toString() ?: return
        if (actionName == "Shortcuts" && KeyloggerSettings.instance.ideaVim) {
            // When using the IdeaVim plugin,
            // the backspace key triggers both the "Backspace" and "Shortcuts" actions.
            // Therefore, we ignore the "Shortcuts" action.
            return
        }
        counterService.counter.next(
            Action(Action.Type.EditorAction, actionName)
        )
    }

    override fun afterEditorTyping(c: Char, dataContext: DataContext) {
        super.afterEditorTyping(c, dataContext)
        counterService.counter.next(
            Action(
                Action.Type.Keystroke, "<$c>"
            )
        )
    }
}
