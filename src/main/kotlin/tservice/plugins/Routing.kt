package tservice.plugins

import tservice.dao.UserDAO
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.util.getOrFail

fun Application.configureRouting(dao: UserDAO) {
    install(ContentNegotiation) {
        json()
    }

    routing {
        route("/user") {
            get("/byId/{id}") {
                val id = call.parameters.getOrFail("id").toIntOrNull()
                    ?: throw BadRequestException("user id must be integer!")
                call.respond(dao.getById(id))
            }

            get("/byName/{name}") {
                val name = call.parameters.getOrFail("name")
                if (name.isBlank() || name.isEmpty()) {
                    throw BadRequestException("user name cannot be blank or empty!")
                }

                call.respond(dao.getByName(name))
            }
        }
    }
}
