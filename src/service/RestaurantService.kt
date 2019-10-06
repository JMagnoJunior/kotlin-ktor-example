package com.magnojr.service

import com.magnojr.domain.Restaurant
import com.magnojr.dto.RestaurantExternalDTO
import com.magnojr.repository.Repository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*


object RestaurantService {

    private val logger: Logger = LoggerFactory.getLogger(RestaurantService.javaClass)

    suspend fun getAll(): List<Restaurant> {
        logger.debug("Get all restaurants")
        return Repository.getAll()
    }

    suspend fun getById(id: UUID): Restaurant {
        logger.debug("Get restaurant by id {}", id)
        return Repository.getById(id)
    }

    suspend fun update(id: UUID, updatetRestaurant: RestaurantExternalDTO): Restaurant {
        logger.info("Updating a new restaurant {}", updatetRestaurant)
        return Repository.update(id, updatetRestaurant)
    }

    suspend fun create(newRestaurant: RestaurantExternalDTO): UUID {
        logger.info("Creating a new restaurant {}", newRestaurant)
        return Repository.insert(newRestaurant)
    }

    suspend fun delete(id: UUID): Unit {
        logger.info("Removing the restaurant {}", id)
        return Repository.delete(id)
    }

}