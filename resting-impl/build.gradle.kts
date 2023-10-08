plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.freefair.lombok") version "8.0.1"
    id("io.papermc.paperweight.userdev") version "1.5.7"
}

group = "com.danichef"
version = "1.0-SNAPSHOT"
description = "resting"
java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")
    implementation("org.jetbrains:annotations:24.0.1")
    implementation(project(":resting-api"))
}

tasks.withType<ProcessResources> {
    expand(project.properties)
}
tasks.assemble { dependsOn(tasks.reobfJar) }
