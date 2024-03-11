package auth

import auth.data.RawUser
import auth.data.Users
import auth.data.UsersRepositoryImpl
import auth.domain.UsersRepository
import core.crypt.testCryptService
import core.db.TestDatabase
import kotlinx.coroutines.runBlocking
import kotlin.test.*

class UsersRepositoryImplTest: TestDatabase(Users) {
    private lateinit var repo: UsersRepository
    private lateinit var user1: RawUser
    private lateinit var user2: RawUser

    @BeforeTest
    fun setup() {
        repo = UsersRepositoryImpl(db, testCryptService())

        user1 = RawUser("jogn", "can")
        user2 = RawUser("aklgljakjga", "aklgjalkgjk")
    }

    @Test
    fun createUserTest() = runBlocking {
        repo.create(user1)
        val dbUser = repo.getByName(user1.name)

        assertEquals(user1.name, dbUser?.name)
        assertEquals(user1.password, dbUser?.password)
    }

    @Test
    fun getByNameTest() = runBlocking {
        repo.create(user1)
        val id = repo.getByName(user1.name)!!.id
        val user = repo.getById(id)

        assertEquals(user1.name, user?.name)
        assertEquals(user1.password, user?.password)
    }
    @Test
    fun deleteUserTest() = runBlocking {
        repo.create(user1)

        val id = repo.getByName(user1.name)?.id
        assertNotNull(id)
        repo.delete(id)

        assertNull(repo.getById(id))
    }

    @Test
    fun multipleSameUsersTest() = runBlocking {
        repo.create(user1)
        val second = repo.create(user1)
        assertNull(second)
    }

    @Test
    fun multipleUsersTest() = runBlocking {
        val id1 = repo.create(user1)
        val id2 = repo.create(user2)

        assertNotNull(id1)
        assertNotNull(id2)
        assertNotEquals(repo.getById(id1), repo.getById(id2))
    }

    @Test
    fun updateUserTest() = runBlocking {
        repo.create(user1)

        val id = repo.getByName(user1.name)?.id
        assertNotNull(id)
        repo.update(id, "hello")
        val password = repo.getByName(user1.name)?.password

        assertEquals("hello", password)
    }
}