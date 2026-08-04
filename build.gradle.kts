import xyz.jpenilla.runpaper.task.RunServer

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow)
    alias(libs.plugins.userdev)
    alias(libs.plugins.runtask)
    alias(libs.plugins.resourcefactory)
    alias(libs.plugins.resourcefactory.bukkit)
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

    testImplementation(kotlin("test"))
    testImplementation(libs.testify.junit)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
    dependsOn(tasks.shadowJar)
}

tasks.test {
    useJUnitPlatform()
    dependsOn(tasks.shadowJar)
    systemProperty("testify.realTests", providers.gradleProperty("testify.realTests").orElse("false").get())
}

tasks.withType<RunServer> {
    if (name == "runServer") {
        dependsOn(tasks.shadowJar)
        minecraftVersion("1.21.11")
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
    apiVersion = "1.21"
    main = "lol.simeon.jer.JustEnoughRecipes"
    author = "DerSimeon"
}

bukkitPluginYaml {
    name = "JustEnoughRecipes"
    this.version = project.version.toString()
    this.description = project.description.toString()
    apiVersion = "1.21"
    main = "lol.simeon.jer.JustEnoughRecipes"
    author = "DerSimeon"
}
