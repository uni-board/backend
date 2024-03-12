package auth

import TestDatabase
import auth.data.Users
import auth.data.auth
import auth.domain.RawUser
import body
import core.db.dbModule
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import testApp
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

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
    fun signupTest() = testApp(Application::auth, dbModule) {
        val response = createUser()
        assertEquals(HttpStatusCode.Created, response.status)
        assertNotNull(response.bodyAsText().toLongOrNull())
    }

    @Test
    fun signinTest() = testApp(Application::auth, dbModule) {
        createUser()
        val response = signIn()
        assertEquals(HttpStatusCode.OK, response.status)
        assert(response.bodyAsText().isNotBlank())
    }

    @Test
    fun tokenValidTest() = testApp(Application::auth, dbModule) {
        createUser()
        val token = signIn().bodyAsText()
        val response = client.get("/api/hello") {
            bearerAuth(token)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assert(response.bodyAsText().isNotBlank())
    }

    @Test
    fun tokenInvalidTest() = testApp(Application::auth, dbModule) {
        createUser()
        signIn()
        val response = client.get("/api/hello") {
            bearerAuth("kajgkajgk")
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun tokenExpiredTest() = testApp(Application::auth, dbModule) {
        createUser()
        val token = signIn().bodyAsText()
        val response = client.get("/api/hello") {
            bearerAuth(token)
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }
}