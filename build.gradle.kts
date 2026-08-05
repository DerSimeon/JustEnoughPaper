import io.papermc.paperweight.userdev.ReobfArtifactConfiguration
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
    paperweight.paperDevBundle("1.21.11-R0.1-SNAPSHOT")
    implementation(libs.stdlib)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

configure<SourceSetContainer> {
    named("main") {
        java.srcDir("src/main/kotlin")
    }
}

tasks.assemble {
    dependsOn(tasks.reobfJar)
}

paperweight.reobfArtifactConfiguration = ReobfArtifactConfiguration.REOBF_PRODUCTION

tasks.reobfJar {
    outputJar = layout.buildDirectory.file("libs/${project.name}-${project.version}-reobf.jar")
}

tasks.withType<RunServer> {
    if (name == "runServer") {
        dependsOn(tasks.reobfJar)
        minecraftVersion("1.21.11")
        pluginJars(tasks.reobfJar.flatMap { it.outputJar })
    }
}

runPaper {
    disablePluginJarDetection()
}

paperPluginYaml {
    name = "JustEnoughRecipes"
    this.version = project.version.toString()
    this.description = project.description.toString()
    apiVersion = "1.21"
    main = "lol.simeon.jer.JustEnoughRecipes"
    author = "DerSimeon"
}
