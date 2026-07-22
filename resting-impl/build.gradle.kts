plugins {
    java
    id("com.gradleup.shadow") version "9.6.1"
    id("io.freefair.lombok") version "9.5.0"
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("io.papermc.paperweight.userdev")
}

description = "resting"
java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))
tasks.runServer { minecraftVersion("1.21.11") }

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.21.11-R0.1-SNAPSHOT")
    implementation("org.jetbrains:annotations:26.0.2-1")
    implementation(project(":resting-api"))
}

tasks.withType<ProcessResources> {
    expand(project.properties)
}
tasks.assemble { dependsOn(tasks.shadowJar) }
