import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.72" // used by codingame
}

description = "Search and sort algorithms for practice and educational purpose."

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("org.jetbrains:annotations:15.0")
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks {
    test {
        useJUnitPlatform()
    }
}