package com.github.jpvos.keylogger.plugin.listeners

import com.github.jpvos.keylogger.core.Action
import com.github.jpvos.keylogger.plugin.services.CounterService
import com.github.jpvos.keylogger.plugin.settings.KeyloggerSettings
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.AnActionResult
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.ex.AnActionListener
import com.intellij.openapi.application.ApplicationActivationListener
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.event.EditorMouseEvent
import com.intellij.openapi.editor.event.EditorMouseListener
import com.intellij.openapi.wm.IdeFrame

// FIXME: all IDE instances are considered active and publish keystrokes in other instances
internal class KeyloggerListener : AnActionListener, EditorMouseListener, ApplicationActivationListener {
    private val counterService = service<CounterService>()
    private var active = true

    override fun applicationActivated(ideFrame: IdeFrame) {
        // TODO
        // active = true
    }

    override fun applicationDeactivated(ideFrame: IdeFrame) {
        // TODO
        // active = false
    }

    override fun afterActionPerformed(action: AnAction, event: AnActionEvent, result: AnActionResult) {
        super.afterActionPerformed(action, event, result)
        if (!active) {
            return
        }

        val actionName = action.toString()
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
        if (!active) {
            return
        }
        counterService.counter.next(
            Action(
                Action.Type.Keystroke,
                "<$c>"
            )
        )
    }

    override fun mousePressed(event: EditorMouseEvent) {
        super.mousePressed(event)
        if (!active) {
            return
        }
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
