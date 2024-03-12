package core.crypt

import core.crypt.impl.CryptServiceImpl
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CryptServiceTest {
    private lateinit var service: CryptService

    @BeforeTest
    fun setup() {
        service = CryptServiceImpl()
    }

    @Test
    fun sha512Test() {
        val given = "Hello, World!"
        val expected =
            "374d794a95cdcfd8b35993185fef9ba368f160d8daf432d08ba9f1ed1e5abe6cc69291e0fa2fe0006a52570ef18c19def4e617c33ce52ef0a6e5fbe318cb0387"

        assertEquals(expected, service.sha512(given))
    }
}