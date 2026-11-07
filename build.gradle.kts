plugins {
    id("net.fabricmc.fabric-loom")
}

group = property("maven_group") as String
version = property("mod_version") as String

val minecraftVersion = property("minecraft_version") as String
val loaderVersion = property("loader_version") as String
val fabricApiVersion = property("fabric_api_version") as String

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    implementation("net.fabricmc:fabric-loader:$loaderVersion")
    implementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
}

tasks.jar {
    archiveClassifier.set("fabric")
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("mc_version", minecraftVersion)
    inputs.property("fabric_api_version", fabricApiVersion)

    from(sourceSets.main.get().resources.srcDirs) {
        include("fabric.mod.json")
        expand(
            "version" to project.version,
            "mc_version" to minecraftVersion,
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
        languageVersion.set(JavaLanguageVersion.of(25))
    }
    withSourcesJar()
    withJavadocJar()
}