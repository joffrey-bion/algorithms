import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.72"
    id("com.jfrog.bintray") version "1.8.5"
}

group = "org.hildan.algorithms"
version = "0.1"
description = "Search and sort algorithms for practice and educational purpose."

val Project.githubUser get() = "joffrey-bion"
val githubSlug = "$githubUser/${rootProject.name}"
val githubRepoUrl = "https://github.com/$githubSlug"
val Project.labels get() = arrayOf("sort", "search", "algorithm", "astar", "quicksort", "mergesort")
val Project.licenses get() = arrayOf("MIT")

repositories {
    jcenter()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

dependencies {
    implementation("org.jetbrains:annotations:15.0")
    testImplementation("junit:junit:4.+")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
