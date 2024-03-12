package core.db

import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

val dbModule = module {
    single { Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver") }
}