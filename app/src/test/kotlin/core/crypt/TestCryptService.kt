package core.crypt

fun testCryptService(): CryptService = object : CryptService {
    override fun sha512(value: String): String {
        return value
    }
}