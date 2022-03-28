group = "dev.partkyle"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

plugins {
    kotlin("jvm") version "1.6.10"
}

val lwjglVersion = "3.0.0"

dependencies {
    implementation("org.lwjgl:lwjgl:${lwjglVersion}")
    implementation("org.lwjgl:lwjgl-platform:${lwjglVersion}:natives-windows")
    implementation("org.lwjgl:lwjgl-platform:${lwjglVersion}:natives-linux")
    implementation("org.lwjgl:lwjgl-platform:${lwjglVersion}:natives-osx")

    implementation("io.github.java-graphics:glm:1.0.1")
}
