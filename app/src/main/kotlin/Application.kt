import auth.data.auth
import auth.data.authModule
import core.coreModule
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.ktor.plugin.Koin

private val modules = listOf(
    coreModule,
    authModule
)

fun main() {
    startKoin {
        modules(modules)
    }
    embeddedServer(Netty, port = 8080) {
        main()
    }.start(wait = true)
}

fun Application.main() {
    installPlugins()
    auth()
}

private fun Application.installPlugins() {
    install(Koin) {
        modules(modules)
    }
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        })
    }
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            cause.printStackTrace()
        }
    }

    /*install(CallLogging) {
        level = org.slf4j.event.Level.DEBUG
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]
            "Status: $status, HTTP method: $httpMethod, User agent: $userAgent"
        }
    }*/
}