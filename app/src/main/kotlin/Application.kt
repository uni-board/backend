import com.uniboard.board.data.boardModule
import com.uniboard.board.presentation.board
import com.uniboard.board.presentation.boardSocketServer
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.logging.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.ktor.plugin.Koin
import org.slf4j.event.Level

private val modules = listOf(
    boardModule
)

fun main() {
    startKoin {
        modules(modules)
    }
    val coroutineScope = CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { coroutineContext, throwable ->
        throwable.printStackTrace()
    })
    coroutineScope.boardSocketServer()

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
    install(CallLogging) {
        logger = KtorSimpleLogger("Backend")
        level = Level.INFO
    }
    install(CORS) {
        anyHost()
    }

    routing {
        get("hello") {
            call.respondText("Hello")
        }
    }
}