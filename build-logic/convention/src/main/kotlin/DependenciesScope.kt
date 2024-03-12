class DependenciesScope {
    internal val main = mutableListOf<SourceDependenciesScope>()
    internal val test = mutableListOf<SourceDependenciesScope>()
    fun main(block: SourceDependenciesScope.() -> Unit) {
        main.add(generate(block))
    }

    fun test(block: SourceDependenciesScope.() -> Unit) {
        test.add(generate(block))
    }
    private fun generate(block: SourceDependenciesScope.() -> Unit): SourceDependenciesScope {
        val scope = SourceDependenciesScope()
        scope.block()
        return scope
    }
}