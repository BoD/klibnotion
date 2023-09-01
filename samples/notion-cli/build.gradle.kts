plugins {
    kotlin("multiplatform")
}

repositories {
    mavenLocal()
    mavenCentral()
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
            }
        }
    }

    sourceSets.all {
        languageSettings.optIn("kotlin.RequiresOptIn")
        languageSettings.optIn("kotlinx.cli.ExperimentalCli")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Kotlin
                implementation(libs.kotlinx.cli)

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
            }
        }
    }
}
