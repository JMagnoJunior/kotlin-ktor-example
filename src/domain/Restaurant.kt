package com.magnojr.domain

import org.jetbrains.exposed.sql.Table
import java.util.*

object Restaurants: Table() {
     val id = uuid("id" ).primaryKey().default(UUID.randomUUID())
     val name = varchar("name", length =  50)
}

data class Restaurant(
    val id: UUID,
    val name: String
)
