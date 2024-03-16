plugins {
    id("java")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("com.destroystokyo.paper:paper-api:1.14.4-R0.1-SNAPSHOT")
    //compileOnly("org.spigotmc:spigot-api:1.14.4-R0.1-SNAPSHOT")
    implementation(files("/home/nexus/IdeaProjects/ExtendedViewDistance/common/src/main/java/club/tesseract/extendedviewdistance/core/lib/folia-scheduler-wrapper-0.0.3-all.jar"))

    // Messaging Utils
    compileOnly("net.md-5:bungeecord-chat:1.16-R0.4")
    compileOnly("net.kyori:adventure-api:4.14.0")


    // Mojang Libraries
    //compileOnly("com.mojang:datafixerupper:4.1.27")
    //compileOnly("com.mojang:brigadier:1.0.18")
    //compileOnly("com.mojang:javabridge:1.2.24")
    //compileOnly("com.mojang:authlib:3.16.29")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks{
    compileJava {
        options.encoding = "UTF-8"
    }
}