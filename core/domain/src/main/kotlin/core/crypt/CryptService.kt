package core.crypt

interface CryptService {
    fun sha512(value: String): String
}