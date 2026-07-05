import io.papermc.paperweight.userdev.ReobfArtifactConfiguration
import proguard.gradle.ProGuardTask
import xyz.jpenilla.runpaper.task.RunServer

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow)
    alias(libs.plugins.userdev)
    alias(libs.plugins.runtask)
    alias(libs.plugins.resourcefactory)
    alias(libs.plugins.resourcefactory.bukkit)
    alias(libs.plugins.proguard)
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

val java21Launcher = javaToolchains.launcherFor {
    languageVersion.set(JavaLanguageVersion.of(21))
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

tasks.register<ProGuardTask>("obfuscateJar") {
    group = "build"
    description = "Obfuscates the plugin jar using ProGuard. The resulting jar is saved as build/libs/${project.name}-${project.version}-obf.jar."
    dependsOn(tasks.shadowJar)

    injars(tasks.shadowJar.get().archiveFile)
    outjars(layout.buildDirectory.file("libs/${project.name}-${project.version}-obf.jar"))

    libraryjars(
        mapOf("jarfilter" to "!**.jar", "filter" to "!module-info.class"),
        java21Launcher.map { it.metadata.installationPath.file("jmods/java.base.jmod").asFile }
    )
    val proguardLibraryJars = configurations.compileClasspath.get().filter { file ->
        file.extension != "jmod" && "${File.separator}jmods${File.separator}" !in file.path
    }
    libraryjars(proguardLibraryJars)

    configuration("proguard.pro")
}

tasks.matching { it.name == "proguard" }.configureEach {
    enabled = false
}

tasks.withType<ProGuardTask>().configureEach {
    notCompatibleWithConfigurationCache("The ProGuard Gradle task captures project state.")
}

tasks.build {
    dependsOn("obfuscateJar")
}

tasks.test {
    useJUnitPlatform()
    dependsOn(tasks.named<ProGuardTask>("obfuscateJar"))
    systemProperty("testify.realTests", providers.gradleProperty("testify.realTests").orElse("false").get())
}

tasks.withType<RunServer> {
    if (name == "runServer") {
        dependsOn("obfuscateJar")
        minecraftVersion("1.21.11")
        val buildPath = project.layout.buildDirectory.file("libs/${project.name}-${project.version}-obf.jar").get().asFile.absolutePath
        pluginJars(buildPath)
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
