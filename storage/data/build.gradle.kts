plugins {
    alias(libs.plugins.configuration)
    alias(libs.plugins.serialization)
}

configuration {
    internal {
        +project(":storage:domain")
    }
    test {
        +libs.bundles.test
    }
}
