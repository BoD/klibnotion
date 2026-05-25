@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {
        mainRun {
            mainClass = "org.jraf.klibnotion.sample.MainKt"
        }
    }
    macosArm64 {
        binaries {
            executable()
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                // Library
                implementation(project(":klibnotion"))
            }
        }
        jvmMain {
            dependencies {
                // Logging
                implementation(libs.slf4j.simple)
            }
        }
    }
}

// ./gradlew :sample:jvmRun to run the JVM sample
// ./gradlew :sample:runDebugExecutableMacosArm64 to run the macOS sample
