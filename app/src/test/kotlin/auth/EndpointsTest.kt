package auth

import auth.data.RawUser
import auth.data.Users
import core.db.TestDatabase
import core.ktor.body
import core.ktor.testApp
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class EndpointsTest : TestDatabase(Users) {
    private lateinit var user: RawUser

    @BeforeTest
    fun setup() {
        user = RawUser("jogn", "can")
    }

    private suspend fun ApplicationTestBuilder.createUser() =
        client.post("/api/signup") {
            body(user)
        }

    private suspend fun ApplicationTestBuilder.signIn() =
        client.post("/api/signin") {
            body(user)
        }

    @Test
    fun signupTest() = testApp {
        val response = createUser()
        assertEquals(HttpStatusCode.Created, response.status)
        assertNotNull(response.bodyAsText().toLongOrNull())
    }

    @Test
    fun signinTest() = testApp {
        createUser()
        val response = signIn()
        assertEquals(HttpStatusCode.OK, response.status)
        assert(response.bodyAsText().isNotBlank())
    }

    @Test
    fun tokenValidTest() = testApp {
        createUser()
        val token = signIn().bodyAsText()
        val response = client.get("/api/hello") {
            bearerAuth(token)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assert(response.bodyAsText().isNotBlank())
    }

    @Test
    fun tokenInvalidTest() = testApp {
        createUser()
        signIn()
        val response = client.get("/api/hello") {
            bearerAuth("kajgkajgk")
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun tokenExpiredTest() = testApp {
        createUser()
        val token = signIn().bodyAsText()
        val response = client.get("/api/hello") {
            bearerAuth(token)
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }
}