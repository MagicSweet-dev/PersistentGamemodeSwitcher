import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.api.fabricapi.FabricApiExtension

plugins {
    id("java")
    id("net.fabricmc.fabric-loom-remap") apply false
    id("net.fabricmc.fabric-loom") apply false
}

val withObfuscation = sc.current.parsed < "26.1"
if (withObfuscation) plugins.apply("net.fabricmc.fabric-loom-remap")
else plugins.apply("net.fabricmc.fabric-loom")

group = property("maven_group") as String
version = "v${property("mod_version")}"

val minecraftVersion = property("minecraft_version") as String
val maxMinecraftVersion = property("minecraft_version_max") as String
val loaderVersion = property("loader_version") as String
val fabricApiVersion = property("fabric_api_version") as String

dependencies {
    val mod = if (withObfuscation) "modImplementation" else "implementation"

    "minecraft"("com.mojang:minecraft:$minecraftVersion")
    mod("net.fabricmc:fabric-loader:${loaderVersion}")
    mod("net.fabricmc.fabric-api:fabric-api:${fabricApiVersion}")

    if (withObfuscation) {
        "mappings"(project.extensions.getByType<LoomGradleExtensionAPI>().officialMojangMappings())
    }

    testImplementation("net.fabricmc:fabric-loader-junit:${loaderVersion}")
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
    }
}

project.extensions.getByType<FabricApiExtension>().apply {
    configureTests()
}

var versionSuffix =
    if (
        minecraftVersion == maxMinecraftVersion
        || "${minecraftVersion}.x" == maxMinecraftVersion
    ) maxMinecraftVersion
    else "${minecraftVersion}-${maxMinecraftVersion}"
base.archivesName = "persistentgamemodeswitcher-mc$versionSuffix"

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("mc_version", minecraftVersion)
    inputs.property("mc_version_max", maxMinecraftVersion)
    inputs.property("fabric_api_version", fabricApiVersion)

    from(sourceSets.main.get().resources.srcDirs) {
        include("fabric.mod.json")
        expand(
            "version" to project.version,
            "mc_version" to minecraftVersion,
            "mc_version_max" to if (maxMinecraftVersion.contains(".x")) "=${maxMinecraftVersion}" else "<=${maxMinecraftVersion}",
            "fabric_api_version" to fabricApiVersion,
        )
    }

    from(sourceSets.main.get().resources.srcDirs) {
        exclude("fabric.mod.json")
    }

    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(
            if (sc.current.parsed >= "26.1") 25
            else if (sc.current.parsed >= "1.20.5") 21
            else if (sc.current.parsed >= "1.18") 17
            else if (sc.current.parsed >= "1.17") 16
            // FIXME Java 8 support for Minecraft 1.16.x (and don't forget mixins.json)
            else 11
        ))
    }
}