package core.crypt

object NoEncryptionCryptService: CryptService {
    override fun sha512(value: String): String {
        return value
    }
}