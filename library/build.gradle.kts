import com.gradleup.librarian.gradle.Librarian

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}


kotlin {
    jvm()
    macosArm64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.json)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.logging)
                api(libs.kotlinx.coroutines.core)
                api(libs.kotlinx.datetime)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }

        macosMain {
            dependencies {
                implementation(libs.ktor.client.curl)
            }
        }
    }
}

Librarian.module(project)
