pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://maven.architectury.dev/")
        maven("https://maven.fabricmc.net")
        maven(url = "https://jitpack.io/")
        maven(url = "https://maven.minecraftforge.net/")
        maven(url = "https://repo.spongepowered.org/maven/")
        maven(url = "https://repo.essential.gg/repository/maven-releases/")
        maven(url = "https://maven.architectury.dev/")
        maven("https://repo.nea.moe/releases")
        maven("https://maven.xpdustry.com/releases") {
            name = "xpdustry-releases"
            mavenContent { releasesOnly() }
        }
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "gg.essential.loom" -> useModule("gg.essential:architectury-loom:${requested.version}")
            }
        }
    }
}

include("annotations")
rootProject.name = "HypixelCry"