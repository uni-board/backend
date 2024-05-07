plugins {
    alias(libs.plugins.configuration)
    alias(libs.plugins.serialization)
}

configuration {
    internal {
        +project(":core:domain")
        +project(":pdf:domain")
        +libs.koin.core
        +libs.pdfbox
        +libs.pdfbox.io
        +libs.coroutines
    }
    test {
        +libs.bundles.test
    }
}
