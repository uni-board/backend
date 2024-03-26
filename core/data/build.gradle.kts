plugins {
    alias(libs.plugins.configuration)
    alias(libs.plugins.serialization)
}
configuration {
    internal {
        +project(":core:domain")
        +libs.koin.core
        +libs.csv
        +libs.coroutines
    }
    test {
        +libs.kotlin.test
        +libs.kotlin.test.junit
        +libs.jupiter
    }
}