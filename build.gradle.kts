plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

allprojects{
    group = project.group
    version = project.version
}

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")

    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly("com.destroystokyo.paper:paper-api:1.14.4-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot-api:1.14.4-R0.1-SNAPSHOT")

    implementation(project(":common"))


    // 1.18.2
    implementation(project(":branch_1_18_2", configuration = "reobf"))
    // 1.19.*
    implementation(project(":branch_1_19_4", configuration = "reobf"))
    // 1.20.*
    implementation(project(":branch_1_20_1", configuration = "reobf"))
    implementation(project(":branch_1_20_2", configuration = "reobf"))
    implementation(project(":branch_1_20_4", configuration = "reobf"))
    implementation(files("/home/nexus/IdeaProjects/ExtendedViewDistance/common/src/main/java/club/tesseract/extendedviewdistance/core/lib/folia-scheduler-wrapper-0.0.3-all.jar"))

    // PAPI
    compileOnly("me.clip:placeholderapi:2.11.5")

    // Chat stuff
    compileOnly("net.md-5:bungeecord-chat:1.16-R0.4")
    compileOnly("net.kyori:adventure-api:4.14.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    build{
        dependsOn(shadowJar)
    }

    shadowJar {
        relocate("com.github.NahuLD", "club.tesseract.extendedviewdistance.libs")
        archiveBaseName.set(project.name)
        archiveClassifier.set("")
    }

    processResources {
        filter<org.apache.tools.ant.filters.ReplaceTokens>("tokens" to mapOf("version" to project.version))
    }
}