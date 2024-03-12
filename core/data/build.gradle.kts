plugins {
    alias(libs.plugins.configuration)
    alias(libs.plugins.serialization)
}
configuration {
    internal {
        +projects.core.domain
        +libs.koin.core
    }
    test {
        +libs.kotlin.test
        +libs.kotlin.test.junit
        +libs.jupiter
    }
}