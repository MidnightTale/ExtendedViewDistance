plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.7.1"
    id("xyz.jpenilla.run-paper") version "2.3.0"
}

configurations.reobf {extendsFrom(configurations.apiElements.get(), configurations.runtimeElements.get())}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")

    compileOnly(project(":common"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}
paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION
tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    assemble {
        dependsOn(reobfJar)
    }
}