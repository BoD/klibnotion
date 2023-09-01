plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    id("maven-publish")
    alias(libs.plugins.dokka)
    id("signing")
}

tasks {
    // Generate a Version.kt file with a constant for the version name
    register("generateVersionKt") {
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

    // Generate Javadoc (Dokka) Jar
    register<Jar>("dokkaHtmlJar") {
        archiveClassifier.set("javadoc")
        from("build/dokka")
        dependsOn(dokkaHtml)
    }
}

kotlin {
    jvm {
        val main by compilations.getting {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    macosX64("macos") {
        binaries {
            framework()
        }
    }

    sourceSets.all {
        languageSettings.optIn("kotlin.RequiresOptIn")
        languageSettings.optIn("io.ktor.util.InternalAPI")
    }

    sourceSets {
        val commonMain by getting {

            kotlin.srcDir(tasks.getByName("generateVersionKt").outputs.files)

            dependencies {
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.json)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.logging)
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
                implementation(libs.kotlinx.coroutines.jdk8)
                implementation(libs.ktor.client.okhttp)
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
                implementation(libs.ktor.client.curl)
            }
        }
        val macosTest by getting {
        }
    }
}

publishing {
    repositories {
        maven {
            // Note: declare your user name / password in your home's gradle.properties like this:
            // mavenCentralNexusUsername = <user name>
            // mavenCentralNexusPassword = <password>
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
            name = "mavenCentralNexus"
            credentials(PasswordCredentials::class)
        }
    }

    publications.withType<MavenPublication>().forEach { publication ->

        publication.artifact(tasks.getByName("dokkaHtmlJar"))

        publication.pom {
            name.set("klibnotion")
            description.set("A Notion API client library for Kotlin, Java and more.")
            url.set("https://github.com/BoD/klibnotion")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    distribution.set("repo")
                }
            }
            developers {
                developer {
                    id.set("BoD")
                    name.set("Benoit 'BoD' Lubek")
                    email.set("BoD@JRAF.org")
                    url.set("https://JRAF.org")
                    organization.set("JRAF.org")
                    organizationUrl.set("https://JRAF.org")
                    roles.set(listOf("developer"))
                    timezone.set("+1")
                }
            }
            scm {
                connection.set("scm:git:https://github.com/BoD/klibnotion")
                developerConnection.set("scm:git:https://github.com/BoD/klibnotion")
                url.set("https://github.com/BoD/klibnotion")
            }
            issueManagement {
                url.set("https://github.com/BoD/klibnotion/issues")
                system.set("GitHub Issues")
            }
        }
    }
}

signing {
    // Note: declare the signature key, password and file in your home's gradle.properties like this:
    // signing.keyId=<8 character key>
    // signing.password=<your password>
    // signing.secretKeyRingFile=<absolute path to the gpg private key>
    sign(publishing.publications)
}

// Workaround for https://youtrack.jetbrains.com/issue/KT-46466
val dependsOnTasks = mutableListOf<String>()
tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOnTasks.add(this.name.replace("publish", "sign").replaceAfter("Publication", ""))
    dependsOn(dependsOnTasks)
}

tasks.dokkaHtml.configure {
    outputDirectory.set(rootProject.file("docs"))
}

// Run `./gradlew dokkaHtml` to generate the docs
// Run `./gradlew publishToMavenLocal` to publish to the local maven repo
// Run `./gradlew publish` to publish to Maven Central (then go to https://oss.sonatype.org/#stagingRepositories and "close", and "release")
