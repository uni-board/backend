class SourceDependenciesScope {
    internal val externalDependencies = mutableListOf<Any>()
    internal val internalDependencies = mutableListOf<Any>()
    fun external(block: VisibilityDependenciesScope.() -> Unit) {
        externalDependencies.addAll(generateDependencies(block))
    }

    fun internal(block: VisibilityDependenciesScope.() -> Unit) {
        internalDependencies.addAll(generateDependencies(block))
    }

    operator fun Any.unaryPlus() {
        internalDependencies.add(this)
    }

    private fun generateDependencies(block: VisibilityDependenciesScope.() -> Unit): List<Any> {
        val scope = VisibilityDependenciesScope()
        scope.block()
        return scope.dependencies
    }
}