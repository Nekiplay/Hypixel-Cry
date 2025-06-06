import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("fabric-loom") version "1.10.1"
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.0"
    id("com.nekiplay.hypixelcry.annotation-processor")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    flatDir {
        dirs("libs")
    }
    mavenCentral()
    gradlePluginPortal()
    maven {
        name = "meteor-maven"
        url = uri("https://maven.meteordev.org/releases")
    }
    maven {
        name = "meteor-maven-snapshots"
        url = uri("https://maven.meteordev.org/snapshots")
    }
    maven { url = uri("https://maven.fabricmc.net/") }
    maven { url = uri("https://maven.notenoughupdates.org/releases/") }
    maven { url = uri("https://repo.codemc.io/repository/maven-public/") }
    maven {
        url = uri("https://maven.azureaaron.net/releases")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

base {
    archivesName.set(project.property("archives_base_name") as String)
}

version = project.property("mod_version") as String
group = project.property("maven_group") as String

val shadowModImpl by configurations.creating {
    configurations.modImplementation.get().extendsFrom(this)
}



dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")

    include(modImplementation("org.notenoughupdates.moulconfig:modern-1.21.5:3.10.0")!!)
    // HM API (Hypixel Mod API Library)
    include(modImplementation("net.azureaaron:hm-api:${property("hm_api_version")}")!!)
    shadowModImpl("org.notenoughupdates.moulconfig:modern-1.21.5:3.10.0");
    include(implementation("meteordevelopment:orbit:${property("orbit_version")}")!!)


    // Occlusion Culling
    include(implementation("com.logisticscraft:occlusionculling:${property("occlusionculling_version")}")!!)
}

tasks {
    shadowJar {
        from(sourceSets.main.get().output)
        configurations = listOf(shadowModImpl)
        archiveClassifier.set("shadow")

        //relocate("io.github.notenoughupdates.moulconfig", "com.nekiplay.hypixelcry.moulconfig")
        mergeServiceFiles()
    }

    remapJar {
        dependsOn(shadowJar)
        inputFile.set(shadowJar.get().archiveFile)
    }
}

loom {
    clientOnlyMinecraftJar()
    accessWidenerPath.set(file("src/main/resources/hypixelcry.accesswidener"))
    mixin.useLegacyMixinAp.set(false)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    sourceCompatibility = "21"
    targetCompatibility = "21"
}