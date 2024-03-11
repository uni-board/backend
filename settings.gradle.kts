pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "backend"

rootDir
    .walk()
    .filter {
        it.isDirectory
                && file("${it.absolutePath}/build.gradle.kts").exists()
    }
    .forEach {
        val calculated = it.absolutePath.replace(rootDir.absolutePath, "").replace("/", ":")
        include(calculated)
    }
