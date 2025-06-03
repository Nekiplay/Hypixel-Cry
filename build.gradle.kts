import com.xpdustry.ksr.kotlinRelocate
import net.fabricmc.loom.task.RemapJarTask

plugins {
    idea
    java
    id("gg.essential.loom") version "0.10.0.+"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.github.juuxel.loom-quiltflower") version "1.7.3"
    kotlin("jvm") version "1.8.21"
    id("com.google.devtools.ksp") version "1.8.21-1.0.11"
    id("net.kyori.blossom") version "2.1.0"
    id("com.xpdustry.ksr") version "1.0.0"
    id("moe.nea.shot") version "1.0.0"
}

version = "1.1.0"
group = "com.nekiplay.hypixelcry"
base.archivesName.set("HypixelCry")

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

loom {
    launchConfigs {
        "client" {
            property("mixin.debug", "true")
            arg("--mixin", "mixins.hypixelcry.json")
            arg("--tweakClass", "io.github.notenoughupdates.moulconfig.tweaker.DevelopmentResourceTweaker")
        }
    }
    forge {
        pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
        mixinConfig("mixins.hypixelcry.json")
    }
    mixin {
        defaultRefmapName.set("mixins.hypixelcry.refmap.json")
    }
}

repositories {
    mavenCentral()
    maven("https://repo.polyfrost.cc/releases")
    maven("https://jitpack.io")
    maven("https://repo.spongepowered.org/maven/")
    maven("https://maven.minecraftforge.net/")
    maven("https://maven.notenoughupdates.org/releases")
}

val shadowImplementation: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

val shadowModImpl: Configuration by configurations.creating {
    configurations.modImplementation.get().extendsFrom(this)
}


dependencies {
    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")

    implementation(kotlin("stdlib"))
    implementation("org.spongepowered:mixin:0.7.11-SNAPSHOT") {
        isTransitive = false
    }
    annotationProcessor("net.fabricmc:sponge-mixin:0.11.4+mixin.0.8.5")

    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    shadowModImpl("org.notenoughupdates.moulconfig:legacy:3.8.0")

    shadowImplementation("it.unimi.dsi:fastutil:8.2.1")
}

tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-8"
}

tasks.jar {
    archiveClassifier.set("named")
    manifest.attributes(
        "FMLCorePluginContainsFMLMod" to true,
        "FMLCorePlugin" to "com.nekiplay.hypixelcry.FMLLoadingPlugin",
        "ForceLoadAsMod" to true,
        "MixinConfigs" to "mixins.hypixelcry.json",
        "ModSide" to "CLIENT",
        "TweakOrder" to "0"
    )
}

tasks.shadowJar {
    archiveClassifier.set("dev")
    configurations = listOf(shadowImplementation, shadowModImpl)
    exclude("**/module-info.class", "LICENSE.txt")
    dependencies {
        exclude(dependency("org.jetbrains.kotlin:.*"))
    }
    mergeServiceFiles()
    relocate("io.github.notenoughupdates.moulconfig", "com.nekiplay.hypixelcry.deps.moulconfig")
    minimize {
		exclude(dependency("org.notenoughupdates.moulconfig:legacy:.*"))
        exclude("com/nekiplay/hypixelcry/deps/moulconfig/.*")
    }
}

val remapJar by tasks.named<net.fabricmc.loom.task.RemapJarTask>("remapJar") {
    archiveClassifier.set("")
    from(tasks.shadowJar)
    input.set(tasks.shadowJar.get().archiveFile)

    finalizedBy("cleanIntermediateJars")
}

tasks.register("cleanIntermediateJars") {
    doLast {
        val filesToDelete = fileTree("build/libs").matching {
            include("*-named.jar", "*-dev.jar")
        }
        filesToDelete.forEach { file ->
            if (file.exists()) {
                println("Deleting intermediate file: ${file.name}")
                file.delete()
            }
        }
    }
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("mcmod.info") {
        expand("version" to project.version, "mcversion" to "1.8.9")
    }
}

tasks.assemble.get().dependsOn(remapJar)
