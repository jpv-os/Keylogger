package com.github.jpvos.keylogger.plugin

import com.intellij.DynamicBundle
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey

/**
 * The resource bundle for the Keylogger plugin.

 */
@NonNls
private const val BUNDLE = "messages.KeyloggerBundle"

/**
 * The object to access the resource bundle for the Keylogger plugin.
 */
internal object KeyloggerBundle : DynamicBundle(BUNDLE) {
    /**
     * @see DynamicBundle.message
     */
    @JvmStatic
    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any) =
        getMessage(key, *params)
}
