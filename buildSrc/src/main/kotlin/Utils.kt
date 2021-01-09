import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

fun KotlinDependencyHandler.implementation(group: String, artifact: String, version: String) =
    implementation("$group:$artifact:$version")