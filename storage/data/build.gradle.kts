plugins {
    alias(libs.plugins.configuration)
    alias(libs.plugins.serialization)
}

configuration {
    internal {
        +project(":storage:domain")
        +project(":core:domain")
        +libs.koin.core
        +libs.mongodb
    }
    test {
        +libs.bundles.test
    }
}
