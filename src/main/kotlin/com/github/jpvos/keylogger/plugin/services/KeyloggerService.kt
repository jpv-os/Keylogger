package com.github.jpvos.keylogger.plugin.services

import com.github.jpvos.keylogger.core.Action
import com.github.jpvos.keylogger.core.Counter
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import java.sql.*


@Service
class KeyloggerService : Counter.Listener {
    val counter = Counter()

    init {
        // TODO: read from storage
        val initialState = Counter.State(
            activeTime = 0,
            idleTime = 0,
            actions = emptyMap()
        )
        counter.setState(initialState)


        Class.forName("org.sqlite.JDBC")
        var connection: Connection? = null
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:main.db")
            val statement: Statement = connection.createStatement()
            statement.setQueryTimeout(30) // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists person")
            statement.executeUpdate("create table person (id integer, name string)")
            statement.executeUpdate("insert into person values(1, 'leo')")
            statement.executeUpdate("insert into person values(2, 'yui')")
            val rs: ResultSet = statement.executeQuery("select * from person")
            while (rs.next()) {
                // read the result set
                thisLogger().warn("name = " + rs.getString("name"))
                thisLogger().warn("id = " + rs.getInt("id"))
            }
        } catch (e: SQLException) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.thisLogger().warn(e.message)
        } finally {
            try {
                if (connection != null) connection.close()
            } catch (e: SQLException) {
                // connection close failed.
                System.err.thisLogger().warn(e.message)
            }
        }
    }

    override fun onAction(action: Action) {
        // TODO: save action/state to storage
    }

}
