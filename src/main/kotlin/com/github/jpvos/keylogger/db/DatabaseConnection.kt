package com.github.jpvos.keylogger.db

import java.sql.*

class DatabaseConnection(url: String) {

    private val defaultQueryTimeout = 30
    private lateinit var connection: Connection

    init {
        connect(url)
    }

    private fun connect(url: String) {
        try {
            // the following line will force-load the org.sqlite.JDBC driver
            // without it, the driver will not be found when running the JAR
            // see https://stackoverflow.com/questions/6740601/what-does-class-fornameorg-sqlite-jdbc-do
            Class.forName("org.sqlite.JDBC")
            // now that the driver is loaded, we can establish a connection
        } catch (e: Exception) {
            throw Error("Error while loading JDBC driver", e)
        }
        try {
            connection = DriverManager.getConnection(url)
        } catch (e: SQLException) {
            // TODO: catch "out of memory" error if DB does not exist
            throw Error("Error while establishing connection to database", e)
        }
    }

    fun <T> statement(body: Statement.() -> T): T {
        try {
            val statement = connection.createStatement()
            statement.queryTimeout = defaultQueryTimeout
            return statement.body()
        } catch (e: SQLException) {
            throw Error("Error while executing statement", e)
        }
    }

    fun preparedStatement(sql: String, body: PreparedStatement.() -> Unit) {
        try {
            val preparedStatement = connection.prepareStatement(sql)
            preparedStatement.queryTimeout = defaultQueryTimeout
            preparedStatement.body()
        } catch (e: SQLException) {
            throw Error("Error while executing prepared statement", e)
        }
    }


    fun close() {
        try {
            connection.close()
        } catch (e: SQLException) {
            throw Error("Error while closing connection to database", e)
        }
    }

}
