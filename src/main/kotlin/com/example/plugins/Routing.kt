package com.example.plugins

import com.example.Database
import com.example.Logger
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting(database: Database, logger: Logger) {

    // Starting point for a Ktor app:
    routing {
        get("/account") {
            logger.log("GET /account")
            call.respond(database.getAccount())
        }
        get("/market") {
            logger.log("GET /market")
            call.respond(database.getMarket())
        }
        post("/market/deal") {
            logger.log("POST /market/deal")
            val dealParams = call.receive<Database.DealParams>()
            logger.log("POST /market/deal body: $dealParams")
            database.makeDeal(dealParams).fold({
                logger.log("POST /market/deal response: Ok")
                call.respond(HttpStatusCode.OK)
            }, {
                logger.log("POST /market/deal response: BadRequest - ${it.message}")
                call.respond(HttpStatusCode.BadRequest)
            })
        }
    }

}
