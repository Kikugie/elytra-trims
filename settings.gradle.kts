import dev.kikugie.stonecutter.gradle.StonecutterSettings

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev")
        maven("https://maven.minecraftforge.net")
        maven("https://maven.kikugie.dev/releases")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.3.2"
}

extensions.configure<StonecutterSettings> {
    kotlinController(true)
    centralScript("build.gradle.kts")
    shared {
        vers("1.19.4-fabric", "1.19.4")
        vers("1.19.4-forge", "1.19.4")
        vers("1.20.1-fabric", "1.20.1")
        vers("1.20.1-forge", "1.20.1")
        vers("1.20.2-fabric", "1.20.2")
        vers("1.20.2-forge", "1.20.2")
        vers("1.20.4-fabric", "1.20.4")
    }
    create(rootProject)
}
rootProject.name = "Elytra Trims"