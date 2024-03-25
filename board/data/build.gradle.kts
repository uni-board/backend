plugins {
    alias(libs.plugins.configuration)
    alias(libs.plugins.serialization)
}

configuration {
    internal {
        +project(":board:domain")
        +libs.mongodb

        +libs.koin.core
    }
    test {
        +libs.bundles.test
    }
}
