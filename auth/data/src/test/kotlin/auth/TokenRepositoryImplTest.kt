package auth

import auth.data.TokenRepositoryImpl
import auth.domain.TokenRepository
import auth.domain.UsersRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test

class TokenRepositoryImplTest {
    private lateinit var repo: TokenRepository
    private lateinit var usersRepo: UsersRepository
    @BeforeTest
    fun setup() {
        usersRepo = mockk()
        repo = TokenRepositoryImpl(usersRepo)
    }

    @Test
    fun generateTokenTest() = runBlocking {
        val token = repo.generateToken(1)
    }
}