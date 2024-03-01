package com.github.jpvos.keylogger.plugin.services

import com.github.jpvos.keylogger.core.DatabaseConnection
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service

/**
 * This service offers a database connection to the rest of the application:
 * - automatically connects to the database on creation
 * - automatically disconnects from the database on disposal
 * - can be restored (= closed and reopened) at any time, for example when the database file changes
 */
@Service
class DatabaseService : Disposable {

    /**
     * The database connection, ready to be used by the rest of the application.
     */
    var connection: DatabaseConnection = DatabaseConnection(service<SettingsService>().getDatabaseFilePath())
        private set

    init {
        restoreDatabase()
    }

    /**
     * @see [Disposable.dispose]
     */
    override fun dispose() {
        connection.close()
    }

    /**
     * Close and reopen the database connection.
     */
    fun restoreDatabase() {
        connection.close()
        connection = DatabaseConnection(service<SettingsService>().getDatabaseFilePath())
    }


}
