package core.ktor

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import main

@KtorDsl
fun testApp(configure: suspend ApplicationTestBuilder.() -> Unit) {
    testApplication {
        application {
            main()
        }
        configure()
    }
}

inline fun <reified T : Any> HttpRequestBuilder.body(value: T) {
    setBody(Json.encodeToString(value))
    contentType(ContentType.Application.Json)
}