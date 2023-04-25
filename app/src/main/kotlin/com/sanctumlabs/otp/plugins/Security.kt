package com.sanctumlabs.otp.plugins

import io.ktor.server.sessions.*
import io.ktor.server.response.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureSecurity(environment: ApplicationEnvironment) {
    data class Session(val count: Int = 0)

    install(Sessions) {
        cookie<Session>("SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    authentication {
        jwt {
            val jwtAudience = environment.config.property("jwt.audience").getString()
            val jwtDomain = environment.config.property("jwt.domain").getString()
            val jwtSecret = environment.config.property("jwt.secret").getString()
            val algorithm = Algorithm.HMAC256(jwtSecret)
            realm = environment.config.property("jwt.realm").getString()
            verifier(
                JWT
                    .require(algorithm)
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
    }

    routing {
        get("/session/increment") {
            val session = call.sessions.get() ?: Session()
            call.sessions.set(session.copy(count = session.count + 1))
            call.respondText("Counter is ${session.count}. Refresh to increment.")
        }
    }
}
