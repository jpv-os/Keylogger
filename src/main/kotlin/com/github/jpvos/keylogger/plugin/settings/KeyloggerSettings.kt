package com.github.jpvos.keylogger.plugin.settings

import com.github.jpvos.keylogger.plugin.KeyloggerBundle
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import kotlin.io.path.Path


@State(
    name = "com.github.jpvos.keylogger.plugin.settings.KeyloggerSettings",
    storages = [Storage("Keylogger.xml")]
)
internal class KeyloggerSettings : PersistentStateComponent<KeyloggerSettings> {
    companion object {
        val instance: KeyloggerSettings
            get() = ApplicationManager.getApplication().getService(KeyloggerSettings::class.java)
        val defaultDatabaseURL = Path(
            System.getProperty("user.home"),
            *KeyloggerBundle.message("settings.form.databaseURL.defaultValue").split("/").toTypedArray()
        ).toString()
        val defaultIdleTimeout = KeyloggerBundle.message("settings.form.idleTimeout.defaultValue").toLong()
    }

    var databaseURL = defaultDatabaseURL
    var idleTimeout = defaultIdleTimeout

    override fun getState(): KeyloggerSettings {
        return this
    }

    override fun loadState(state: KeyloggerSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

}
