package com.magnojr.endpoint


import com.magnojr.dto.CreateRestaurantDTO
import com.magnojr.service.RestaurantService
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import java.util.*


fun Route.restaurantEndpoint() {
    route("/restaurant") {

        get() {
            call.respond(RestaurantService.getAll())
        }

        get("/{id}") {
            val id : UUID =  UUID.fromString(call.parameters["id"])
            call.respond(RestaurantService.getById(id))
        }

        post() {
            val newRestaurant : CreateRestaurantDTO = call.receive<CreateRestaurantDTO>()
            call.respond(RestaurantService.create(newRestaurant))
        }

    }

}