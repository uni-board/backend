plugins {
    alias(libs.plugins.configuration)
    alias(libs.plugins.serialization)
}

configuration {
    internal {
    }
    test {
        +libs.bundles.test
    }
}
