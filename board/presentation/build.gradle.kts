plugins {
    alias(libs.plugins.configuration)
    alias(libs.plugins.serialization)
}

configuration {
    internal {
        +project(":board:domain")
        +libs.ktor.websockets
        +libs.bundles.ktor
        +libs.socketio
        +libs.javax.servlet
        +libs.koin.ktor
    }
    test {
        +libs.bundles.test
    }
}