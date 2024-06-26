plugins {
    alias(libs.plugins.configuration)
    alias(libs.plugins.serialization)
}

configuration {
    internal {
        +libs.bundles.exposed
        +libs.koin.core
        +libs.h2db
    }
}