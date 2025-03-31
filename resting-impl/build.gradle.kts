plugins {
    java
    id("com.gradleup.shadow") version "8.3.5"
    id("io.freefair.lombok") version "8.13.1"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.16"
}

description = "resting"
java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
    implementation("org.jetbrains:annotations:24.0.1")
    implementation(project(":resting-api"))
}

tasks.withType<ProcessResources> {
    expand(project.properties)
}
