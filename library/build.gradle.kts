plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version Versions.KOTLIN_SERIALIZATION
}

// Generate a Version.kt file with a constant for the version name
tasks.register("generateVersionKt") {
    val outputDir = layout.buildDirectory.dir("generated/source/kotlin").get().asFile
    outputs.dir(outputDir)
    doFirst {
        val outputWithPackageDir = File(outputDir, "org/jraf/klibnotion/internal/client").apply { mkdirs() }
        File(outputWithPackageDir, "Version.kt").writeText(
            """
                package org.jraf.klibnotion.internal.client
                internal const val VERSION = "${project.version}"
            """.trimIndent()
        )
    }
}

kotlin {
    jvm {
        val main by compilations.getting {
            kotlinOptions {
                jvmTarget = "1.8"
                languageVersion = "1.4"
            }
        }
    }

    macosX64("macos") {
        binaries {
            framework()
        }
    }

    sourceSets.all {
        languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        languageSettings.useExperimentalAnnotation("io.ktor.util.KtorExperimentalAPI")
        languageSettings.useExperimentalAnnotation("io.ktor.util.InternalAPI")
    }

    sourceSets {
        val commonMain by getting {

            kotlin.srcDir(tasks.getByName("generateVersionKt").outputs.files)

            dependencies {
                implementation(kotlin("stdlib"))
                implementation("io.ktor", "ktor-client-core", Versions.KTOR)
                implementation("io.ktor", "ktor-client-json", Versions.KTOR)
                implementation("io.ktor", "ktor-client-serialization", Versions.KTOR)
                implementation("io.ktor", "ktor-client-logging", Versions.KTOR)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-jdk8", Versions.COROUTINES)
                implementation("io.ktor", "ktor-client-okhttp", Versions.KTOR)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }

        val macosMain by getting {
            dependencies {
                implementation("io.ktor", "ktor-client-curl", Versions.KTOR)
            }
        }
        val macosTest by getting {
        }
    }
}

//tasks.withType<KotlinCompile> {
//    kotlinOptions.jvmTarget = "1.8"
//}
