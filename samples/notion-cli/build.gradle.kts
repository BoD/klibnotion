plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    macosArm64()

    sourceSets {
        commonMain {
            dependencies {
                // Kotlin
                implementation(libs.kotlinx.cli)

                // Library
                implementation(project(":klibnotion"))
            }
        }
    }
}
