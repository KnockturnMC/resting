plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.4.0")
}

include(":resting-api", ":resting-impl")

rootProject.name = "resting"
