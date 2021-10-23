import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("com.github.ben-manes.versions") version Versions.BEN_MANES_VERSIONS_PLUGIN
    kotlin("multiplatform") version Versions.KOTLIN apply false
}

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        maven(url = "https://dl.bintray.com/kotlin/kotlinx/")
    }
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        maven(url = "https://dl.bintray.com/kotlin/kotlinx/")
    }

    group = "org.jraf"
    version = "1.9.0"

    // Show a report in the log when running tests
    tasks.withType<Test> {
        testLogging {
            events("passed", "skipped", "failed", "standardOut", "standardError")
        }
    }
}

tasks {
    register<Delete>("clean") {
        delete(rootProject.buildDir)
        delete(rootProject.file("docs"))
    }

    wrapper {
        distributionType = Wrapper.DistributionType.ALL
        gradleVersion = Versions.GRADLE
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
