package com.magnojr

import org.flywaydb.core.Flyway

enum class Actions {
    MIGRATE, CLEAN;

    companion object {
        fun ignoreCaseValueOf(str: String): Actions = Actions.valueOf(str.toUpperCase())
    }
}

data class FlywayConfig(
    val locations: List<String>,
    val jdbcUrl: String,
    val user: String,
    val password: String?
)

fun runFlyway(action: Actions, config: FlywayConfig) {

    val locations: List<String> = config.locations
    val jdbcUrl: String = config.jdbcUrl
    val user: String = config.user

    val flyway = Flyway.configure()
        .locations(*locations.toTypedArray())
        .dataSource(jdbcUrl, user, null)
        .load();

    when (action) {
        Actions.MIGRATE -> flyway.migrate()
        Actions.CLEAN -> flyway.clean()
    }
}