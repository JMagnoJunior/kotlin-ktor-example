package com.magnojr

import com.fasterxml.jackson.databind.ObjectMapper
import com.magnojr.domain.Restaurant
import com.magnojr.dto.RestaurantExternalDTO
import com.magnojr.repository.Repository
import io.ktor.config.MapApplicationConfig
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ApplicationTest {

    @Test
    fun testRoot() = initialTestContext {
        handleRequest(HttpMethod.Get, "/").apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals("Hello! This endpoints will help you to find the best restaurants.", response.content)
        }
    }

    @Test
    fun testGetEmptyListRestaurants() = initialTestContext {
        handleRequest(HttpMethod.Get, "/restaurants").apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals("[ ]", response.content)
        }
    }

    @Test
    fun testInsertRestaurants() = initialTestContext {
        val dto: RestaurantExternalDTO = RestaurantExternalDTO("new test restaurant", "Mitte")
        handleRequest(HttpMethod.Post, "/restaurants") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(ObjectMapper().writeValueAsString(dto))
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertNotNull(response.content)
        }
    }


    @Test
    fun testUpdateRestaurants() = initialTestContext {
        val newRestaurant : RestaurantExternalDTO = RestaurantExternalDTO(name = "test 1", local = "place 1")
        val id : UUID = runBlocking {  Repository.insert(newRestaurant)  }
        val restaurant : Restaurant = runBlocking {  Repository.getById(id)  }
        assertNull(restaurant.rate)

        val updateRestaurant: RestaurantExternalDTO = RestaurantExternalDTO(name = "test 1", local = "place 1", rate = 5)
        handleRequest(HttpMethod.Put, "/restaurants/${id}") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(ObjectMapper().writeValueAsString(updateRestaurant))
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertNotNull(response.content)
        }
        val restaurantUpdated : Restaurant = runBlocking {  Repository.getById(id)  }
        assertEquals(5, restaurantUpdated.rate)
    }

    private fun initialTestContext(callback: TestApplicationEngine.() -> Unit): Unit {
        runMigrationForTest()
        withTestApplication({
            (environment.config as MapApplicationConfig).apply {
                put("database.jdbcUrl", "jdbc:h2:mem:test")
            }
            this.module(true)
        }, callback)
        cleanDatabase()
    }
}
