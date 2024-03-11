package auth.domain

import auth.data.RawUser
import auth.domain.User

interface UsersRepository {
    suspend fun getById(id: Long): User?
    suspend fun getByName(name: String): User?

    suspend fun create(user: RawUser): Long?

    suspend fun update(id: Long, password: String)

    suspend fun delete(id: Long)
}