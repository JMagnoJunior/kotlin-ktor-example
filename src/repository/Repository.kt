package com.magnojr.repository

import com.magnojr.domain.Restaurant
import com.magnojr.domain.Restaurants
import com.magnojr.dto.RestaurantExternalDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

//TODO: This can be a super class
object Repository {

    private suspend fun <T> sqlCommand(
        block: () -> T
    ): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }

    suspend fun getAll(): List<Restaurant> = sqlCommand {
        Restaurants.selectAll().map { toRestaurant(it) }
    }

    // FIXME : This method has to receive a Restaurant instead RestaurantExternalDTO
    suspend fun insert(newRestaurant: RestaurantExternalDTO): UUID = sqlCommand {
        Restaurants.insert {
            it[name] = newRestaurant.name
            it[local] = newRestaurant.local
            it[rate] = newRestaurant.rate
        } get Restaurants.id
    }

    // FIXME : This method has to receive a Restaurant instead RestaurantExternalDTO
    suspend fun update(id: UUID, updatedRestaurant: RestaurantExternalDTO): Restaurant = sqlCommand {
        Restaurants.update({ Restaurants.id eq id }) {
            it[name] = updatedRestaurant.name
            it[local] = updatedRestaurant.local
            it[rate] = updatedRestaurant.rate
        }
        runBlocking { getById(id) }
    }

    suspend fun getById(id: UUID): Restaurant = sqlCommand {
        Restaurants.select(Op.build { Restaurants.id eq id }).map { toRestaurant(it) }.first()
    }

    suspend fun delete(id: UUID): Unit = sqlCommand {
        Restaurants.deleteIgnoreWhere { Restaurants.id eq id }
    }

    //TODO : Cliente can send this convertion function
    private fun toRestaurant(row: ResultRow): Restaurant =
        Restaurant(
            id = row[Restaurants.id],
            name = row[Restaurants.name],
            local = row[Restaurants.local],
            rate = row[Restaurants.rate]
        )
}