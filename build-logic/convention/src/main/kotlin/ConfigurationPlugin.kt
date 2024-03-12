import org.gradle.api.Plugin
import org.gradle.api.Project

class ConfigurationPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project.pluginManager) {
            apply("org.jetbrains.kotlin.jvm")
        }
        project.setupKotlin()
    }
}
