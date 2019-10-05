package com.magnojr

import io.ktor.config.MapApplicationConfig
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun testRoot() = initialTestContext {
        handleRequest(HttpMethod.Get, "/").apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals("Hello! This endpoints will help you to find the best restaurants.", response.content)
        }
    }

    @Test
    fun testSomething() = initialTestContext {
        handleRequest(HttpMethod.Get, "/restaurants").apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals("[ ]", response.content)
        }
    }

    private fun initialTestContext(callback: TestApplicationEngine.() -> Unit): Unit {
        runMigrationForTest()
        withTestApplication({
            (environment.config as MapApplicationConfig).apply {
                put("database.jdbcUrl", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")

            }
            this.module()
        }, callback)

    }

}
