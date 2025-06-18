plugins {
    java
    id("com.gradleup.shadow") version "8.3.6"
    id("io.freefair.lombok") version "8.14"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("io.papermc.paperweight.userdev")
}

description = "resting"
java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))
tasks.runServer { minecraftVersion("1.21.5") }

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.21.5-R0.1-SNAPSHOT")
    implementation("org.jetbrains:annotations:26.0.2")
    implementation(project(":resting-api"))
}

tasks.withType<ProcessResources> {
    expand(project.properties)
}
tasks.assemble { dependsOn(tasks.shadowJar) }
