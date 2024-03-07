package com.github.jpvos.keylogger.plugin.util.components

import java.awt.Component

abstract class UpdatablePanel : Component() {
    abstract val panel: Component
    abstract fun update()
}
