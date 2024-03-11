package auth.data

import auth.domain.TokenRepository
import auth.domain.UsersRepository
import org.koin.dsl.module

val authModule = module {
    single<UsersRepository> { UsersRepositoryImpl(get(), get()) }
    single<TokenRepository> { TokenRepositoryImpl(get()) }
}