package com.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import java.io.File

fun main(args: Array<String>) {
    val data = jacksonObjectMapper().readValue(File(args[0]).readText(), Data::class.java)
    val database = Database(data)
    val logFilePath = args.getOrNull(1)?.let {
        File(it).apply {
            createNewFile()
        }
    }
    val logger = Logger(logFilePath)

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting(database, logger)
        install(ContentNegotiation) {
            jackson()
        }
    }.start(wait = true)
}

