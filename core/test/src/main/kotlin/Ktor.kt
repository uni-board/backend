import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import io.ktor.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.ktor.plugin.Koin

@KtorDsl
fun testApp(
    app: Application.() -> Unit,
    vararg modules: Module,
    configure: suspend ApplicationTestBuilder.() -> Unit
) {
    testApplication {
        application {
            install(Koin) {
                modules(*modules)
            }
            install(ContentNegotiation) {
                json()
            }
            app()
        }
        configure()
    }
}

inline fun <reified T : Any> HttpRequestBuilder.body(value: T) {
    setBody(Json.encodeToString(value))
    contentType(ContentType.Application.Json)
}