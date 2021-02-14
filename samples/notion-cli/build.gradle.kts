plugins {
    kotlin("multiplatform")
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
                // TODO add to version
                implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.1")

                // Library
                implementation(project(":klibnotion"))
                // implementation("org.jraf", "klibnotion", "1.0.0")
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
