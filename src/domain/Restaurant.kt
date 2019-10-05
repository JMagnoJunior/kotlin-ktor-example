package com.magnojr.domain

import org.jetbrains.exposed.sql.Table
import java.util.*

object Restaurants : Table() {
    val id = uuid("id").primaryKey().default(UUID.randomUUID())
    val name = varchar("name", length = 100)
    val local = varchar("local", length = 100)
    val rate = integer("rate")
}

data class Restaurant(
    val id: UUID,
    val name: String,
    val local: String,
    val rate: Int?
)
