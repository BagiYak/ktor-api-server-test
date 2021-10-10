package routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureRouting() {

    routing {
        get("/hi") {
            call.respondText("Hello World & BagiYak!", status = HttpStatusCode.OK)
        }
    }
}

