package auth.data

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import auth.domain.TokenRepository
import auth.domain.UsersRepository
import backend.app.BuildConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*


class TokenRepositoryImpl(private val usersRepository: UsersRepository) : TokenRepository {
    override suspend fun generateToken(userId: Long): String {
        return requireNotNull(
            JWT.create()
                .withAudience(BuildConfig.SIGNIN_AUDIENCE)
                .withIssuer(BuildConfig.SIGNIN_ISSUER)
                .withClaim("userid", userId.toString())
                .withExpiresAt(java.util.Date(System.currentTimeMillis() + BuildConfig.SIGNIN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(BuildConfig.SIGNIN_SECRET))
        )
    }

    override fun Application.configure() {
        install(Authentication) {
            jwt("auth-jwt") {
                realm = "Access to Uniboard"
                verifier(
                    JWT.require(Algorithm.HMAC256(BuildConfig.SIGNIN_SECRET))
                        .withAudience(BuildConfig.SIGNIN_AUDIENCE)
                        .withIssuer(BuildConfig.SIGNIN_ISSUER)
                        .build()
                )
                validate { credential ->
                    val id = credential.payload.getClaim("userid").asString().toLongOrNull()
                    if (id != null && usersRepository.getById(id) != null)
                        JWTPrincipal(credential.payload)
                    else null
                }
            }
        }
    }
}