package com.magnojr.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.transaction

object Repository {

    suspend fun <T> sqlCommand(
        block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }

}