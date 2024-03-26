plugins {
    alias(libs.plugins.configuration)
    alias(libs.plugins.serialization)
}

configuration {
    internal {
        +libs.serialization
    }
    test {
        +libs.bundles.test
    }
}
