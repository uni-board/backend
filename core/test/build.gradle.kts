import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.exposed.core)
    implementation(libs.ktor.test)
    implementation(libs.kotlin.test)
    implementation(libs.junit)
    implementation(libs.jupiter)
}
kotlin {
    jvmToolchain(21)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
    }
}