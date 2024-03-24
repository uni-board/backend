import io.ktor.plugin.features.*
import java.util.*

plugins {
    alias(libs.plugins.configuration)
    alias(libs.plugins.serialization)
    alias(libs.plugins.buildconfig)
    alias(libs.plugins.ktor)
}

configuration {
    internal {
        +project(":board:presentation")
        +project(":board:data")

        +project(":storage:presentation")
        +project(":storage:data")
        +libs.bundles.ktor
        +libs.koin.core
        +libs.koin.ktor

        +libs.slf4j.api
        +libs.slf4j.simple
    }
    test {

    }
}

buildConfig {
    val properties = Properties().apply {
        load(File(rootDir, "local.properties").bufferedReader())
    }
    buildConfigField("SIGNIN_AUDIENCE", properties.getProperty("SIGNIN_AUDIENCE"))
    buildConfigField("SIGNIN_ISSUER", properties.getProperty("SIGNIN_ISSUER"))
    buildConfigField("SIGNIN_EXPIRATION_TIME", properties.getProperty("SIGNIN_EXPIRATION_TIME").toLong())
    buildConfigField("SIGNIN_SECRET", properties.getProperty("SIGNIN_SECRET"))
}

ktor {
    docker {
        jreVersion = JavaVersion.VERSION_17
        localImageName = "uniboard-backend"
        imageTag = "0.0.1-SNAPSHOT"

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