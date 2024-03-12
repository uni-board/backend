import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
plugins {
    `kotlin-dsl`
}
java {
    sourceCompatibility = JavaVersion.VERSION_20
    targetCompatibility = JavaVersion.VERSION_20
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_20.toString()
    }
}

dependencies {
    implementation(libs.kotlin.gradle)
}
gradlePlugin {
    plugins {
        register("configuration") {
            id = "configuration"
            implementationClass = "ConfigurationPlugin"
        }
    }
}