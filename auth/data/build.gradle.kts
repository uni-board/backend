import java.util.*

plugins {
    alias(libs.plugins.configuration)
    alias(libs.plugins.serialization)
    alias(libs.plugins.buildconfig)
}

configuration {
    external {
        +libs.bundles.ktor
        +libs.bundles.exposed
        +libs.serialization
        +libs.koin.core
        +libs.koin.ktor
        +projects.core.db
        +projects.core.domain
        +projects.core.ktor
        +projects.auth.domain
    }
    test {
        +projects.core.test
        +libs.bundles.test
        +projects.core.data
    }
}

buildConfig {
    val properties = Properties().apply {
        load(File(rootDir, "local.properties").bufferedReader())
    }
    buildConfigField("SIGNIN_AUDIENCE", properties.getProperty("SIGNIN_AUDIENCE"))
    buildConfigField("SIGNIN_ISSUER", properties.getProperty("SIGNIN_ISSUER"))
    buildConfigField("SIGNIN_EXPIRATION_TIME", properties.getProperty("SIGNIN_EXPIRATION_TIME").toLong())
    buildConfigField("SIGNIN_SECRET", properties.getProperty("SIGNIN_SECRET"))
}