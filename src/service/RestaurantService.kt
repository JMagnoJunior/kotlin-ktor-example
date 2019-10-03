package com.magnojr.service

import com.magnojr.domain.Restaurant
import com.magnojr.domain.Restaurants
import com.magnojr.dto.CreateRestaurantDTO
import com.magnojr.repository.Repository
import org.jetbrains.exposed.sql.*
import java.util.*

object RestaurantService {

    suspend fun getAll(): List<Restaurant> = Repository.sqlCommand { Restaurants.selectAll().map { toRestaurant(it) } }

    suspend fun getById(id : UUID): Restaurant = Repository.sqlCommand {
        Restaurants.select( Op.build { Restaurants.id eq id } ).map { toRestaurant(it) }.first()
    }

    suspend fun create(newRestaurant: CreateRestaurantDTO): UUID = Repository.sqlCommand {
         Restaurants.insert { it[name] = newRestaurant.name } get Restaurants.id
    }

    private fun toRestaurant(row: ResultRow) : Restaurant = Restaurant(id = row[Restaurants.id], name = row[Restaurants.name])

}