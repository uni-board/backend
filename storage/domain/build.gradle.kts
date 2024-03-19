plugins {
    alias(libs.plugins.configuration)
    alias(libs.plugins.serialization)
}

configuration {
    internal {
    }
    test {
        +projects.core.test
        +libs.bundles.test
    }
}
