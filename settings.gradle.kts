
pluginManagement {
    fun property(name: String) = providers.gradleProperty(name).get()

    repositories {
        maven("https://maven.fabricmc.net/")
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        id("net.fabricmc.fabric-loom") version property("loom_version")
    }
}

rootProject.name = "persistentgamemodeswitcher"