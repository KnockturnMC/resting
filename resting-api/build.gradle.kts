plugins {
    java
    `maven-publish`
}

description = "resting"
java { toolchain.languageVersion.set(JavaLanguageVersion.of(21)); withSourcesJar(); withJavadocJar() } // Include for publishing

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation("org.jetbrains:annotations:24.0.1")
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
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
