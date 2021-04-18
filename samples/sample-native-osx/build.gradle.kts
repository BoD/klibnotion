plugins {
    kotlin("multiplatform")
}

kotlin {
    macosX64("macos") {
        binaries {
            executable()
        }
    }

    sourceSets {
        val macosMain by getting {
            dependencies {
                // Library
                implementation(project(":klibnotion"))
                // implementation("org.jraf", "klibnotion", "1.0.0")
            }
        }
    }
}

// ./gradlew :sample-native-osx:build