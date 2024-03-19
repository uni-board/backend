plugins {
    alias(libs.plugins.configuration)
    alias(libs.plugins.serialization)
}

configuration {
    internal {
        +projects.core.domain
        +libs.exposed.core
        +libs.ktor.test
        +libs.kotlin.test
        +libs.kotlin.test.junit
        +libs.jupiter
        +libs.serialization
        +libs.koin.ktor
        +libs.ktor.negotiation
        +libs.ktor.serialization
    }
}