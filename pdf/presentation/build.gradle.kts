plugins {
    alias(libs.plugins.configuration)
    alias(libs.plugins.serialization)
}

configuration {
    internal {
        +libs.bundles.ktor
        +libs.koin.core
        +libs.koin.ktor
        +project(":storage:domain")
        +project(":pdf:domain")
    }
    test {
        +libs.bundles.test
    }
}
