import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.serialization)
    alias(libs.plugins.buildconfig)
}

dependencies {
    implementation(projects.core.domain)

    implementation(libs.bundles.ktor)
    implementation(libs.bundles.exposed)

    implementation(libs.koin.core)
    implementation(libs.koin.ktor)
    implementation(libs.h2db)
    implementation(libs.serialization)

    testImplementation(libs.ktor.test)
    testImplementation(libs.kotlin.test)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
    }
}

buildConfig {
    val properties = Properties().apply {
        load(File(rootDir, "local.properties").bufferedReader())
    }
    buildConfigField("SIGNIN_AUDIENCE", properties.getProperty("SIGNIN_AUDIENCE"))
    buildConfigField("SIGNIN_ISSUER", properties.getProperty("SIGNIN_ISSUER"))
    buildConfigField("SIGNIN_EXPIRATION_TIME", properties.getProperty("SIGNIN_EXPIRATION_TIME").toLong())
    buildConfigField("SIGNIN_SECRET", properties.getProperty("SIGNIN_SECRET"))
}