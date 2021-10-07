package routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import models.User
import java.util.*

fun Route.authRouting(
    audience: String,
    issuer: String,
    secret: String
) {

    route("/login") { // defines an authentication route for receiving POST requests
        post {
            val user = call.receive<User>() // receives user credentials sent as a JSON object and converts it to a User class object
            // Check username and password
            // ...
            val token = JWT.create() // generates a token with the specified JWT settings, adds a custom claim with a received username, and signs a token with the specified algorithm: HS256, a shared secret is used to sign a token
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("username", user.username)
                .withExpiresAt(Date(System.currentTimeMillis() + 180000)) // set token expire time to 3 minutes
                .sign(Algorithm.HMAC256(secret))
            call.respond(hashMapOf("token" to token)) // sends a token to a client as a JSON object
        }
    }

    authenticate("auth-jwt") { // define the authorization for the different resources in our application using the authenticate function
        get("/hello") {
            val principal = call.principal<JWTPrincipal>() // In a case of successful authentication, you can retrieve an authenticated JWTPrincipal inside a route handler using the call.principal function
            val username = principal!!.payload.getClaim("username").asString()      // the value of a custom username claim
            val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())  // and a token expiration time are retrieved
            call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
        }
    }

}

fun Application.registerAuthRoutes(
    audience: String,
    issuer: String,
    secret: String
) {
    routing {
        authRouting(audience, issuer, secret)
    }
}