plugins {
    alias(libs.plugins.configuration)
    alias(libs.plugins.serialization)
}

configuration {
    internal {
        +project(":board:domain")
        +libs.bundles.ktor
        +libs.socketio
        +libs.koin.ktor
    }
    test {
        +libs.bundles.test
    }
}