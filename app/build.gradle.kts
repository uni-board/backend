import io.ktor.plugin.features.*

plugins {
    alias(libs.plugins.configuration)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ktor)
    application
}

configuration {
    internal {
        +project(":core:data")
        +project(":core:domain")

        +project(":board:presentation")
        +project(":board:data")

        +project(":storage:presentation")
        +project(":storage:data")
        +libs.bundles.ktor
        +libs.koin.core
        +libs.koin.ktor
        +libs.ktor.cors

        +libs.slf4j.api
        +libs.slf4j.simple
    }
    test {

    }
}

jib {
    container.mainClass = "Main"
}

ktor {
    docker {
        jreVersion = JavaVersion.VERSION_17
        localImageName = "uniboard-backend"
        imageTag = "0.0.1-SNAPSHOT"

        environmentVariable("IS_DOCKER", "true")
        portMappings = listOf(
            DockerPortMapping(
                80,
                8080,
                DockerPortMappingProtocol.TCP
            ),
            DockerPortMapping(
                81,
                8081,
                DockerPortMappingProtocol.TCP
            )
        )

        externalRegistry = DockerImageRegistry.externalRegistry(
            username = provider { System.getenv("USERNAME") },
            password = provider { System.getenv("PASSWORD") },
            project = provider { System.getenv("IMAGE_NAME") },
            hostname = provider { System.getenv("REGISTRY") },
            namespace = provider { System.getenv("NAMESPACE")}
        )
    }
}