rootProject.name = "persistentgamemodeswitcher"

pluginManagement {
    fun property(name: String) = providers.gradleProperty(name).get()

    repositories {
        maven("https://maven.fabricmc.net/")
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        id("net.fabricmc.fabric-loom") version property("loom_version")
        id("net.fabricmc.fabric-loom-remap") version property("loom_version")
        id("dev.kikugie.stonecutter") version property("stonecutter_version")
    }
}

plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter {
    create(rootProject) {
        versions(
            "26.3",
            "26.2",
            "26.1",
            "1.21.11",
            "1.21.9",
            "1.21.6"
        )
    }
}