import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.internal.catalog.DelegatingProjectDependency
import org.gradle.api.internal.catalog.ExternalModuleDependencyFactory
import org.gradle.api.provider.Provider

class VisibilityDependenciesScope {
    val dependencies = mutableListOf<Any>()

    operator fun Any.unaryPlus() {
        dependencies.add(this)
    }
}