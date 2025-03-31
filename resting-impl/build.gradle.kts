plugins {
    java
    id("com.gradleup.shadow") version "8.3.5"
    id("io.freefair.lombok") version "8.13.1"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("io.papermc.paperweight.userdev")
}

description = "resting"
java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))
tasks.runServer { minecraftVersion("1.21.4") }

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
    implementation("org.jetbrains:annotations:24.0.1")
    implementation(project(":resting-api"))
}

tasks.withType<ProcessResources> {
    expand(project.properties)
}
tasks.assemble { dependsOn(tasks.shadowJar) }
