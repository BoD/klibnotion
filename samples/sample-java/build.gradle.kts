plugins {
    kotlin("jvm")
    application
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("org.jraf.klibnotion.sample.SampleKt")
}

dependencies {
    // Logging
    implementation(libs.slf4j.simple)

    // Library
    implementation(project(":klibnotion"))
    // implementation("org.jraf", "klibnotion", "1.0.0")
}
