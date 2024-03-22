plugins {
    alias(libs.plugins.configuration)
    alias(libs.plugins.serialization)
}

configuration {
    internal {
        +project(":board:domain")
    }
    test {
        +libs.bundles.test
    }
}
