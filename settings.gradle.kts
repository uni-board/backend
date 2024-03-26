pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}

//enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "backend"

include("core:domain")
include("core:data")
include("storage:data")
include("storage:domain")
include("storage:presentation")

include("board:data")
include("board:domain")
include("board:presentation")

include("app")