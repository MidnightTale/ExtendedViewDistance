pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}


plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.5.0")
}


rootProject.name = "ExtendedViewDistance"

include("common")

file("branch").listFiles()?.forEach { file ->
    if (!file.name.contains("1_17_1")) {
        include("branch_${file.name}")
        project(":branch_${file.name}").projectDir = file
    }
}
