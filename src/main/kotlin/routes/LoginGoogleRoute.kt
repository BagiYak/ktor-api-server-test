package routes

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.html.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.p
import models.UserInfo
import models.UserSession

fun Route.loginGoogleRoute(httpClient: HttpClient) {

    route("/")  {

        authenticate("auth-oauth-google") {
            get("/login") {
                // Redirects to 'authorizeUrl' automatically
            }

            get("/callback") {
                val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
                call.sessions.set(UserSession(principal?.accessToken.toString()))
                call.respondRedirect("/hello")
            }
        }

        get{
            call.respondHtml {
                body {
                    p {
                        a("/login") { +"Login with Google" }
                    }
                }
            }
        }
        get("/hello") {
            val userSession: UserSession? = call.sessions.get<UserSession>()
            if (userSession != null) {
                val userInfo: UserInfo = httpClient.get("https://www.googleapis.com/oauth2/v2/userinfo") {
                    headers {
                        append(io.ktor.http.HttpHeaders.Authorization, "Bearer ${userSession.token}")
                    }
                }
                call.respondText("Hello, ${userInfo.name}!")
            } else {
                call.respondRedirect("/")
            }
        }
    }

}

fun Application.loginGoogleRoute(httpClient: HttpClient) {
    routing {
       loginGoogleRoute(httpClient)
    }
}