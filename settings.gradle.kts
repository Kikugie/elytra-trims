import dev.kikugie.stonecutter.gradle.StonecutterSettings

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev")
        maven("https://maven.minecraftforge.net")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.kikugie.dev/releases")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.3.5"
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("club.minnced:discord-webhooks:0.8.4")
    }
}

extensions.configure<StonecutterSettings> {
    kotlinController = true
    centralScript = "build.gradle.kts"
    shared {
        fun mc(version: String, vararg loaders: String) {
            for (it in loaders) vers("$version-$it", version)
        }
        //mc("1.19.4", "fabric", "forge")
        mc("1.20.1", "fabric", "forge")
        //mc("1.20.2", "fabric", "forge")
        mc("1.20.4", "fabric", "neoforge")
        mc("1.20.5", "fabric")
    }
    create(rootProject)
}
rootProject.name = "Elytra Trims"

include("extensions")
val ext = project(":extensions")
listOf("common", "fabric", "forge").forEach {
    include("extensions:$it")
}