import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    alias(libs.plugins.benManes.versions)
    alias(libs.plugins.kotlin.multiplatform) apply false
}

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
    }

    group = "org.jraf"
    version = "1.12.0"

    // Show a report in the log when running tests
    tasks.withType<Test> {
        testLogging {
            events("passed", "skipped", "failed", "standardOut", "standardError")
        }
    }
}

tasks {
    register<Delete>("clean") {
        delete(rootProject.file("build"))
        delete(rootProject.file("docs"))
    }

    // Configuration for gradle-versions-plugin
    // Run `./gradlew dependencyUpdates` to see latest versions of dependencies
    withType<DependencyUpdatesTask> {
        resolutionStrategy {
            componentSelection {
                all {
                    if (
                        setOf("alpha", "beta", "rc", "preview", "eap", "m1", "m2").any {
                            candidate.version.contains(it, true)
                        }
                    ) {
                        reject("Non stable")
                    }
                }
            }
        }
    }
}
