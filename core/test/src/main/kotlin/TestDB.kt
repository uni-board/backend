import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.TestInstance
import kotlin.test.BeforeTest

fun testDatabase(): Database =
    Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class TestDatabase(private vararg val tables: Table){
    protected val db = testDatabase()

    @BeforeTest
    fun dbSetup() {
        transaction(db) {
            SchemaUtils.drop(*tables)
            SchemaUtils.create(*tables)
        }
    }
}