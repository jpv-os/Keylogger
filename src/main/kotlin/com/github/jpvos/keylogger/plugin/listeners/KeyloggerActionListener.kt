package com.github.jpvos.keylogger.plugin.listeners

import com.github.jpvos.keylogger.plugin.model.Action
import com.github.jpvos.keylogger.plugin.services.CounterService
import com.github.jpvos.keylogger.plugin.services.SettingsService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.AnActionResult
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.ex.AnActionListener
import com.intellij.openapi.components.service

internal class KeyloggerActionListener : AnActionListener {
    override fun afterActionPerformed(action: AnAction, event: AnActionEvent, result: AnActionResult) {
        super.afterActionPerformed(action, event, result)
        val actionName = action.templatePresentation.text?.toString() ?: run {
            // When the action does not have a template presentation,
            // we cannot determine the action name.
            return
        }
        if (actionName == SettingsService.IDEA_VIM_ACTION_NAME && service<SettingsService>().state.ideaVim) {
            // When using the IdeaVim plugin,
            // the backspace key triggers both the "Backspace" and "Shortcuts" actions.
            // Therefore, we ignore the "Shortcuts" action.
            return
        }
        service<CounterService>().counter.next(
            Action(Action.Type.EditorAction, actionName)
        )
    }

    override fun afterEditorTyping(c: Char, dataContext: DataContext) {
        super.afterEditorTyping(c, dataContext)
        service<CounterService>().counter.next(
            Action(
                Action.Type.Keystroke, "<$c>"
            )
        )
    }
}
