package com.magnojr.service

import com.magnojr.converter.ConverterService
import com.magnojr.domain.Restaurant
import com.magnojr.dto.RestaurantReceiverDTO
import com.magnojr.repository.RestaurantRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*


object RestaurantService {

    private val logger: Logger = LoggerFactory.getLogger(RestaurantService.javaClass)

    suspend fun getAll(): List<Restaurant> {
        logger.debug("Get all restaurants")
        return RestaurantRepository.getAll()
    }

    suspend fun getById(id: UUID): Restaurant {
        logger.debug("Get restaurant by id {}", id)
        return RestaurantRepository.getById(id)
    }

    suspend fun update(id: UUID, updateRestaurant: RestaurantReceiverDTO): Restaurant {
        logger.info("Updating a new restaurant {}", updateRestaurant)
        val restaurant: Restaurant = ConverterService.fromDTOToDomain(updateRestaurant)
        return RestaurantRepository.update(id, restaurant)
    }

    suspend fun create(newRestaurant: RestaurantReceiverDTO): UUID {
        logger.info("Creating a new restaurant {}", newRestaurant)
        val restaurant: Restaurant = ConverterService.fromDTOToDomain(newRestaurant)
        return RestaurantRepository.insert(restaurant)
    }

    suspend fun delete(id: UUID): Unit {
        logger.info("Removing the restaurant {}", id)
        return RestaurantRepository.delete(id)
    }

}