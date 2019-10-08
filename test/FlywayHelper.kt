package com.magnojr

import io.ktor.config.ApplicationConfig
import io.ktor.server.testing.withTestApplication
import org.flywaydb.core.Flyway

class FlywayHelper {

    var dbConfig: ApplicationConfig? = null

    fun runMigrationForTest(config: ApplicationConfig) {
        val locations: List<String> = config.property("flyway.location").getList().map { it }
        val flyway: Flyway = Flyway.configure()
            .locations(*locations.toTypedArray())
            .dataSource(
                config?.property("jdbcUrl").getString(),
                config?.property("user").getString(),
                null
            )
            .load();
        flyway.migrate();
    }

    fun cleanDatabase() {
        withTestApplication {
            val flyway: Flyway = Flyway.configure()
                .dataSource(
                    dbConfig?.property("jdbcUrl")?.getString(),
                    dbConfig?.property("user")?.getString(),
                    null
                )
                .load();
            flyway.clean();
        }
    }

}