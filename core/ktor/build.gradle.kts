plugins {
    alias(libs.plugins.configuration)
    alias(libs.plugins.serialization)
}

configuration {
    internal {
        +libs.koin.core
        +libs.ktor.core
        +libs.ktor.auth
        +libs.ktor.auth.jwt
    }
}
