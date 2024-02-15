package com.github.jpvos.keylogger.db

import java.sql.*

class SqliteDatabaseConnection {

    private val defaultQueryTimeout = 30
    private var connection: Connection? = null

    val open: Boolean
        get() = !(connection?.isClosed ?: true)

    fun connect(url: String) {
        try {
            Class.forName("org.sqlite.JDBC")
        } catch (e: ClassNotFoundException) {
            throw Error("Error while loading JDBC driver", e)
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:file:${url}")
        } catch (e: SQLException) {
            throw Error("Error while establishing connection to database", e)
        }
    }

    fun <T> query(sql: String, body: ResultSet.() -> T): T {
        try {
            val statement = getConnection().createStatement()
            statement.queryTimeout = defaultQueryTimeout
            val resultSet = statement.executeQuery(sql)
            return resultSet.body()
        } catch (e: SQLException) {
            throw Error("Error while executing query", e)
        }
    }

    fun statement(sql: String) {
        try {
            val statement = getConnection().createStatement()
            statement.queryTimeout = defaultQueryTimeout
            statement.execute(sql)
        } catch (e: SQLException) {
            throw Error("Error while executing statement", e)
        }
    }

    fun preparedStatement(sql: String, body: PreparedStatement.() -> Unit) {
        try {
            val preparedStatement = getConnection().prepareStatement(sql)
            preparedStatement.queryTimeout = defaultQueryTimeout
            preparedStatement.body()
            preparedStatement.execute()
        } catch (e: SQLException) {
            throw Error("Error while executing prepared statement", e)
        }
    }


    fun close() {
        val con = connection
        if (con == null || con.isClosed)
            return
        try {
            con.close()
        } catch (e: SQLException) {
            throw Error("Error while closing connection to database", e)
        }
    }

    private fun getConnection(): Connection {
        return connection ?: throw Error("No connection to database")
    }

}
