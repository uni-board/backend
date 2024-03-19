plugins {
    alias(libs.plugins.configuration)
    alias(libs.plugins.serialization)
}

configuration {
    internal {
        +libs.koin.core
        +libs.koin.ktor
        +libs.ktor.core
        +libs.ktor.auth
        +libs.ktor.auth.jwt
        +projects.auth.domain
        +projects.core.ktor
    }
    test {
        +projects.core.test
        +libs.bundles.test
    }
}
