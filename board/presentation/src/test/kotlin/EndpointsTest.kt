import com.uniboard.board.domain.AllBoardsRepository
import com.uniboard.board.presentation.board
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.testing.*
import io.ktor.util.*
import io.ktor.util.logging.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.serialization.json.*
import org.junit.Rule
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.slf4j.event.Level
import kotlin.test.Test
import kotlin.test.assertEquals

class EndpointsTest {
    @Test
    fun createBoardTest() = testApplication {
        val repo: AllBoardsRepository = mockk {
            every { add() } returns 0
        }
        application {
            install(Koin) {
                modules(
                    module {
                        single<AllBoardsRepository> {
                            repo
                        }
                    }
                )
            }
            install(ContentNegotiation) {
                json()
            }
            install(StatusPages) {
                exception { call: ApplicationCall, cause: Throwable ->
                    cause.printStackTrace()
                }
            }
            install(CallLogging) {
                logger = KtorSimpleLogger("TEST")
                level = Level.TRACE
            }
            board()
        }
        val id = client.post("/createboard")
        assertEquals("0", Json.decodeFromString<JsonObject>(id.bodyAsText())["id"]?.jsonPrimitive?.content)
        verify(exactly = 1) { repo.add() }
    }
}