package auth.data

import auth.domain.RawUser
import auth.domain.User
import auth.domain.UsersRepository
import core.crypt.CryptService
import core.db.DatabaseWrapper
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

object Users : Table() {
    val id = long("id").autoIncrement()
    val name = varchar("name", length = 50)
    val password = varchar("password", length = 1024)

    override val primaryKey = PrimaryKey(id)
}

class UsersRepositoryImpl(db: Database, private val cryptService: CryptService) : DatabaseWrapper(db, Users),
    UsersRepository {

    override suspend fun getById(id: Long): User? = dbQuery {
        Users.selectAll()
            .where { Users.id eq id }
            .map { User(it[Users.id], it[Users.name], it[Users.password]) }
            .singleOrNull()
    }

    override suspend fun getByName(name: String): User? = dbQuery {
        Users.selectAll()
            .where {
                Users.name eq name
            }
            .toList()
            .singleOrNull()
            ?.let {
                User(it[Users.id], it[Users.name], it[Users.password])
            }
    }

    override suspend fun create(user: RawUser): Long? = dbQuery {
        val previousUser = getByName(user.name)
        if (previousUser != null) return@dbQuery null

        Users.insert {
            it[name] = user.name
            it[password] = cryptService.sha512(user.password)
        }[Users.id]
    }

    override suspend fun update(id: Long, password: String) {
        dbQuery {
            Users.update({ Users.id eq id }) {
                it[Users.password] = cryptService.sha512(password)
            }
        }
    }

    override suspend fun delete(id: Long) {
        dbQuery {
            Users.deleteWhere { Users.id eq id }
        }
    }
}