rootProject.name = "klibnotion-root"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://central.sonatype.com/repository/maven-snapshots/")
        google()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        maven("https://central.sonatype.com/repository/maven-snapshots/")
        google()
    }
}

plugins {
    // See https://splitties.github.io/refreshVersions/setup/
    id("de.fayard.refreshVersions") version "0.60.6"
}

include(":library")
project(":library").name = "klibnotion"

// Include all the sample modules from the "samples" directory
file("samples").listFiles()!!.forEach { dir ->
    include(dir.name)
    project(":${dir.name}").apply {
        projectDir = dir
        name = dir.name
    }
}
