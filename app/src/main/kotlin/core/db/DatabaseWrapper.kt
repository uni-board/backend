package core.db

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

abstract class DatabaseWrapper(db: Database, table: Table) {
    init {
        transaction(db) {
            SchemaUtils.create(table)
        }
    }

    protected suspend inline fun <T> dbQuery(crossinline block: suspend () -> T) =
        newSuspendedTransaction(Dispatchers.IO) { block() }

}