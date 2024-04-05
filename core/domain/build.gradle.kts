plugins {
    alias(libs.plugins.configuration)
    alias(libs.plugins.serialization)
}

configuration {
    internal {
        +libs.koin.core
    }
}