package com.magnojr

import com.fasterxml.jackson.databind.SerializationFeature
import com.magnojr.config.DatabaseConfig
import com.magnojr.endpoint.restaurantEndpoint
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.jackson.jackson
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    DatabaseConfig().apply {
        val databaseConfig = environment.config.config("database")
        setupHikary(databaseConfig)
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        allowCredentials = true
        anyHost()
    }

    install(CallLogging) {
        level = Level.DEBUG
    }

    install(Routing) {

        get("/") {
            call.respondText(
                "Hello! This endpoints will help you to find the best restaurants.",
                contentType = ContentType.Text.Plain
            )
        }

        restaurantEndpoint()
    }
}

