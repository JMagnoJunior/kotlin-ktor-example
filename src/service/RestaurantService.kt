package com.magnojr.service

import com.magnojr.domain.Restaurant
import com.magnojr.domain.Restaurants
import com.magnojr.dto.CreateRestaurantDTO
import com.magnojr.repository.Repository
import org.jetbrains.exposed.sql.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*


object RestaurantService {

    private val logger: Logger = LoggerFactory.getLogger(RestaurantService.javaClass)

    suspend fun getAll(): List<Restaurant> = Repository.sqlCommand {
        logger.debug("Get all restaurants")
        Restaurants.selectAll().map { toRestaurant(it) }
    }

    suspend fun getById(id: UUID): Restaurant = Repository.sqlCommand {
        logger.debug("Get restaurant by id {}", id)
        Restaurants.select(Op.build { Restaurants.id eq id }).map { toRestaurant(it) }.first()
    }

    suspend fun create(newRestaurant: CreateRestaurantDTO): UUID = Repository.sqlCommand {
        logger.info("Creating a new restaurant {}", newRestaurant)
        Restaurants.insert { it[name] = newRestaurant.name } get Restaurants.id
    }

    private fun toRestaurant(row: ResultRow): Restaurant =
        Restaurant(
            id = row[Restaurants.id],
            name = row[Restaurants.name],
            local = row[Restaurants.local],
            rate = row[Restaurants.rate]
        )

}