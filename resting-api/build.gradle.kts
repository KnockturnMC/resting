plugins {
    java
    `maven-publish`
}

description = "resting"
java { toolchain.languageVersion.set(JavaLanguageVersion.of(17)); withSourcesJar(); withJavadocJar() } // Include for publishing

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    implementation("org.jetbrains:annotations:24.0.1")
    compileOnly("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
}

publishing {
    repositories {
        maven("https://repo.knockturnmc.com/content/repositories/knockturn-public-snapshot/") {
            name = "knockturnPublic"
            credentials(PasswordCredentials::class)
        }
    }

    publications.create<MavenPublication>("maven") {
        artifactId = "resting-api"
        from(components["java"])
    }
}
