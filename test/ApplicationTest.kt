package com.magnojr

import com.fasterxml.jackson.databind.ObjectMapper
import com.magnojr.domain.Restaurant
import com.magnojr.dto.RestaurantReceiverDTO
import com.magnojr.repository.RestaurantRepository
import io.ktor.config.MapApplicationConfig
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.runBlocking
import org.flywaydb.core.api.Location
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ApplicationTest {

    @KtorExperimentalAPI
    @Test
    fun testRoot() = initialTestContext {
        handleRequest(HttpMethod.Get, "/").apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals("Hello! This endpoints will help you to find the best restaurants.", response.content)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun testGetEmptyListRestaurants() = initialTestContext {
        handleRequest(HttpMethod.Get, "/restaurants").apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals("[ ]", response.content)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun testInsertRestaurants() = initialTestContext {
        val dto = RestaurantReceiverDTO("new test restaurant", "Mitte")
        handleRequest(HttpMethod.Post, "/restaurants") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(ObjectMapper().writeValueAsString(dto))
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertNotNull(response.content)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun testUpdateRestaurants() = initialTestContext {
        val newRestaurant = Restaurant(name = "test 1", local = "place 1")
        val id: UUID = runBlocking { RestaurantRepository.insert(newRestaurant) }
        val restaurant: Restaurant = runBlocking { RestaurantRepository.getById(id) }
        assertNull(restaurant.rate)

        val updateRestaurant =
            RestaurantReceiverDTO(name = "test 1", local = "place 1", rate = 5)
        handleRequest(HttpMethod.Put, "/restaurants/${id}") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(ObjectMapper().writeValueAsString(updateRestaurant))
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertNotNull(response.content)
        }
        val restaurantUpdated: Restaurant = runBlocking { RestaurantRepository.getById(id) }
        assertEquals(5, restaurantUpdated.rate)
    }

    @KtorExperimentalAPI
    private fun initialTestContext(callback: TestApplicationEngine.() -> Unit) {

        // please, let me know if you find a better way to

        val config = FlywayConfig(
            locations = listOf(Location.FILESYSTEM_PREFIX + "./resources/db/migration"),
            jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            user = "sa",
            password = null
        )

        withTestApplication({
            (environment.config as MapApplicationConfig).apply {
                put("database.jdbcUrl", config.jdbcUrl)
                put("database.user", config.user)
                put("database.flyway.location", config.locations)
            }
            runFlyway(Actions.MIGRATE, config)
            this.module(true)
        }, callback)
        runFlyway(Actions.CLEAN, config)
    }
}
