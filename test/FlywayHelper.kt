package com.magnojr

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.Location

fun runMigrationForTest() {
    // TODO : Move this config to file
    val flyway: Flyway = Flyway.configure()
        .locations(Location.FILESYSTEM_PREFIX + "./resources/db/migration")
        .dataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", null)
        .load();

    flyway.migrate();
}