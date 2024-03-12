import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.dependencies

open class Configuration {
    internal val dependencies = mutableListOf<DependenciesScope>()

    fun Configuration.dependencies(block: DependenciesScope.() -> Unit) {
        val scope = DependenciesScope()
        scope.block()
        dependencies.add(scope)
    }
}

internal fun Configuration.apply(project: Project) {
    val mainDeps = dependencies.flatMap { it.main }
    val testDeps = dependencies.flatMap { it.test }
    project.dependencies {
        mainDeps.forEach { scope ->
            scope.internalDependencies.forEach {
                "implementation"(it)
            }
            scope.externalDependencies.forEach {
                "api"(it)
            }
        }
        testDeps.forEach { scope ->
            scope.internalDependencies.forEach {
                "testImplementation"(it)
            }
            scope.externalDependencies.forEach {
                "testImplementation"(it)
            }
        }
    }
}

fun Project.configuration(block: Configuration.() -> Unit) {
    val configuration = Configuration()
    configuration.block()
    configuration.apply(this)
}

fun Configuration.internal(block: VisibilityDependenciesScope.() -> Unit) {
    main {
        internal(block)
    }
}

fun Configuration.external(block: VisibilityDependenciesScope.() -> Unit) {
    main {
        external(block)
    }
}

fun Project.gradleProperty(name: String): String? = providers.gradleProperty(name).orNull

fun Configuration.main(block: SourceDependenciesScope.() -> Unit) {
    dependencies {
        main(block)
    }
}

fun Configuration.test(block: SourceDependenciesScope.() -> Unit) {
    dependencies {
        test(block)
    }
}