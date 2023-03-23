plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.4.0")
}

include(":api", ":impl")

rootProject.name = "resting"
project(":impl").name = "resting-plugin"
project(":api").name = "resting-api"