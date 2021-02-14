enableFeaturePreview("GRADLE_METADATA")

rootProject.name = "klibnotion-root"

include(":library")
project(":library").name = "klibnotion"

// Include all the sample modules from the "samples" directory
file("samples").listFiles()!!.forEach { dir ->
    include(dir.name)
    project(":${dir.name}").projectDir = dir
}

