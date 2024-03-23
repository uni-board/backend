import com.uniboard.board.presentation.board
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.ktor.plugin.Koin

private val modules = listOf<Module>(
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
    board()
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

    routing {
        get("hello") {
            call.respond("Hello")
        }
    }
}