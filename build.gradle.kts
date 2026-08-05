import xyz.jpenilla.runpaper.task.RunServer

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow)
    alias(libs.plugins.userdev)
    alias(libs.plugins.runtask)
    alias(libs.plugins.resourcefactory)
}

group = "lol.simeon"
version = "1.2-SNAPSHOT"
description = "Send recipe data from the server to clients needed for some client-side mods."

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("26.1.2.build.74-stable")
    implementation(libs.stdlib)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

configure<SourceSetContainer> {
    named("main") {
        java.srcDir("src/main/kotlin")
    }
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}

tasks.withType<RunServer> {
    if (name == "runServer") {
        dependsOn(tasks.shadowJar)
        minecraftVersion("26.1.2")
        pluginJars(tasks.shadowJar.flatMap { it.archiveFile })
    }
}

runPaper {
    disablePluginJarDetection()
}

paperPluginYaml {
    name = "JustEnoughRecipes"
    this.version = project.version.toString()
    this.description = project.description.toString()
    apiVersion = "26.1"
    main = "lol.simeon.jer.JustEnoughRecipes"
    author = "DerSimeon"
}
