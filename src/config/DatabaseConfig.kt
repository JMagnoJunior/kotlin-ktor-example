package com.magnojr.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.config.ApplicationConfig
import org.jetbrains.exposed.sql.Database
import java.sql.Connection


class DatabaseConfig {

    fun setupHikary(config: ApplicationConfig) {
        Database.connect(hikari(config))
    }

    private fun hikari(config: ApplicationConfig): HikariDataSource {

        val jdbcUrl: String = config.property("jdbcUrl").getString()
        val driverClassName: String = config.propertyOrNull("driverClassName")?.getString() ?: "org.h2.Driver"
        val username: String = config.propertyOrNull("username")?.getString() ?: "sa"
        val maximumPoolSize: Int = config.propertyOrNull("maximumPoolSize")?.getString()?.toInt() ?: 3
        val isAutoCommit: Boolean = config.propertyOrNull("isAutoCommit")?.getString()?.toBoolean() ?: false
        val transactionIsolation: String =
            config.propertyOrNull("transactionIsolation")?.getString() ?: "TRANSACTION_REPEATABLE_READ"

        val config = HikariConfig()
        config.driverClassName = driverClassName
        config.jdbcUrl = jdbcUrl
        config.username = username
        config.maximumPoolSize = maximumPoolSize
        config.isAutoCommit = isAutoCommit
        config.transactionIsolation = transactionIsolation
        config.validate()
        return HikariDataSource(config)
    }


}
