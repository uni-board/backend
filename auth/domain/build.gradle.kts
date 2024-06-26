plugins {
    alias(libs.plugins.configuration)
    alias(libs.plugins.serialization)
}

configuration {
    internal {
        +libs.koin.ktor
        +libs.bundles.ktor
        +projects.core.ktor
    }
}