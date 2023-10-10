plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.freefair.lombok") version "8.3"
    id("io.papermc.paperweight.userdev") version "1.5.8"
}

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
tasks.reobfJar {
    outputJar.set(layout.buildDirectory.file("libs/${project.name}-${project.version}-reobf.jar"))
}
tasks.assemble {
    dependsOn(tasks.reobfJar)
}
