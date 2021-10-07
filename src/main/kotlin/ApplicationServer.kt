import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.*
import routes.registerCustomerRoutes
import routes.registerOrderRoutes
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.serialization.*
import routes.configureRouting
import routes.registerAuthRoutes

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {

    // to open in browsers
    install(CORS) {
        anyHost()
    }

    // to accept application/json contentType request via http
    install(ContentNegotiation) {
        json()
    }

    // Configure JWT settings (a custom jwt group in the application.conf)
    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()
    install(Authentication) {
        jwt("auth-jwt") {
            realm = myRealm // The realm property allows you to set the realm to be passed in WWW-Authenticate header when accessing a protected route
            verifier( // The verifier function allows you to verify a token format and its signature: HS256, you need to pass a JWTVerifier instance to verify a token
                JWT
                .require(Algorithm.HMAC256(secret))
                .withAudience(audience)
                .withIssuer(issuer)
                .build()
            )
            validate { credential -> // The validate function allows you to perform additional validations on the JWT payload in the following way
                if (credential.payload.getClaim("username").asString() != "") { // Check the credential parameter, which represents a JWTCredential object and contains the JWT payload. In the example below, the value of a custom username claim is checked.
                    JWTPrincipal(credential.payload) // In a case of successful authentication, return JWTPrincipal. If authentication fails, return null
                } else {
                    null
                }
            }
        }
    }

    configureRouting()
    registerAuthRoutes(audience, issuer, secret)
    registerCustomerRoutes()
    registerOrderRoutes()

}