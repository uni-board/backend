import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


fun Project.setupKotlin() {
    tasks.withType(KotlinCompile::class.java) {
        kotlinOptions.apply {
            jvmTarget = "20"
            experimentalContextReceivers()
        }
    }
}

fun KotlinCommonOptions.experimentalContextReceivers() {
    freeCompilerArgs += listOf("-Xcontext-receivers")
}