package com.example.plugins

import com.example.Database
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting(database: Database) {

    // Starting point for a Ktor app:
    routing {
        get("/account") {
            call.respond(database.getAccount())
        }
        get("/market") {
            call.respond(database.getMarket())
        }
        post("/market/deal") {
            val dealParams = call.receive<Database.DealParams>()
            database.makeDeal(dealParams).fold({
                call.respond(HttpStatusCode.OK)
            }, {
                call.respond(HttpStatusCode.BadRequest)
            })
        }
    }

}
