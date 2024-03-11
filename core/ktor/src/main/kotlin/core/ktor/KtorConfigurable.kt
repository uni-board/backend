package core.ktor

import io.ktor.server.application.*
import io.ktor.util.*

interface KtorConfigurable {
    @KtorDsl
    fun Application.configure() {
    }
}

context(Application)
@KtorDsl
fun KtorConfigurable.configure() {
    this@Application.configure()
}
