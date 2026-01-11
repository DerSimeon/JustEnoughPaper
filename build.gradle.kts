import xyz.jpenilla.gremlin.gradle.WriteDependencySet

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow)
    alias(libs.plugins.userdev)
    alias(libs.plugins.runtask)
    alias(libs.plugins.resourcefactory)
    alias(libs.plugins.gremlin)
}

group = "lol.simeon"
version = "1.0-SNAPSHOT"
description = "Fake the server-side implementation of JustEnoughItems for Paper"

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.21.11-R0.1-SNAPSHOT")
    runtimeDownload(libs.stdlib)
}

gremlin {
    defaultJarRelocatorDependencies.set(true)
    defaultGremlinRuntimeDependency.set(true)
}

configurations.compileOnly {
    extendsFrom(configurations.runtimeDownload.get())
}

tasks.withType<WriteDependencySet> {
    outputFileName.set("jep-dependencies.txt")
    repos.add("https://repo.papermc.io/repository/maven-public/")
    repos.add("https://repo.maven.apache.org/maven2/")
    repos.add("https://repo.triumphteam.dev/snapshots/")
}

configure<SourceSetContainer> {
    named("main") {
        java.srcDir("src/main/kotlin")
    }
}

paperPluginYaml {
    name = "JustEnoughPaper"
    this.version = project.version.toString()
    this.description = project.description.toString()
    apiVersion = "1.21"
    main = "lol.simeon.jep.JustEnoughPaper"
    author = "DerSimeon"
    loader = "lol.simeon.jep.boot.JepPluginLoader"
    bootstrapper = "lol.simeon.jep.boot.JepBootstrapper"
}