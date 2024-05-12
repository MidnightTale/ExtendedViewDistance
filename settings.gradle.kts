pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}


plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.8.0")
}


rootProject.name = "ExtendedViewDistance"

include("common")

//file("branch").listFiles()?.forEach { file ->
//    include("branch_${file.name}")
//    project(":branch_${file.name}").projectDir = file
//}
file("branch").listFiles()?.forEach { file ->
    if (!file.name.contains("1_17_1") && !file.name.contains("1_18_2") && !file.name.contains("1_19_4") && !file.name.contains("1_20_1") && !file.name.contains("1_20_2") && !file.name.contains("1_20_4")) {
        include("branch_${file.name}")
        project(":branch_${file.name}").projectDir = file
    }
}
