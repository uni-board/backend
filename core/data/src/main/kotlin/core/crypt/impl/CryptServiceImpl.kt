package core.crypt.impl

import core.crypt.CryptService
import java.math.BigInteger
import java.security.MessageDigest

class CryptServiceImpl: CryptService {

    // https://stackoverflow.com/a/66733767/15061682
    override fun sha512(value: String): String {
        val md = MessageDigest.getInstance("SHA-512")
        val digest = md.digest(value.toByteArray())
        val no = BigInteger(1, digest)

        var hashtext: String = no.toString(16)
        while (hashtext.length < 128) {
            hashtext = "0$hashtext"
        }
        return hashtext
    }
}