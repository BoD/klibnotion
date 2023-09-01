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
    implementation(libs.kotlinx.coroutines.core)

    // Library
    implementation(project(":klibnotion"))
    // implementation("org.jraf", "klibnotion", "1.0.0")
}
