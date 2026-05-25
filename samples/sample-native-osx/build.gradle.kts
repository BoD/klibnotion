plugins {
    kotlin("multiplatform")
}

kotlin {
    macosArm64 {
        binaries {
            executable()
        }
    }

    sourceSets {
        macosMain {
            dependencies {
                // Library
                implementation(project(":klibnotion"))
            }
        }
    }
}

// ./gradlew :sample-native-osx:build
