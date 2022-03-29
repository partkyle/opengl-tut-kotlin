group = "dev.partkyle"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

plugins {
    kotlin("jvm") version "1.6.10"
}

val lwjglVersion = "3.0.0"
val jupiterVersion = "5.8.1"
val glmVersion = "1.0.1"

dependencies {
    implementation("org.lwjgl:lwjgl:${lwjglVersion}")
    implementation("org.lwjgl:lwjgl-platform:${lwjglVersion}:natives-windows")
    implementation("org.lwjgl:lwjgl-platform:${lwjglVersion}:natives-linux")
    implementation("org.lwjgl:lwjgl-platform:${lwjglVersion}:natives-osx")

    implementation("io.github.java-graphics:glm:${glmVersion}")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${jupiterVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${jupiterVersion}")
}
