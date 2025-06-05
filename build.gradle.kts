plugins {
    id("fabric-loom") version "1.10-SNAPSHOT"
    id("maven-publish")
    kotlin("jvm")
}

repositories {
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
    maven { url = uri("https://repo.codemc.io/repository/maven-public/") } // For Occlusion Culling library
}

configurations.all {
    // Check for snapshots more frequently than Gradle's default of 1 day. 0 = every build.
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}

java {
}

base {
    archivesName.set(project.property("archives_base_name") as String)
}

version = project.property("mod_version") as String
group = project.property("maven_group") as String

configurations {
    create("shadowModImpl")
    getByName("modImplementation").extendsFrom(getByName("shadowModImpl"))

    create("library")
    getByName("implementation").extendsFrom(getByName("library"))
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings(loom.layered {
        //Using Mojmap breaks runClient, so uncomment only for snapshots when temp mappings are needed
        //officialMojangMappings()
        mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
    })
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")

    "shadowModImpl"("org.notenoughupdates.moulconfig:modern:3.5.0")

    "library"("meteordevelopment:orbit:${property("orbit_version")}")
    implementation(kotlin("stdlib-jdk8"))

    // Occlusion Culling (https://github.com/LogisticsCraft/OcclusionCulling)
    include(implementation("com.logisticscraft:occlusionculling:${property("occlusionculling_version")}")!!)
}

tasks.register<Jar>("shadowJar") {
    from(sourceSets.main.get().output)
    from({
        configurations.getByName("shadowModImpl").map { if (it.isDirectory) it else zipTree(it) }
    })
    archiveClassifier.set("shadow")

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.named<net.fabricmc.loom.task.RemapJarTask>("remapJar") {
    dependsOn(tasks.named("shadowJar"))
    input.set(tasks.named<Jar>("shadowJar").get().archiveFile)
}

loom {
    accessWidenerPath.set(file("src/main/resources/hypixelcry.accesswidener"))
    mixin {
        useLegacyMixinAp = false
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
kotlin {
    jvmToolchain(21)
}