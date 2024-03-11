package core

import core.crypt.CryptService
import core.crypt.impl.CryptServiceImpl
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

val coreModule = module {
    single<Database> {
        Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
    }
    single<CryptService>{ CryptServiceImpl() }
}