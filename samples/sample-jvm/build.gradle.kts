import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", Versions.COROUTINES)

    // Library
    implementation(project(":klibnotion"))
    // implementation("org.jraf", "klibnotion", "1.0.0")
}
