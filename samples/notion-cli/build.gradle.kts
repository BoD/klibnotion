plugins {
    kotlin("multiplatform") version Versions.KOTLIN
}

repositories {
    maven { url = uri("https://kotlin.bintray.com/kotlinx") }
}

kotlin {
    macosX64("macos") {
        binaries {
            executable()
        }
    }

    jvm {
        val main by compilations.getting {
            kotlinOptions {
                jvmTarget = "1.8"
                languageVersion = "1.4"
            }
        }
    }

    sourceSets.all {
        languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        languageSettings.useExperimentalAnnotation("kotlinx.cli.ExperimentalCli")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Kotlin
                implementation(kotlin("stdlib"))
                implementation("org.jetbrains.kotlinx", "kotlinx-cli", Versions.KOTLINX_CLI)

                // Library
                implementation(project(":klibnotion"))
            }
        }

        val macosMain by getting {
            dependencies {

            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-jdk8", Versions.COROUTINES)
            }
        }
    }
}
