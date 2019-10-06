package com.magnojr.endpoint


import com.magnojr.dto.RestaurantExternalDTO
import com.magnojr.service.RestaurantService
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import java.util.*


fun Route.restaurantEndpoint() {
    route("/restaurants") {

        get() {
            call.respond(RestaurantService.getAll())
        }

        get("/{id}") {
            val id: UUID = UUID.fromString(call.parameters["id"])
            call.respond(RestaurantService.getById(id))
        }

        post() {
            val newRestaurant: RestaurantExternalDTO = call.receive<RestaurantExternalDTO>()
            call.respond(RestaurantService.create(newRestaurant))
        }

        put("/{id}") {
            val id: UUID = UUID.fromString(call.parameters["id"])
            val updatedRestaurant: RestaurantExternalDTO = call.receive<RestaurantExternalDTO>()
            call.respond(RestaurantService.update(id, updatedRestaurant))
        }

        delete("/{id") {
            val id: UUID = UUID.fromString(call.parameters["id"])
            call.respond(HttpStatusCode.Accepted, RestaurantService.delete(id))
        }

    }

}